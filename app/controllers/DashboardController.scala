package controllers

import scala.collection.JavaConverters._
import dbi.DBIHolder
import javax.inject._
import play.api.mvc._

@Singleton
class DashboardController @Inject()(cc: ControllerComponents, dbiHolder: DBIHolder) extends AbstractController(cc) {

  def index: Action[AnyContent] = Action {
    val count = dbiHolder.dbi.getACSUnit.getUnitCount(dbiHolder.dbi.getAcs.getUnittypes.getUnittypes.toList.asJava)
    Ok(views.html.dashboard(count))
  }

}
