package controllers

import freeacs.dbi.DBIHolder
import play.api.mvc.{AbstractController, ControllerComponents}

class HealthController (cc: ControllerComponents, dbiHolder: DBIHolder) extends AbstractController(cc) {

  def ok = Action {
    Option(dbiHolder.dbi.getDbiThrowable)
        .map(throwable => InternalServerError("ERROR: DBI reported error:\n" + throwable + "\n" +
          throwable.getStackTrace.map(_.toString).mkString("\n")))
        .getOrElse(Ok("FREEACSOK"))
  }
}
