package controllers

import com.google.inject.Inject
import play.api.mvc._
import services.UnitService

import scala.concurrent.ExecutionContext

class DashboardController @Inject()(cc: ControllerComponents, unitService: UnitService)(
    implicit ec: ExecutionContext
) extends AbstractController(cc) {

  def index: Action[AnyContent] = Action.async {
    unitService.count.map(unitCount => Ok(views.html.templates.dashboard(unitCount)))
  }

}
