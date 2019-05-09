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
      unitTypeList <- unitTypeService.list ?| (e => InternalServerError(e))
      profileList  <- profileService.list ?| (e => InternalServerError(e))
    } yield Ok(unitCreate(form, unitTypeList.toList, profileList.toList))
  }

  def postCreate = Action.async { implicit request =>
    for {
      unitTypeList <- unitTypeService.list ?| (error => InternalServerError(error))
      profileList  <- profileService.list ?| (error => InternalServerError(error))
      data         <- form.bindFromRequest() ?| (form => BadRequest(unitCreate(form, unitTypeList, profileList)))
      _            <- createUnit(data) ?| (error => InternalServerError(unitCreate(form.fill(data), unitTypeList, profileList, Some(error))))
    } yield Redirect(CreateUnit.url).flashing("success" -> s"The Unit ${data.unitId} was created")
  }

  private def createUnit(data: Unit) =
    unitService.create(data.unitId, data.unitTypeId, data.profileId)

  def overview = Action.async {
    for {
      unitList <- unitService.list ?| (e => InternalServerError(e))
    } yield Ok(unitOverview(unitList.toList))
  }
}

object UnitForm {
  import play.api.data.Form
  import play.api.data.Forms._

  case class Unit(
      unitId: String,
      unitTypeId: Int,
      profileId: Int
  )

  val form = Form(
    mapping(
      "unitId"     -> nonEmptyText(minLength = 3),
      "unitTypeId" -> number,
      "profileId"  -> number
    )(Unit.apply)(Unit.unapply)
  )
}
