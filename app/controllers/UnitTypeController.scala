package controllers

import freeacs.dbi.{DBIHolder, Unittype}
import play.api.Logging
import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

class UnitTypeController (cc: ControllerComponents, dbiHolder: DBIHolder) extends AbstractController(cc) with I18nSupport with Logging {
  import UnitTypeForm._

  def viewCreate: Action[AnyContent] = Action { implicit request =>
    Ok(views.html.templates.unitTypeCreate(form))
  }

  def postCreate: Action[AnyContent] = Action { implicit request =>
    form.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.templates.unitTypeCreate(formWithErrors))
      },
      formData => {
        logger.info(formData.toString)
        val newUnitType = new Unittype(formData.name, "", formData.description, formData.protocol)
        dbiHolder.dbi.getAcs.getUnittypes.addOrChangeUnittype(newUnitType, dbiHolder.dbi.getAcs)
        Redirect("/unittype/create").flashing(
          "success" -> s"The Unit Type ${newUnitType.getName} was created"
        )
      }
    )
  }

  def overview: Action[AnyContent] = Action {
    Ok(views.html.templates.unitTypeOverview(dbiHolder.dbi.getAcs.getUnittypes.getUnittypes.toList))
  }
}
