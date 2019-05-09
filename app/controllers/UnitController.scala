package controllers

import freeacs.dbi.{Profile, Unittype}
import io.kanaka.monadic.dsl._
import play.api.Logging
import play.api.i18n.{I18nSupport, MessagesProvider}
import play.api.mvc.{AbstractController, ControllerComponents, Flash}
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
      unitTypeList <- unitTypeService.list ?| (e => InternalServerError(e))
      profileList  <- profileService.list ?| (e => InternalServerError(e))
      form         <- form.bindFromRequest() ?| (form => BadRequest(unitCreate(form, unitTypeList, profileList)))
      _            <- unitService.create(form.unitId, form.unitTypeId, form.profileId) ?| (e => failed(form, unitTypeList, profileList, e))
    } yield Redirect(CreateUnit.url).flashing("success" -> s"The Unit ${form.unitId} was created")
  }

  private def failed(formData: UnitForm.Unit, unitTypeList: Seq[Unittype], profileList: Seq[Profile], error: String)(
      implicit messagesProvider: MessagesProvider,
      flash: Flash
  ) = InternalServerError(unitCreate(form.fill(formData), unitTypeList, profileList)).flashing("failure" -> error)

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
