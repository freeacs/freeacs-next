package controllers

import play.api.Logging
import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, ControllerComponents}
import services.{ProfileService, UnitService, UnitTypeService}
import views.{CreateUnit, UnitDetails}
import views.html.templates.{unitCreate, unitDetails, unitOverview}

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

  def kickUnit(unitId: String) = Action.async {
    unitService.find(unitId).flatMap {
      case Some(unit) =>
        (for {
          _ <- unit.params.find(
                p => p.unitTypeParamName.endsWith("ConnectionRequestURL") && p.value.exists(!_.isBlank)
              )
          _ <- unit.params.find(
                p => p.unitTypeParamName.endsWith("ConnectionRequestUsername") && p.value.exists(!_.isBlank)
              )
          _ <- unit.params.find(
                p => p.unitTypeParamName.endsWith("ConnectionRequestPassword") && p.value.exists(!_.isBlank)
              )
        } yield Future.successful(Redirect(UnitDetails.url + "/" + unitId))) match {
          case Some(f) => f
          case _       => Future.successful(BadRequest("Required params was not found"))
        }
      case None =>
        Future.successful(BadRequest("Unit was not found"))
    }
  }

  def viewUnit(unitId: String) = Action.async {
    unitService.find(unitId).map {
      case Some(unit) =>
        Ok(unitDetails(unit))
      case None =>
        BadRequest
    }
  }

  def viewCreate = Action.async { implicit request =>
    unitTypeService.list.flatMap {
      case Right(unitTypeList) =>
        profileService.list.map {
          case Right(profileList) =>
            Ok(unitCreate(form, unitTypeList.toList, profileList.toList))
          case Left(error) =>
            InternalServerError(error)
        }
      case Left(error) =>
        Future.successful(InternalServerError(error))
    }
  }

  def postCreate = Action.async { implicit request =>
    unitTypeService.list.flatMap {
      case Right(unitTypeList) =>
        profileService.list.flatMap {
          case Right(profileList) =>
            form.bindFromRequest.fold(
              formWithErrors =>
                Future.successful(BadRequest(unitCreate(formWithErrors, unitTypeList, profileList))),
              unitData =>
                unitService.creatOrFail(unitData.unitId, unitData.unitTypeId, unitData.profileId).map {
                  case Right(_) =>
                    Redirect(CreateUnit.url).flashing("success" -> s"The Unit ${unitData.unitId} was created")
                  case Left(error) =>
                    InternalServerError(
                      unitCreate(form.fill(unitData), unitTypeList, profileList, Some(error))
                    )
              }
            )
          case Left(error) =>
            Future.successful(InternalServerError(error))
        }
      case Left(error) =>
        Future.successful(InternalServerError(error))
    }
  }

  def overview = Action.async {
    unitService.list.map {
      case Right(unitList) =>
        Ok(unitOverview(unitList.toList))
      case Left(error) =>
        InternalServerError(error)
    }
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
