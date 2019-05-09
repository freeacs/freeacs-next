package controllers

import freeacs.dbi.Unittype
import io.kanaka.monadic.dsl._
import play.api.Logging
import play.api.i18n.{I18nSupport, MessagesProvider}
import play.api.mvc.{AbstractController, ControllerComponents, Flash}
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
      unitTypeList <- unitTypeService.list ?| (e => InternalServerError(e))
      form         <- form.bindFromRequest() ?| (form => BadRequest(profileCreate(form, unitTypeList.toList)))
      _            <- profileService.create(form.name, form.unitTypeId) ?| (e => failed(form, unitTypeList, e))
    } yield Redirect(CreateProfile.url).flashing("success" -> s"The Profile ${form.name} was created")
  }

  private def failed(formData: ProfileForm.Profile, unitTypeList: Seq[Unittype], error: String)(
      implicit messagesProvider: MessagesProvider,
      flash: Flash
  ) = InternalServerError(profileCreate(form.fill(formData), unitTypeList, Some(error)))

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
