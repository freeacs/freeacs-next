package controllers
import freeacs.dbi.Unittype
import play.api.Logging
import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, ControllerComponents}
import services.{ProfileService, UnitTypeService}
import views.CreateProfile

import scala.concurrent.{ExecutionContext, Future}

class ProfileController(
    cc: ControllerComponents,
    profileService: ProfileService,
    unitTypeService: UnitTypeService
)(
    implicit ec: ExecutionContext
) extends AbstractController(cc)
    with I18nSupport
    with Logging {

  import ProfileForm._

  def viewCreate = Action.async { implicit request =>
    for {
      unitTypeList <- unitTypeService.list
    } yield Ok(views.html.templates.profileCreate(form, unitTypeList.toList))
  }

  def postCreate = Action.async { implicit request =>
    unitTypeService.list.flatMap(unitTypeList => {
      form.bindFromRequest.fold(
        formWithErrors => {
          Future.successful(
            BadRequest(views.html.templates.profileCreate(formWithErrors, unitTypeList.toList))
          )
        },
        formData => {
          profileService
            .create(
              new freeacs.dbi.Profile(
                formData.name,
                new Unittype(formData.unitTypeId.toInt, null, null, null, null)
              )
            )
            .map(
              _ =>
                Redirect(CreateProfile.url).flashing(
                  "success" -> s"The Profile ${formData.name} was created"
              )
            )
            .recover {
              case e =>
                logger.error("Failed to create Profile", e)
                InternalServerError(views.html.templates.profileCreate(form, unitTypeList.toList)).flashing(
                  "failure" -> s"Failed to create Profile ${formData.name}: ${e.getMessage}"
                )
            }
        }
      )
    })
  }

  def overview = Action.async {
    profileService.list
      .map(profileList => {
        Ok(
          views.html.templates.profileOverview(profileList.toList)
        )
      })
      .recover {
        case exception: Exception =>
          InternalServerError(exception.getMessage)
      }
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
