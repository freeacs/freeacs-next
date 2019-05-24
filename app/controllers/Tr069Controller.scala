package controllers

import java.util.Locale

import akka.Done
import config.Settings
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

  def provision: Action[_] = secureAction.verify.async(parseAsXmlOrText) { implicit request =>
    getSessionData(request.sessionId).flatMap {
      case Right(sessionData) =>
        processRequest(request.sessionId, request.username, sessionData, getBodyAsXml(request.body)).flatMap {
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

  private def getSessionData(sessionId: String): Future[Either[Result, Option[SessionData]]] =
    cache.get[SessionData](sessionDataKey(sessionId)).map(Right.apply).recoverWith {
      case e: Exception =>
        logger.error("Failed to get session data", e)
        Future.successful(Left(Ok))
    }

  private def getBodyAsXml(body: Any): Node =
    body match {
      case xml: NodeSeq =>
        xml.headOption.getOrElse(<Empty />)
      case _ =>
        <Empty />
    }

  private def processRequest(
      sessionId: String,
      username: Option[String],
      maybeSessionData: Option[SessionData],
      payload: Node
  ): Future[Either[Result, (SessionData, Result)]] = {
    val method = CwmpMethod.fromNode(payload).getOrElse(CwmpMethod.EM)
    (method match {
      case CwmpMethod.IN if maybeSessionData.isDefined =>
        Future.successful(Left("Misplaced Inform (there is already a session)"))

      case CwmpMethod.IN if maybeSessionData.isEmpty =>
        processInform(sessionId, username, payload)

      case CwmpMethod.EM if maybeSessionData.isDefined =>
        processEmpty(maybeSessionData.head)

      case CwmpMethod.GPNr if maybeSessionData.isDefined =>
        processGetParameterNamesResponse(maybeSessionData.head, payload)

      case CwmpMethod.GPVr if maybeSessionData.isDefined =>
        processGetParameterValuesResponse(maybeSessionData.head, payload)

      case CwmpMethod.SPVr if maybeSessionData.isDefined =>
        closeConnection(maybeSessionData.head)

      case otherMethod =>
        Future.successful(
          Left(s"Got ${otherMethod.abbr}, but could not handle it. SessionData: $maybeSessionData")
        )
    }).flatMap {
      case Left(error: String) =>
        logger.error(s"Failed to process request: $error")
        Future.successful(Left(Ok))
      case Right((sessionData: SessionData, result: Result)) =>
        val finalSessionData = sessionData.copy(requests = sessionData.requests ++ Seq(method))
        Future.successful(Right((finalSessionData, result)))
    }
  }

  private def processGetParameterValuesResponse(
      sessionData: SessionData,
      payload: Node
  ): Future[Right[String, (SessionData, Result)]] = {
    val flagMap = sessionData.unit.unitTypeParams.map(p => p.name -> p.flags).toMap
    val paramToSet = ParameterValueStruct.fromNode(payload).flatMap { p =>
      sessionData.unit.params.find(_.unitTypeParamName == p.name).flatMap { up =>
        if (up.value.isDefined && !up.value.contains(p.value)
            && flagMap(up.unitTypeParamName).contains("W")) {
          Some(p.copy(value = up.value.head))
        } else {
          None
        }
      }
    }
    if (paramToSet.isEmpty) {
      closeConnection(sessionData)
    } else {
      Future.successful(
        Right((sessionData, createSetParameterValues(paramToSet, sessionData.cwmpVersion)))
      )
    }
  }

  private def closeConnection(sessionData: SessionData) =
    Future.successful(
      Right((sessionData, Ok.withHeaders("Connection" -> "close")))
    )

  private def processGetParameterNamesResponse(sessionData: SessionData, payload: Node) =
    Future
      .sequence(
        ParameterInfoStruct
          .fromNode(payload)
          .filter { info =>
            sessionData.unit.unitTypeParams.exists(_.name != info.name)
          }
          .map { info =>
            unitTypeService.createUnitTypeParameter(
              sessionData.unit.profile.unitType.unitTypeId.head,
              info.name,
              s"R${if (info.writable) "W" else ""}"
            )
          }
      )
      .flatMap { addedUnitTypeParams =>
        if (addedUnitTypeParams.nonEmpty) {
          unitService.find(sessionData.unit.unitId).map {
            case Some(unit) => sessionData.copy(unit = unit)
            case _          => sessionData
          }
        } else {
          Future.successful(sessionData)
        }
      }
      .map { sessionData =>
        val paramsToRead = getParamsToRead(sessionData)
        Right((sessionData, createGetParameterValues(paramsToRead, sessionData.cwmpVersion)))
      }

  private def processEmpty(sessionData: SessionData): Future[Either[String, (SessionData, Result)]] =
    if (shouldDiscoverDeviceParameters(sessionData.unit)) {
      maybeClearDiscoverParam(sessionData.unit).map(_.map { _ =>
        (sessionData, createGetParameterNames(sessionData.keyRoot.get, sessionData.cwmpVersion))
      })
    } else {
      Future.successful(
        Right((sessionData, createGetParameterValues(getParamsToRead(sessionData), sessionData.cwmpVersion)))
      )
    }

  private def maybeClearDiscoverParam(unit: AcsUnit): Future[Either[String, Done]] =
    getDiscoverUnitParam(unit) match {
      case Some(up) if up.value.contains("1") =>
        unitService
          .upsertParameters(
            Seq(
              AcsUnitParameter(
                unit.unitId,
                up.unitTypeParamId,
                up.unitTypeParamName,
                Some("0")
              )
            )
          )
          .map(_ => Right(Done))
          .recoverWith {
            case e: Exception =>
              val errorMsg = s"Failed to clear discover param for unit ${unit.unitId}"
              logger.error(errorMsg, e)
              Future.successful(Left(errorMsg))
          }
      case _ =>
        Future.successful(Right(Done))
    }

  private def getParamsToRead(sessionData: SessionData) =
    sessionData.PERIODIC_INFORM_INTERVAL +: sessionData.unit.unitTypeParams
      .filter(_.flags.contains("A"))
      .map(_.name)
      .distinct

  private def getDiscoverUnitParam(unit: AcsUnit) =
    unit.params.find(_.unitTypeParamName == SystemParameters.DISCOVER.name)

  private def shouldDiscoverDeviceParameters(unit: AcsUnit) =
    (unit.unitTypeParams.forall(up => up.flags.contains("X")) && settings.discoveryMode) ||
      unitDiscoveryParamIsSet(unit)

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
  ): Future[Either[String, (SessionData, Result)]] =
    (for {
      header   <- getHeader(payload)
      events   <- getEvents(payload)
      params   <- getParamValues(payload)
      deviceId <- getDeviceId(payload)
    } yield {
      val username = maybeUsername.getOrElse(deviceId.unitId)
      unitService
        .find(username)
        .flatMap {
          case Some(unit) =>
            Future.successful(unit)
          case None =>
            val newUnitTypeName = deviceId.productClass.underlying
            unitService.createAndReturnUnit(username, "Default", newUnitTypeName)
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
        .map(sessionData => (sessionData, createInformResponse(sessionData.cwmpVersion)))
    }).mapToFutureEither.recoverWith {
      case e: Exception =>
        val errorMsg = "Failed to load unit"
        logger.error(errorMsg, e)
        Future.successful(Left(errorMsg))
    }

  private def createSetParameterValues(params: Seq[ParameterValueStruct], cwmpVersion: String): Result =
    SoapEnvelope(cwmpVersion) {
      <cwmp:SetParameterValues>
        <ParameterNames soapenc:arrayType= {s"cwmp:ParameterValueStruct[${params.length}]"}>
          {params.map(param => {
            <Name>{param.name}</Name>
            <Value>{param.value}</Value>
          })}
        </ParameterNames>
      </cwmp:SetParameterValues>
    }

  private def createGetParameterNames(keyRoot: String, cwmpVersion: String): Result =
    SoapEnvelope(cwmpVersion) {
      <cwmp:GetParameterNames>
        <ParameterNames>
          <ParameterPath>{keyRoot}</ParameterPath>
          <NextLevel>false</NextLevel>
        </ParameterNames>
      </cwmp:GetParameterNames>
    }

  private def createGetParameterValues(
      params: Seq[String],
      cwmpVersion: String
  ): Result =
    SoapEnvelope(cwmpVersion) {
      <cwmp:GetParameterValues>
        <ParameterNames soapenc:arrayType={s"xsd:string[${params.length}]"}>
          {params.map(param => <string>{param}</string>)}
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
    Right(EventStruct.fromNode(payload))

  private def getParamValues(payload: Node): Either[String, Seq[ParameterValueStruct]] =
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
