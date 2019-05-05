package controllers

import freeacs.dbi.DBIHolder
import javax.inject._
import play.api.mvc._

import scala.collection.JavaConverters._

@Singleton
class DashboardController @Inject()(cc: ControllerComponents, dbiHolder: DBIHolder) extends AbstractController(cc) {

  def index = Action {
    val dbi = dbiHolder.dbi
    val unitTypeList = dbi.getAcs.getUnittypes.getUnittypes.toList.asJava
    val unitCount = dbi.getACSUnit.getUnitCount(unitTypeList)
    Ok(views.html.templates.dashboard(unitCount))
  }

}
