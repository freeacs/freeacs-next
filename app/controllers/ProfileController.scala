package controllers

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
      unitTypeList <- unitTypeService.list ?| (e => InternalServerError(e))
    } yield Ok(profileCreate(form, unitTypeList.toList))
  }

  def postCreate = Action.async { implicit request =>
    for {
      unitTypeList <- unitTypeService.list ?| (error => InternalServerError(error))
      data         <- form.bindFromRequest() ?| (form => BadRequest(profileCreate(form, unitTypeList.toList)))
      _            <- createProfile(data) ?| (error => InternalServerError(profileCreate(form.fill(data), unitTypeList, Some(error))))
    } yield Redirect(CreateProfile.url).flashing("success" -> s"The Profile ${data.name} was created")
  }

  private def createProfile(data: ProfileForm.Profile) =
    profileService.create(data.name, data.unitTypeId)

  def overview = Action.async {
    for {
      profileList <- profileService.list ?| (e => InternalServerError(e))
    } yield Ok(profileOverview(profileList.toList))
  }
}

object ProfileForm {
  import play.api.data.Form
  import play.api.data.Forms._

  case class Profile(
      id: Option[Int] = None,
      name: String,
      unitTypeId: Int
  )

  val form = Form(
    mapping(
      "id"         -> optional(number),
      "name"       -> nonEmptyText(minLength = 3),
      "unitTypeId" -> number
    )(Profile.apply)(Profile.unapply)
  )
}
