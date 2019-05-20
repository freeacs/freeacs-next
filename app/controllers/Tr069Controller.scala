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

  def provision: Action[_] = secureAction.authenticate.async(parseAsXmlOrText) { implicit request =>
    getSessionData(request).flatMap {
      case Right(sessionData) =>
        processRequest(request, sessionData, getBodyAsXml(request)).flatMap {
          case Right((updatedSessionData, result)) =>
            putSessionData(request.sessionId, sessionData, updatedSessionData).flatMap {
              case Right(Done) =>
                Future.successful(result.withSession(request.session))
              case Left(error) =>
                Future.successful(error)
            }
          case Left(error) =>
            Future.successful(error)
        }
      case Left(error) =>
        Future.successful(error)
    }
  }

  private val parseAsXmlOrText = parse.using { request =>
    request.contentType.map(_.toLowerCase(Locale.ENGLISH).split(";").head) match {
      case Some("application/xml") | Some("text/xml") | Some("text/html") if request.hasBody =>
        parse.tolerantXml
      case _ =>
        parse.tolerantText
    }
  }

  private def getSessionData(request: SecureRequest[_]): Future[Either[Result, Option[SessionData]]] =
    cache.get[SessionData](sessionDataKey(request.sessionId)).map(Right.apply).recoverWith {
      case e: Exception =>
        logger.error("Failed to get session data", e)
        Future.successful(Left(Ok))
    }

  private def getBodyAsXml(request: SecureRequest[_]): Node =
    request.body match {
      case xml: NodeSeq =>
        xml.headOption.getOrElse(<Empty />)
      case _ =>
        <Empty />
    }

  private def processRequest(
      request: SecureRequest[_],
      maybeSessionData: Option[SessionData],
      payload: Node
  ): Future[Either[Result, (SessionData, Result)]] = {
    val method = CwmpMethod.fromNode(payload).getOrElse(CwmpMethod.EM)
    (method match {
      case CwmpMethod.IN if maybeSessionData.isDefined =>
        logger.error("Misplaced Inform (there is already a session, will now ask cpe to close connection)")
        Future.successful(Right((maybeSessionData.head, Ok.withHeaders("Connection" -> "close"))))

      case CwmpMethod.IN if maybeSessionData.isEmpty =>
        processInform(request.sessionId, request.username, payload)

      case CwmpMethod.EM if maybeSessionData.isDefined =>
        processEmpty(maybeSessionData.head)

      case CwmpMethod.GPNr if maybeSessionData.isDefined =>
        // TODO:
        // 1. Extract parameters from GPN response
        // 2. Find all parameters that does not exist in unit type
        // 3. Save those parameters in unit type
        // 4. Update unit in sessionData
        Future.successful(
          Right(
            (
              maybeSessionData.head,
              createGetParameterValuesResponse(
                getParamsToRead(maybeSessionData.head),
                maybeSessionData.map(_.cwmpVersion).head
              )
            )
          )
        )

      case CwmpMethod.GPVr if maybeSessionData.isDefined =>
        // TODO:
        // 1. Extract parameter values from GPV response
        // 2. Find all parameters where ACS has a different set value
        // 3. Ask CPE to save the values of those parameters
        // 4. Then save those units in the ACS
        Future.successful(
          Right((maybeSessionData.head, createSetParameterValuesResponse(maybeSessionData.head.cwmpVersion)))
        )

      case CwmpMethod.SPVr if maybeSessionData.isDefined =>
        Future.successful(Right((maybeSessionData.head, Ok.withHeaders("Connection" -> "close"))))

      case otherMethod =>
        logger.debug(s"Got ${otherMethod.abbr} method, answering with Ok")
        Future.successful(Left(Ok))
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

  private def processEmpty(sessionData: SessionData): Future[Either[String, (SessionData, Result)]] =
    if (shouldDiscoverDeviceParameters(sessionData.unit)) {
      maybeClearDiscoverParam(sessionData.unit).map(_.map { _ =>
        (
          sessionData,
          createGetParameterNamesResponse(
            sessionData.keyRoot.get,
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

  private def maybeClearDiscoverParam(unit: AcsUnit): Future[Either[String, Done]] =
    getDiscoverUnitParam(unit) match {
      case Some(up) if up.value.contains("1") =>
        val discoverUtp = getDiscoverUnitTypeParam(unit)
        unitService
          .upsertParameters(
            Seq(
              AcsUnitParameter(
                unit.unitId,
                discoverUtp.unitTypeParamId,
                discoverUtp.name,
                Some("0")
              )
            )
          )
          .map(_ => Right(Done))
          .recoverWith {
            case e: Exception =>
              logger.error(s"Failed to clear discover param for unit ${unit.unitId}", e)
              Future.successful(
                Left(
                  s"Failed to clear discover param for unit ${unit.unitId}: ${e.getLocalizedMessage}"
                )
              )
          }
      case _ =>
        Future.successful(Right(Done))
    }

  private def getParamsToRead(sessionData: SessionData) =
    sessionData.unit.unitTypeParams.filter { utp =>
      utp.name == sessionData.PERIODIC_INFORM_INTERVAL || utp.flags.contains("A")
    }

  private def getDiscoverUnitParam(unit: AcsUnit) =
    unit.params.find(_.unitTypeParamName == SystemParameters.DISCOVER.name)

  private def getDiscoverUnitTypeParam(unit: AcsUnit) =
    unit.unitTypeParams.find(_.name == SystemParameters.DISCOVER.name).head

  private def shouldDiscoverDeviceParameters(unit: AcsUnit) =
    unit.unitTypeParams.forall(_.flags.contains("X")) &&
      (settings.discoveryMode || unitDiscoveryParamIsSet(unit))

  private def unitDiscoveryParamIsSet(unit: AcsUnit) =
    unit.params.exists(
      p => p.unitTypeParamName == SystemParameters.DISCOVER.name && p.value.contains("1")
    )

  private def putSessionData(
      sessionId: String,
      maybeSessionData: Option[SessionData],
      updatedSessionData: SessionData
  ): Future[Either[Result, Done]] =
    if (!maybeSessionData.contains(updatedSessionData)) {
      cache.set(sessionDataKey(sessionId), updatedSessionData).map(Right.apply).recoverWith {
        case e: Exception =>
          logger.error("Failed to update session data", e)
          Future.successful(Left(Ok))
      }
    } else {
      Future.successful(Right(Done))
    }

  private def processInform(
      sessionId: String,
      maybeUsername: Option[String],
      payload: Node
  ): Future[Either[String, (SessionData, Result)]] = {
    (for {
      header   <- getHeader(payload)
      events   <- getEvents(payload)
      params   <- getParams(payload)
      deviceId <- getDeviceId(payload)
    } yield {
      val username = maybeUsername.getOrElse(deviceId.unitId)
      unitService
        .find(username)
        .flatMap {
          case Some(unit) =>
            Future.successful(unit)
          case None =>
            unitService.createAndReturnUnit(username, "Default", deviceId.productClass.underlying)
        }
        .map(
          unit =>
            SessionData(
              username = username,
              sessionId = sessionId,
              unit = unit,
              deviceId = deviceId,
              events = events,
              params = params,
              header = header
          )
        )
        .flatMap(updateAcsParams)
        .map(sessionData => (sessionData, createInformResponse(sessionData.cwmpVersion)))
    }).mapToFutureEither.recoverWith {
      case e: Exception =>
        logger.error("Failed to load unit", e)
        Future.successful(Left("Failed to load unit"))
    }
  }

  private def updateAcsParams(sessionData: SessionData): Future[SessionData] =
    for {
      firstConnect <- getTimestamp(sessionData.unit, FIRST_CONNECT_TMS, update = false)
      lastConnect  <- getTimestamp(sessionData.unit, LAST_CONNECT_TMS, update = true)
      deviceParams <- getDeviceParameters(sessionData, sessionData.unit)
      parameters = getParamsToUpdate(firstConnect +: lastConnect +: deviceParams, sessionData.unit.params)
      _                <- unitService.upsertParameters(parameters)
      maybeUpdatedUnit <- unitService.find(sessionData.unit.unitId)
    } yield maybeUpdatedUnit.map(unit => sessionData.copy(unit = unit)).getOrElse(sessionData)

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

  private def getDeviceParameters(sessionData: SessionData, unit: AcsUnit): Future[Seq[AcsUnitParameter]] =
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

  private def getHeader(payload: Node): Either[String, HeaderStruct] =
    HeaderStruct.fromNode(payload) match {
      case Some(header) => Right(header)
      case None         => Left("Missing header in payload")
    }

  private def getEvents(payload: Node): Either[String, Seq[EventStruct]] =
    EventStruct.fromNode(payload) match {
      case seq: Seq[EventStruct] if seq.nonEmpty => Right(seq)
      case _                                     => Left("Missing events")
    }

  private def getParams(payload: Node): Either[String, Seq[ParameterValueStruct]] =
    ParameterValueStruct.fromNode(payload) match {
      case seq: Seq[ParameterValueStruct] if seq.nonEmpty => Right(seq)
      case _                                              => Left("Missing params")
    }

  private def getDeviceId(payload: Node): Either[String, DeviceIdStruct] =
    DeviceIdStruct.fromNode(payload) match {
      case Some(deviceIdStruct) => Right(deviceIdStruct)
      case None                 => Left("Missing deviceId")
    }

  private def sessionDataKey(sessionId: String) = s"$sessionId-data"
}
