package controllers

import io.kanaka.monadic.dsl._
import play.api.Logging
import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, ControllerComponents}
import services.{ProfileService, UnitService, UnitTypeService}
import views.CreateUnit
import views.html.templates.{unitCreate, unitOverview}

import scala.concurrent.ExecutionContext

class UnitController(
    cc: ControllerComponents,
    unitService: UnitService,
    profileService: ProfileService,
    unitTypeService: UnitTypeService
)(
    implicit ec: ExecutionContext
) extends AbstractController(cc)
    with I18nSupport
    with Logging {

  import UnitForm._

  def viewCreate = Action.async { implicit request =>
    for {
      unitTypeList <- unitTypeService.list
      profileList  <- profileService.list
    } yield Ok(unitCreate(form, unitTypeList.toList, profileList.toList))
  }

  def postCreate = Action.async { implicit request =>
    for {
      unitTypeList <- unitTypeService.list
      profileList  <- profileService.list
      form         <- form.bindFromRequest() ?| (form => BadRequest(unitCreate(form, unitTypeList.toList, profileList.toList)))
      _            <- unitService.create(form.unitId, form.unitTypeId.toInt, form.profileId.toInt) ?| InternalServerError
    } yield Redirect(CreateUnit.url).flashing("success" -> s"The Unit ${form.unitId} was created")
  }

  def overview = Action.async {
    for {
      unitTypeList <- unitService.list
    } yield Ok(unitOverview(unitTypeList.toList))
  }
}

object UnitForm {
  import play.api.data.Form
  import play.api.data.Forms._

  case class Unit(
      unitId: String,
      unitTypeId: Long,
      profileId: Long
  )

  val form = Form(
    mapping(
      "unitId"     -> nonEmptyText(minLength = 3),
      "unitTypeId" -> longNumber,
      "profileId"  -> longNumber
    )(Unit.apply)(Unit.unapply)
  )
}
