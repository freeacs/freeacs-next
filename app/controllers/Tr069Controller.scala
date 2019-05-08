package controllers
import akka.Done
import models._
import play.api.Logging
import play.api.cache.AsyncCacheApi
import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, ControllerComponents, Result}
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
      payload  <- request.body.asXml.flatMap(_.headOption)
      method   <- CwmpMethod.fromNode(payload)
      header   <- HeaderStruct.fromNode(payload)
      deviceId <- DeviceIdStruct.fromNode(payload)
      rSession <- request.session
                   .get(SESSION_KEY)
                   .map(_ => request.session)
                   .orElse(Some(request.session + (SESSION_KEY -> java.util.UUID.randomUUID.toString)))
      sessionId <- rSession.get(SESSION_KEY)
    } yield {
      for {
        sessionData                  <- getSessionData(sessionId, header, deviceId, payload)
        (updatedSessionData, result) <- processRequest(sessionData, method)
        _                            <- updateSessionData(sessionId, sessionData, updatedSessionData)
      } yield result.withSession(rSession)
    }).getOrElse {
      logger.warn("Got no payload and no method, assuming empty")
      Future.successful(Ok)
    }
  }

  private def processRequest(sessionData: SessionData, method: CwmpMethod): Future[(SessionData, Result)] = {
    method match {
      case CwmpMethod.IN =>
        val unitId = sessionData.unit.map(_.getId).getOrElse("N/A")
        logger.warn(s"Got the following Inform from unit: $unitId:\n$sessionData")
        val response = Ok(
          <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
            <soapenv:Body>
              <cwmp:InformResponse xmlns:cwmp="urn:dslforum-org:cwmp-1-0">
                <MaxEnvelopes>1</MaxEnvelopes>
              </cwmp:InformResponse>
            </soapenv:Body>
          </soapenv:Envelope>
        ).withHeaders("SOAPAction" -> "")
        Future.successful((sessionData, response))
      case _ =>
        Future.successful((sessionData, NotImplemented))
    }
  }

  private def getSessionData(
      sessionId: String,
      header: HeaderStruct,
      deviceId: DeviceIdStruct,
      payload: Node
  ): Future[SessionData] =
    cache.getOrElseUpdate[SessionData](sessionDataKey(sessionId)) {
      unitService
        .find(deviceId.unitId)
        .map(
          unit =>
            SessionData(
              sessionId,
              unit,
              header,
              deviceId,
              EventStruct.fromNode(payload),
              ParameterValueStruct.fromNode(payload)
          )
        )
    }

  private def updateSessionData(
      sessionId: String,
      sessionData: SessionData,
      updateSessionData: SessionData
  ): Future[Done] =
    if (sessionData != updateSessionData)
      cache.set(sessionDataKey(sessionId), updateSessionData)
    else
      Future.successful(Done)
}

object Tr069Controller {
  val SESSION_KEY = "uuid"

  def sessionDataKey(sessionId: String) = s"$sessionId-data"
}
