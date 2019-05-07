package controllers
import models.Method
import play.api.Logging
import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, ControllerComponents, Request}
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
    request.body.asXml
      .flatMap(_.headOption)
      .flatMap(Method.fromNode)
      .map(method => Future.successful(Ok(method.abbr)))
      .getOrElse(Future.successful(Ok))
  }
}
