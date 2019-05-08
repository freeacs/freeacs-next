package controllers
import models._
import play.api.Logging
import play.api.cache.AsyncCacheApi
import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, ControllerComponents}
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

  def provision = Action.async { implicit request =>
    (for {
      payload  <- request.body.asXml.flatMap(_.headOption)
      method   <- CwmpMethod.fromNode(payload)
      header   <- HeaderStruct.fromNode(payload)
      deviceId <- DeviceIdStruct.fromNode(payload)
      session = request.session.get("uuid").map(_ => request.session).getOrElse {
        request.session + ("uuid" -> java.util.UUID.randomUUID.toString)
      }
      sessionId <- session.get("uuid")
    } yield
      method match {
        case CwmpMethod.IN =>
          getSessionData(sessionId, header, deviceId, payload).map { sessionData =>
            val unitId = sessionData.unit.map(_.getId).getOrElse("N/A")
            logger.warn(s"Got the following Inform from unit: $unitId:\n$sessionData")
            Ok(
              <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
                <soapenv:Body>
                  <cwmp:InformResponse xmlns:cwmp="urn:dslforum-org:cwmp-1-0">
                    <MaxEnvelopes>1</MaxEnvelopes>
                  </cwmp:InformResponse>
                </soapenv:Body>
              </soapenv:Envelope>
            ).withHeaders("SOAPAction" -> "").withSession(session)
          }
        case _ =>
          Future.successful(NotImplemented)
      }).getOrElse {
      logger.warn("Got no payload and no method, assuming empty")
      Future.successful(Ok)
    }
  }

  private def getSessionData(
      sessionId: String,
      header: HeaderStruct,
      deviceId: DeviceIdStruct,
      payload: Node
  ): Future[SessionData] =
    cache.getOrElseUpdate[SessionData](s"$sessionId-data") {
      unitService
        .find(deviceId.unitId)
        .map(
          unit =>
            SessionData(
              sessionId,
              unit,
              header,
              EventStruct.fromNode(payload),
              ParameterValueStruct.fromNode(payload),
              deviceId
          )
        )
    }
}
