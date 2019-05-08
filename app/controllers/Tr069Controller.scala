package controllers
import models.{CwmpMethod, EventStruct, HeaderStruct, ParameterValueStruct}
import play.api.Logging
import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, ControllerComponents}
import services.{ProfileService, UnitService, UnitTypeService}

import scala.concurrent.{ExecutionContext, Future}

class Tr069Controller(
    cc: ControllerComponents,
    unitService: UnitService,
    profileService: ProfileService,
    unitTypeService: UnitTypeService
)(
    implicit ec: ExecutionContext
) extends AbstractController(cc)
    with I18nSupport
    with Logging {

  def provision = Action.async { implicit request =>
    (for {
      payload <- request.body.asXml.flatMap(_.headOption)
      method  <- CwmpMethod.fromNode(payload)
      header  <- HeaderStruct.fromNode(payload)
    } yield {
      method match {
        case CwmpMethod.IN =>
          val events = EventStruct.fromNode(payload)
          val params = ParameterValueStruct.fromNode(payload)
          logger.warn(s"Got the following Inform:\n$header\n$events\n$params")
          Future.successful(
            Ok(
              <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
                <soapenv:Body>
                  <cwmp:InformResponse xmlns:cwmp="urn:dslforum-org:cwmp-1-0">
                    <MaxEnvelopes>1</MaxEnvelopes>
                  </cwmp:InformResponse>
                </soapenv:Body>
              </soapenv:Envelope>
            ).withHeaders("SOAPAction" -> "")
          )
        case _ =>
          Future.successful(NotImplemented)
      }
    }).getOrElse {
      logger.warn("Got no payload and no method, assuming empty")
      Future.successful(Ok)
    }
  }
}
