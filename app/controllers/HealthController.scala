package controllers

import freeacs.dbi.DBIHolder
import play.api.mvc.{AbstractController, ControllerComponents}

class HealthController(cc: ControllerComponents, dbiHolder: DBIHolder) extends AbstractController(cc) {

  def ok = Action {
    Ok("FREEACSOK")
  }
}
