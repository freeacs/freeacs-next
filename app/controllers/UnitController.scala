package controllers

import play.api.Logging
import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, ControllerComponents}
import services.{ProfileService, UnitService, UnitTypeService}
import views.CreateUnit

import scala.concurrent.{ExecutionContext, Future}

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
    } yield Ok(views.html.templates.unitCreate(form, unitTypeList.toList, profileList.toList))
  }

  def postCreate = Action.async { implicit request =>
    // Should use for yield here, but two flatMaps will have to suffice for now
    unitTypeService.list.flatMap(unitTypeList => {
      profileService.list.flatMap(profileList => {
        form.bindFromRequest.fold(
          formWithErrors => {
            Future.successful(
              BadRequest(
                views.html.templates.unitCreate(formWithErrors, unitTypeList.toList, profileList.toList)
              )
            )
          },
          formData => {
            unitService
              .create(formData.unitId, formData.unitTypeId.toInt, formData.profileId.toInt)
              .map(
                _ =>
                  Redirect(CreateUnit.url).flashing(
                    "success" -> s"The Unit ${formData.unitId} was created"
                )
              )
              .recover {
                case e =>
                  logger.error("Failed to create Unit", e)
                  InternalServerError(
                    views.html.templates.unitCreate(form, unitTypeList.toList, profileList.toList)
                  ).flashing(
                    "failure" -> s"Failed to create Unit ${formData.unitId}: ${e.getMessage}"
                  )
              }
          }
        )
      })
    })
  }

  def overview = Action.async {
    unitService.list
      .map(unitList => {
        Ok(
          views.html.templates.unitOverview(unitList.toList)
        )
      })
      .recover {
        case exception: Exception =>
          InternalServerError(exception.getMessage)
      }
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
