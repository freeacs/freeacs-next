package controllers

import com.google.inject.Inject
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

class HealthController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def ok: Action[AnyContent] = Action {
    Ok("FREEACSOK")
  }
}
