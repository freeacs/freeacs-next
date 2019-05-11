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
    cache: AsyncCacheApi,
    secureAction: SecureAction
)(
    implicit ec: ExecutionContext
) extends AbstractController(cc)
    with I18nSupport
    with Logging {

  def provision = secureAction.authenticate.async { implicit request =>
    for {
      sessionData       <- getSessionData(request) ?| InternalServerError("Failed to get session")
      (updated, result) <- processRequest(sessionData, payload(request)) ?| (e => InternalServerError(e))
      _                 <- putSessionData(request, sessionData, updated) ?| InternalServerError("Failed to update session")
    } yield result.withSession(request.session)
  }

  private def payload(request: SecureRequest[AnyContent]) =
    request.body.asXml.flatMap(_.headOption).getOrElse(<Empty />)

  private def processRequest(
      sessionData: SessionData,
      payload: Node
  ): Future[Either[String, (SessionData, Result)]] = {
    val method = CwmpMethod.fromNode(payload).getOrElse(CwmpMethod.EM)
    (method match {
      case CwmpMethod.IN =>
        (for {
          withHeader   <- getHeader(sessionData, payload)
          withDeviceId <- getDeviceId(withHeader, payload)
          withEvents   <- getEvents(withDeviceId, payload)
          sessionData  <- getParams(withEvents, payload)
        } yield {
          for {
            sessionWithMaybeUnit <- getUnit(sessionData)
            // TODO continue the chain by updating parameters in the db etc ..
          } yield processInform(sessionWithMaybeUnit)
        }) match { // we have an Either[String, Future[(SessionData, Result)]], but we need a Future[Either[String, (SessionData, Result)]]
          case Left(s)  => Future.successful(Left(s))
          case Right(f) => f.map(Right(_))
        }
      case otherMethod =>
        logger.warn(s"Got ${otherMethod.abbr} method, answering with NotImplemented")
        Future.successful(Right((sessionData, NotImplemented)))
    }).map(_.map {
      case (updatedSessionData, result) =>
        (updatedSessionData.copy(requests = updatedSessionData.requests ++ Seq(method)), result)
    })
  }

  private def getUnit(sessionData: SessionData): Future[SessionData] =
    sessionData.unitId match {
      case Some(unitId) =>
        unitService.find(unitId).map { maybeUnit =>
          sessionData.copy(unit = maybeUnit)
        }
      case _ =>
        Future.successful(sessionData)
    }

  private def processInform(sessionData: SessionData): (SessionData, Result) = {
    val debug = pprint.PPrinter.BlackWhite.tokenize(sessionData).mkString
    logger.warn(s"Inform from unit [${sessionData.unitId.getOrElse("anonymous")}]. SessionData:\n$debug")
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

  private def getSessionData(request: SecureRequest[AnyContent]): Future[SessionData] =
    cache.getOrElseUpdate[SessionData](sessionDataKey(request.sessionId)) {
      Future.successful(SessionData(sessionId = request.sessionId, unitId = request.username))
    }

  private def putSessionData(
      request: SecureRequest[AnyContent],
      sessionData: SessionData,
      updatedSessionData: SessionData
  ): Future[Done] =
    if (sessionData != updatedSessionData)
      cache.set(sessionDataKey(request.sessionId), updatedSessionData)
    else
      Future.successful(Done)

  private def sessionDataKey(sessionId: String) = s"$sessionId-data"
}
