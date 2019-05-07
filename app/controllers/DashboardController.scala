package controllers

import play.api.mvc._
import services.UnitService

import scala.concurrent.ExecutionContext

class DashboardController(cc: ControllerComponents, unitService: UnitService)(implicit ec: ExecutionContext)
    extends AbstractController(cc) {

  def index = Action.async {
    unitService.count.map(unitCount => Ok(views.html.templates.dashboard(unitCount)))
  }

}
