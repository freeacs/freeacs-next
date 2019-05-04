package controllers

import dbi.DBIHolder
import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

@Singleton
class HealthController @Inject()(cc: ControllerComponents, dbiHolder: DBIHolder) extends AbstractController(cc) {

  def ok: Action[AnyContent] = Action {
    Option(dbiHolder.dbi.getDbiThrowable)
        .map(throwable => InternalServerError("ERROR: DBI reported error:\n" + throwable + "\n" +
          throwable.getStackTrace.map(_.toString).mkString("\n")))
        .getOrElse(Ok("FREEACSOK"))
  }
}
