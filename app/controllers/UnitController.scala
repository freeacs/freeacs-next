package controllers

import models.{AcsUnit, AcsUnitParameter}
import play.api.Logging
import play.api.data.Form
import play.api.i18n.I18nSupport
import play.api.libs.ws.{WSAuthScheme, WSClient}
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}
import services.{ProfileService, UnitService, UnitTypeService}
import views.{CreateUnit, UnitDetails}
import views.html.templates.{unitCreate, unitDetails, unitOverview}

import scala.concurrent.{ExecutionContext, Future}

class UnitController(
    cc: ControllerComponents,
    unitService: UnitService,
    profileService: ProfileService,
    unitTypeService: UnitTypeService,
    wsClient: WSClient
)(
    implicit ec: ExecutionContext
) extends AbstractController(cc)
    with I18nSupport
    with Logging {

  def addParam(unitId: String) = Action.async { implicit request =>
    upsertParam(unitId, "add")
  }

  def updateParam(unitId: String) = Action.async { implicit request =>
    upsertParam(unitId, "update")
  }

  private def upsertParam(unitId: String, tpe: String)(implicit request: Request[AnyContent]) = {
    import UpdateUnitParamForm._
    form.bindFromRequest.fold(
      err => {
        println(err.toString)
        Future.successful(
          Redirect(s"${UnitDetails.url}/$unitId")
            .flashing("error" -> s"Failed to $tpe param: ${err.errors.toString()}")
        )
      },
      data =>
        unitService
          .upsertParameters(Seq(AcsUnitParameter(unitId, data.unitTypeParamId, null, Some(data.value))))
          .map(
            _ =>
              Redirect(s"${UnitDetails.url}/$unitId").flashing(
                "success" -> s"Have ${tpe.stripSuffix("e")}ed param ${data.unitTypeParamId} with value ${data.value}"
            )
        )
    )
  }

  def kickUnit(unitId: String) = Action.async {
    unitService.find(unitId).flatMap {
      case Some(unit) =>
        (for {
          urlValue      <- getParam(unit, "ConnectionRequestURL")
          usernameValue <- getParam(unit, "ConnectionRequestUsername")
          passwordValue <- getParam(unit, "ConnectionRequestPassword")
        } yield {
          wsClient
            .url(urlValue)
            .withAuth(usernameValue, passwordValue, WSAuthScheme.BASIC)
            .stream()
            .map(_ => Ok(s"Kicked unit $unitId"))
            .recoverWith {
              case e: Exception =>
                Future.successful(InternalServerError(e.getLocalizedMessage))
            }
        }) match {
          case Some(f) => f
          case _       => Future.successful(BadRequest("Required params was not found"))
        }
      case None =>
        Future.successful(BadRequest("Unit was not found"))
    }
  }

  private def getParam(unit: AcsUnit, name: String): Option[String] =
    unit.params
      .find(
        p =>
          p.unitTypeParamName.endsWith(name)
            && p.value.exists(!_.isEmpty)
      )
      .flatMap(_.value)

  def viewUnit(unitId: String) = Action.async { implicit request =>
    import UpdateUnitParamForm._
    unitService.find(unitId).map {
      case Some(unit) =>
        Ok(unitDetails(unit, form))
      case None =>
        BadRequest
    }
  }

  def viewCreate = Action.async { implicit request =>
    import UnitForm._
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
    import UnitForm._
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

object UpdateUnitParamForm {
  import play.api.data.Form
  import play.api.data.Forms._

  case class UpdateUnitParam(
      unitTypeParamId: Int,
      value: String
  )

  val form = Form(
    mapping(
      "unitTypeParamId" -> number,
      "value"           -> nonEmptyText
    )(UpdateUnitParam.apply)(UpdateUnitParam.unapply)
  )
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
