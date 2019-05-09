package controllers

import freeacs.dbi.{Profile, Unittype}
import io.kanaka.monadic.dsl._
import play.api.Logging
import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, ControllerComponents}
import services.{ProfileService, UnitTypeService}
import views.CreateProfile
import views.html.templates.{profileCreate, profileOverview}

import scala.concurrent.ExecutionContext

class ProfileController(
    cc: ControllerComponents,
    profileService: ProfileService,
    unitTypeService: UnitTypeService
)(
    implicit ec: ExecutionContext
) extends AbstractController(cc)
    with I18nSupport
    with Logging {

  import ProfileForm.form

  def viewCreate = Action.async { implicit request =>
    for {
      unitTypeList <- unitTypeService.list
    } yield Ok(profileCreate(form, unitTypeList.toList))
  }

  def postCreate = Action.async { implicit request =>
    for {
      unitTypeList <- unitTypeService.list
      form         <- form.bindFromRequest() ?| (form => BadRequest(profileCreate(form, unitTypeList.toList)))
      _            <- profileService.create(new Profile(form.name, new Unittype(form.unitTypeId.toInt, null, null, null, null))) ?| InternalServerError
    } yield Redirect(CreateProfile.url).flashing("success" -> s"The Profile ${form.name} was created")
  }

  def overview = Action.async {
    for {
      profileList <- profileService.list
    } yield Ok(profileOverview(profileList.toList))
  }
}

object ProfileForm {
  import play.api.data.Form
  import play.api.data.Forms._

  case class Profile(
      id: Option[Long] = None,
      name: String,
      unitTypeId: Long
  )

  val form = Form(
    mapping(
      "id"         -> optional(longNumber),
      "name"       -> nonEmptyText(minLength = 3),
      "unitTypeId" -> longNumber
    )(Profile.apply)(Profile.unapply)
  )
}
