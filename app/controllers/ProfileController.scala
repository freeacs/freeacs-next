package controllers

import com.google.inject.Inject
import play.api.Logging
import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import services.{ProfileService, UnitTypeService}
import views.CreateProfile
import views.html.templates.{profileCreate, profileDetails, profileOverview}

import scala.concurrent.{ExecutionContext, Future}

class ProfileController @Inject()(
    cc: ControllerComponents,
    profileService: ProfileService,
    unitTypeService: UnitTypeService
)(
    implicit ec: ExecutionContext
) extends AbstractController(cc)
    with I18nSupport
    with Logging {

  import ProfileForm.form

  def viewProfile(id: Int): Action[AnyContent] = Action.async {
    profileService.list.map {
      case Right(profileList) if profileList.exists(_.id == id) =>
        Ok(profileDetails(profileList.find(_.id == id).head))
      case Left(error) => InternalServerError(error)
    }
  }

  def viewCreate: Action[AnyContent] = Action.async { implicit request =>
    unitTypeService.list.map {
      case Right(unitTypeList) => Ok(profileCreate(form, unitTypeList))
      case Left(error)         => InternalServerError(error)
    }
  }

  def postCreate: Action[AnyContent] = Action.async { implicit request =>
    unitTypeService.list.flatMap {
      case Right(unitTypeList) =>
        form.bindFromRequest.fold(
          formWithErrors => Future.successful(BadRequest(profileCreate(formWithErrors, unitTypeList.toList))),
          profileData =>
            profileService.createOrFail(profileData.name, profileData.unitTypeId).map {
              case Right(_) =>
                Redirect(CreateProfile.url)
                  .flashing("success" -> s"The Profile ${profileData.name} was created")
              case Left(error) =>
                InternalServerError(profileCreate(form.fill(profileData), unitTypeList, Some(error)))
          }
        )
      case Left(error) =>
        Future.successful(InternalServerError(error))
    }
  }

  def overview: Action[AnyContent] = Action.async {
    profileService.list.map {
      case Right(profileList) => Ok(profileOverview(profileList))
      case Left(error)        => InternalServerError(error)
    }
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
