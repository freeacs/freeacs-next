package controllers

import java.time.LocalDateTime
import java.util.Locale

import akka.Done
import config.Settings
import models.SystemParameters._
import models._
import play.api.Logging
import play.api.cache.AsyncCacheApi
import play.api.i18n.I18nSupport
import play.api.mvc._
import services.{UnitService, UnitTypeService}
import util.MonadTransformers._

import scala.concurrent.{ExecutionContext, Future}
import scala.xml.{Node, NodeSeq}

class Tr069Controller(
    cc: ControllerComponents,
    unitService: UnitService,
    unitTypeService: UnitTypeService,
    cache: AsyncCacheApi,
    secureAction: SecureAction,
    settings: Settings
)(
    implicit ec: ExecutionContext
) extends AbstractController(cc)
    with I18nSupport
    with Logging {

  def provision = secureAction.authenticate.async(parseAsXmlOrText) { implicit request =>
    getSessionData(request).flatMap {
      case Right(sessionData) =>
        processRequest(sessionData, getBodyAsXml(request)).flatMap {
          case Right((updatedSessionData, result)) =>
            putSessionData(request, sessionData, updatedSessionData).flatMap {
              case Right(Done) =>
                if (updatedSessionData.unit.isDefined) {
                  Future.successful(result.withSession(request.session))
                } else {
                  Future.successful(result)
                }
              case Left(error) => Future.successful(error)
            }
          case Left(error) => Future.successful(error)
        }
      case Left(error) => Future.successful(error)
    }
  }

  private val parseAsXmlOrText = parse.using { request =>
    request.contentType.map(_.toLowerCase(Locale.ENGLISH).split(";").head) match {
      case Some("application/xml") | Some("text/xml") | Some("text/html") if request.hasBody =>
        parse.tolerantXml
      case _ => parse.tolerantText
    }
  }

  private def getSessionData(request: SecureRequest[_]): Future[Either[Result, SessionData]] =
    cache
      .getOrElseUpdate[SessionData](sessionDataKey(request.sessionId)) {
        Future.successful(SessionData(sessionId = request.sessionId, username = request.username))
      }
      .map(Right.apply)
      .recoverWith {
        case e: Exception =>
          logger.error("Failed to get session data", e)
          Future.successful(Left(Ok))
      }

  private def getBodyAsXml(request: SecureRequest[_]): Node =
    request.body match {
      case xml: NodeSeq => xml.headOption.getOrElse(<Empty />)
      case _            => <Empty />
    }

  private def processRequest(
      sessionData: SessionData,
      payload: Node
  ): Future[Either[Result, (SessionData, Result)]] = {
    val method = CwmpMethod.fromNode(payload).getOrElse(CwmpMethod.EM)
    (method match {
      case CwmpMethod.IN =>
        processInform(sessionData, payload)

      case CwmpMethod.EM if sessionData.unit.isDefined =>
        processEmpty(sessionData)

      case CwmpMethod.GPNr =>
        // TODO:
        // 1. Extract parameters from GPN response
        // 2. Find all parameters that does not exist in unit type
        // 3. Save those parameters in unit type
        // 4. Update unit in sessionData
        Future.successful(
          Right(
            (
              sessionData,
              createGetParameterValuesResponse(getParamsToRead(sessionData), sessionData.cwmpVersion)
            )
          )
        )

      case CwmpMethod.GPVr if sessionData.unit.isDefined =>
        // TODO:
        // 1. Extract parameter values from GPV response
        // 2. Find all parameters where ACS has a different set value
        // 3. Ask CPE to save the values of those parameters
        // 4. Then save those units in the ACS
        Future.successful(Right((sessionData, createSetParameterValuesResponse(sessionData.cwmpVersion))))

      case CwmpMethod.SPVr if sessionData.unit.isDefined =>
        Future.successful(Right((sessionData, Ok.withHeaders("Connection" -> "close"))))

      case otherMethod =>
        logger.debug(s"Got ${otherMethod.abbr} method, answering with Ok")
        Future.successful(Right((sessionData, Ok)))
    }).flatMap {
      case Left(error) =>
        logger.error(s"Failed to process request: $error")
        Future.successful(Left(Ok))
      case Right((finalData, result)) =>
        Future.successful(
          Right((finalData.copy(requests = finalData.requests ++ Seq(method)), result))
        )
    }
  }

  private def processEmpty(sessionData: SessionData) =
    if (shouldDiscoverDeviceParameters(sessionData)) {
      maybeClearDiscoverParam(sessionData).map(_.map { _ =>
        (
          sessionData,
          createGetParameterNamesResponse(
            sessionData.unsafeKeyRoot,
            sessionData.cwmpVersion
          )
        )
      })
    } else {
      Future.successful(
        Right(
          (
            sessionData,
            createGetParameterValuesResponse(getParamsToRead(sessionData), sessionData.cwmpVersion)
          )
        )
      )
    }

  private def maybeClearDiscoverParam(sessionData: SessionData): Future[Either[String, Done]] =
    getDiscoverUnitParam(sessionData) match {
      case Some(up) if up.value.contains("1") =>
        val discoverUtp = getDiscoverUnitTypeParam(sessionData)
        unitService
          .upsertParameters(
            Seq(
              AcsUnitParameter(
                sessionData.unsafeGetUnitId,
                discoverUtp.unitTypeParamId,
                discoverUtp.name,
                Some("0")
              )
            )
          )
          .map(_ => Right(Done))
          .recoverWith {
            case e: Exception =>
              logger.error(s"Failed to clear discover param for unit ${sessionData.unsafeGetUnitId}", e)
              Future.successful(
                Left(
                  s"Failed to clear discover param for unit ${sessionData.unsafeGetUnitId}: ${e.getLocalizedMessage}"
                )
              )
          }
      case _ =>
        Future.successful(Right(Done))
    }

  private def getParamsToRead(sessionData: SessionData) =
    sessionData.unsafeGetUnit.unitTypeParams.filter { utp =>
      utp.name == sessionData.PERIODIC_INFORM_INTERVAL || utp.flags.contains("A")
    }

  private def getDiscoverUnitParam(sessionData: SessionData) =
    sessionData.unsafeGetUnit.params.find(_.unitTypeParamName == SystemParameters.DISCOVER.name)

  private def getDiscoverUnitTypeParam(sessionData: SessionData) =
    sessionData.unsafeGetUnit.unitTypeParams.find(_.name == SystemParameters.DISCOVER.name).head

  private def shouldDiscoverDeviceParameters(sessionData: SessionData) =
    sessionData.unit.exists(_.unitTypeParams.forall(_.flags.contains("X"))) &&
      (settings.discoveryMode || unitDiscoveryParamIsSet(sessionData))

  private def unitDiscoveryParamIsSet(sessionData: SessionData) =
    sessionData.unit.exists(
      _.params.exists(
        p => p.unitTypeParamName == SystemParameters.DISCOVER.name && p.value.contains("1")
      )
    )

  private def putSessionData(
      request: SecureRequest[_],
      sessionData: SessionData,
      updatedSessionData: SessionData
  ): Future[Either[Result, Done]] =
    if (sessionData != updatedSessionData) {
      cache.set(sessionDataKey(request.sessionId), updatedSessionData).map(Right.apply).recoverWith {
        case e: Exception =>
          logger.error("Failed to update session data", e)
          Future.successful(Left(Ok))
      }
    } else {
      Future.successful(Right(Done))
    }

  private def processInform(
      sessionData: SessionData,
      payload: Node
  ): Future[Either[String, (SessionData, Result)]] =
    getHeader(sessionData, payload)
      .flatMap(getDeviceId(_, payload))
      .flatMap(getEvents(_, payload))
      .flatMap(getParams(_, payload))
      .map(
        loadUnit(_)
          .map(sessionData => sessionData.copy(username = sessionData.unitId))
          .flatMap(maybeCreateUnit)
          .flatMap(maybeUpdateAcsParams)
          .map { sessionData =>
            if (sessionData.unit.isDefined) {
              (sessionData, createInformResponse(sessionData.cwmpVersion))
            } else {
              logger.warn(s"Unit data is missing")
              (sessionData, Ok)
            }
          }
          .recoverWith {
            case e: Exception =>
              logger.error("Something failed when loading and updating unit", e)
              Future.successful((sessionData, Ok))
          }
      )
      .mapToFutureEither

  private def loadUnit(sessionData: SessionData): Future[SessionData] =
    sessionData.unitId match {
      case Some(username) =>
        unitService.find(username).map { maybeUnit =>
          sessionData.copy(unit = maybeUnit)
        }
      case _ =>
        Future.successful(sessionData)
    }

  private def maybeCreateUnit(sessionData: SessionData): Future[SessionData] =
    sessionData.unit match {
      case None if settings.discoveryMode =>
        unitService
          .createAndReturnUnit(
            sessionData.unsafeGetUnitId,
            "Default",
            sessionData.unsafeGetProductClass(settings.appendHwVersion)
          )
          .map(unit => sessionData.copy(unit = Some(unit)))
      case _ =>
        logger.debug("Unit is already loaded or discovery mode is not enabled. Not creating.")
        Future.successful(sessionData)
    }

  private def maybeUpdateAcsParams(sessionData: SessionData): Future[SessionData] =
    sessionData.unit match {
      case Some(unit) =>
        for {
          firstConnect <- getTimestamp(unit, FIRST_CONNECT_TMS, update = false)
          lastConnect  <- getTimestamp(unit, LAST_CONNECT_TMS, update = true)
          deviceParams <- getDeviceParameters(sessionData, unit)
          parameters = getParamsToUpdate(firstConnect +: lastConnect +: deviceParams, unit.params)
          _           <- unitService.upsertParameters(parameters)
          updatedUnit <- unitService.find(unit.unitId)
        } yield sessionData.copy(unit = updatedUnit)
      case _ =>
        Future.successful(sessionData)
    }

  private def getParamsToUpdate(
      newParameters: Seq[AcsUnitParameter],
      existingParams: Seq[AcsUnitParameter]
  ) =
    newParameters.filter(
      p =>
        existingParams.exists(
          up => up.unitTypeParamName == p.unitTypeParamName && up.value != p.value
        ) || p.value.isDefined
    )

  private def getDeviceParameters(sessionData: SessionData, unit: AcsUnit): Future[Seq[AcsUnitParameter]] = {
    if (sessionData.keyRoot.isEmpty) {
      return Future.successful(Seq.empty)
    }
    for {
      softwareVersion <- getOrCreateUnitTypeParameter(unit, NamedParameter(sessionData.SOFTWARE_VERSION, "R"))
      pII             <- getOrCreateUnitTypeParameter(unit, NamedParameter(sessionData.PERIODIC_INFORM_INTERVAL, "RW"))
      connReqUrl      <- getOrCreateUnitTypeParameter(unit, NamedParameter(sessionData.CONNECTION_URL, "R"))
      connReqUser     <- getOrCreateUnitTypeParameter(unit, NamedParameter(sessionData.CONNECTION_USERNAME, "R"))
      connReqPass     <- getOrCreateUnitTypeParameter(unit, NamedParameter(sessionData.CONNECTION_PASSWORD, "R"))
    } yield
      Seq(
        makeUnitParam(unit.unitId, softwareVersion, sessionData.params),
        makeUnitParam(unit.unitId, pII, sessionData.params),
        makeUnitParam(unit.unitId, connReqUrl, sessionData.params),
        makeUnitParam(unit.unitId, connReqUser, sessionData.params),
        makeUnitParam(unit.unitId, connReqPass, sessionData.params)
      )
  }

  private def makeUnitParam(
      unitId: String,
      softwareVersion: AcsUnitTypeParameter,
      params: Seq[ParameterValueStruct]
  ) =
    AcsUnitParameter(
      unitId,
      softwareVersion.unitTypeParamId,
      softwareVersion.name,
      params.find(_.name == softwareVersion.name).map(_.value)
    )

  private def getTimestamp(
      unit: AcsUnit,
      param: Parameter,
      update: Boolean
  ): Future[AcsUnitParameter] = {
    getOrCreateUnitTypeParameter(unit, param).map { unitTypeParameter =>
      val ts = LocalDateTime.now().toString
      unit.params
        .find(_.unitTypeParamName == param.name)
        .map { p =>
          if (update)
            p.copy(value = Some(ts))
          else
            p
        }
        .getOrElse(
          AcsUnitParameter(unit.unitId, unitTypeParameter.unitTypeParamId, unitTypeParameter.name, Some(ts))
        )
    }
  }

  private def getOrCreateUnitTypeParameter(unit: AcsUnit, param: Parameter) =
    unit.unitTypeParams.find(_.name == param.name) match {
      case Some(unitTypeParameter) =>
        Future.successful(unitTypeParameter)
      case None =>
        unitTypeService.createUnitTypeParameter(
          unit.profile.unitType.unitTypeId.get,
          param.name,
          param.flag
        )
    }

  private def createSetParameterValuesResponse(cwmpVersion: String): Result =
    SoapEnvelope(cwmpVersion) {
      <cwmp:SetParameterValues>
        <ParameterNames soapenc:arrayType="cwmp:ParameterValueStruct[1]">
          <Name>InternetGatewayDevice.ManagementServer.PeriodicInformInterval</Name>
          <Value>8600</Value>
        </ParameterNames>
      </cwmp:SetParameterValues>
    }

  private def createGetParameterNamesResponse(keyRoot: String, cwmpVersion: String): Result =
    SoapEnvelope(cwmpVersion) {
      <cwmp:GetParameterNames>
        <ParameterNames>
          <ParameterPath>{keyRoot}</ParameterPath>
          <NextLevel>false</NextLevel>
        </ParameterNames>
      </cwmp:GetParameterNames>
    }

  private def createGetParameterValuesResponse(
      params: Seq[AcsUnitTypeParameter],
      cwmpVersion: String
  ): Result =
    SoapEnvelope(cwmpVersion) {
      <cwmp:GetParameterValues>
        <ParameterNames soapenc:arrayType={s"xsd:string[${params.length}]"}>
          {params.map(param => <string>{param.name}</string>)}
        </ParameterNames>
      </cwmp:GetParameterValues>
    }

  private def createInformResponse(cwmpVersion: String): Result =
    SoapEnvelope(cwmpVersion) {
      <cwmp:InformResponse>
        <MaxEnvelopes>1</MaxEnvelopes>
      </cwmp:InformResponse>
    }

  private def SoapEnvelope(cwmpVersion: String)(body: => Node): Result =
    Soap(Envelope(cwmpVersion)(body))

  private def Envelope(cwmpVersion: String)(body: => Node): Node =
    <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                      xmlns:soapenc="http://schemas.xmlsoap.org/soap/encoding/"
                      xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                      xmlns:cwmp={s"urn:dslforum-org:cwmp-$cwmpVersion"}>
      <soapenv:Header>
        <cwmp:ID soapenv:mustUnderstand="1">1</cwmp:ID>
      </soapenv:Header>
      <soapenv:Body>
        {body}
      </soapenv:Body>
    </soapenv:Envelope>

  private def Soap(body: => Node): Result =
    Ok(body).withHeaders("SOAPAction" -> "").as("text/xml")

  private def getHeader(sessionData: SessionData, payload: Node): Either[String, SessionData] =
    HeaderStruct.fromNode(payload) match {
      case Some(header) => Right(sessionData.copy(header = Some(header)))
      case None         => Left("Missing header in payload")
    }

  private def getEvents(sessionData: SessionData, payload: Node): Either[String, SessionData] =
    EventStruct.fromNode(payload) match {
      case seq: Seq[EventStruct] if seq.nonEmpty => Right(sessionData.copy(events = seq))
      case _                                     => Left("Missing events")
    }

  private def getParams(sessionData: SessionData, payload: Node): Either[String, SessionData] =
    ParameterValueStruct.fromNode(payload) match {
      case seq: Seq[ParameterValueStruct] if seq.nonEmpty => Right(sessionData.copy(params = seq))
      case _                                              => Left("Missing params")
    }

  private def getDeviceId(sessionData: SessionData, payload: Node): Either[String, SessionData] =
    DeviceIdStruct.fromNode(payload) match {
      case Some(deviceIdStruct) => Right(sessionData.copy(deviceId = Some(deviceIdStruct)))
      case None                 => Left("Missing deviceId")
    }

  private def sessionDataKey(sessionId: String) = s"$sessionId-data"
}
