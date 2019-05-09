package controllers

import io.kanaka.monadic.dsl._
import akka.Done
import models._
import play.api.Logging
import play.api.cache.AsyncCacheApi
import play.api.i18n.I18nSupport
import play.api.mvc._
import services.{ProfileService, UnitService, UnitTypeService}

import scala.concurrent.{ExecutionContext, Future}
import scala.xml.Node

class Tr069Controller(
    cc: ControllerComponents,
    unitService: UnitService,
    profileService: ProfileService,
    unitTypeService: UnitTypeService,
    cache: AsyncCacheApi
)(
    implicit ec: ExecutionContext
) extends AbstractController(cc)
    with I18nSupport
    with Logging {

  import Tr069Controller._

  def provision = Action.async { implicit request =>
    (for {
      payload   <- request.body.asXml.flatMap(_.headOption)
      method    <- CwmpMethod.fromNode(payload)
      session   <- getSession(request)
      sessionId <- session.get(SESSION_KEY)
    } yield (payload, method, session, sessionId)) match {
      case Some((payload, method, session, sessionId)) =>
        for {
          header      <- getHeader(payload) ?| (error => BadRequest(error))
          sessionData <- getSessionData(sessionId, header) ?| InternalServerError("Failed to get session")
          result      <- processRequest(sessionData, method, payload) ?| (error => InternalServerError(error))
          _           <- updateSessionData(sessionId, sessionData, result._1) ?| InternalServerError("Failed to update session")
        } yield result._2.withSession(session)
      case _ => Future.successful(Ok)
    }
  }

  private def getSession(request: Request[AnyContent]): Option[Session] =
    request.session
      .get(SESSION_KEY)
      .map(_ => request.session)
      .orElse(Some(request.session + (SESSION_KEY -> java.util.UUID.randomUUID.toString)))

  private def processRequest(
      sessionData: SessionData,
      method: CwmpMethod,
      payload: Node
  ): Future[Either[String, (SessionData, Result)]] = {
    method match {
      case CwmpMethod.IN =>
        Future.successful(
          for {
            withDeviceId <- getDeviceId(sessionData, payload)
            withEvents   <- getEvents(withDeviceId, payload)
            withParams   <- getParams(withEvents, payload)
            sessionData = withParams
          } yield {
            val unitId = sessionData.unit.map(_.getId).getOrElse("N/A")
            logger.warn(s"Got the following Inform from unit: $unitId:\n$sessionData")
            val result = Ok(
              <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
                <soapenv:Body>
                  <cwmp:InformResponse xmlns:cwmp="urn:dslforum-org:cwmp-1-0">
                    <MaxEnvelopes>1</MaxEnvelopes>
                  </cwmp:InformResponse>
                </soapenv:Body>
              </soapenv:Envelope>
            ).withHeaders("SOAPAction" -> "")
            (sessionData, result)
          }
        )
      case _ =>
        Future.successful(Right((sessionData, NotImplemented)))
    }
  }

  private def getHeader(payload: Node): Either[String, HeaderStruct] =
    HeaderStruct.fromNode(payload) match {
      case Some(header) => Right(header)
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

  private def getSessionData(sessionId: String, header: HeaderStruct): Future[SessionData] =
    cache.getOrElseUpdate[SessionData](sessionDataKey(sessionId)) {
      Future.successful(SessionData(sessionId, header))
    }

  private def updateSessionData(sessionId: String, sessionData: SessionData, updateSessionData: SessionData): Future[Done] =
    if (sessionData != updateSessionData)
      cache.set(sessionDataKey(sessionId), updateSessionData)
    else
      Future.successful(Done)
}

object Tr069Controller {
  val SESSION_KEY = "uuid"

  def sessionDataKey(sessionId: String) = s"$sessionId-data"
}
