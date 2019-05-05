package controllers

import dbi.{DBIHolder, Unittype}
import javax.inject.{Inject, Singleton}
import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

@Singleton
class UnitTypeController @Inject()(cc: ControllerComponents, dbiHolder: DBIHolder) extends AbstractController(cc) with I18nSupport {
  import UnitTypeForm._

  def viewCreate: Action[AnyContent] = Action { implicit request =>
    Ok(views.html.unitTypeCreate(form))
  }

  def postCreate: Action[AnyContent] = Action { implicit request =>
    form.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.unitTypeCreate(formWithErrors))
      },
      formData => {
        val newUnitType = new Unittype(formData.name, "", formData.description, formData.protocol)
        dbiHolder.dbi.getAcs.getUnittypes.addOrChangeUnittype(newUnitType, dbiHolder.dbi.getAcs)
        Redirect(routes.UnitTypeController.viewCreate()).flashing(
          "success" -> s"The Unit Type ${newUnitType.getName} was created"
        )
      }
    )
  }

  def overview: Action[AnyContent] = Action {
    Ok(views.html.unitTypeOverview(dbiHolder.dbi.getAcs.getUnittypes.getUnittypes.toList))
  }
}
