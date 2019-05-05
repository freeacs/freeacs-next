package controllers

import freeacs.dbi.DBIHolder
import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, ControllerComponents}

@Singleton
class HealthController @Inject()(cc: ControllerComponents, dbiHolder: DBIHolder) extends AbstractController(cc) {

  def ok = Action {
    Option(dbiHolder.dbi.getDbiThrowable)
        .map(throwable => InternalServerError("ERROR: DBI reported error:\n" + throwable + "\n" +
          throwable.getStackTrace.map(_.toString).mkString("\n")))
        .getOrElse(Ok("FREEACSOK"))
  }
}
