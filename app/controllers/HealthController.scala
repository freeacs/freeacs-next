package controllers

import play.api.mvc.{AbstractController, ControllerComponents}

class HealthController(cc: ControllerComponents) extends AbstractController(cc) {

  def ok = Action {
    Ok("FREEACSOK")
  }
}
