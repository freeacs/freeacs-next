package controllers

import com.google.inject.Inject
import models.AcsProtocol
import play.api.Logging
import play.api.data.format.Formatter
import play.api.data.{FormError, Forms}
import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import services.UnitTypeService
import views.CreateUnitType
import views.html.templates.{unitTypeCreate, unitTypeOverview}

import scala.concurrent.{ExecutionContext, Future}

class UnitTypeController @Inject()(cc: ControllerComponents, unitTypeService: UnitTypeService)(
    implicit ec: ExecutionContext
) extends AbstractController(cc)
    with I18nSupport
    with Logging {
  import UnitTypeForm._

  def viewUnitType(name: String): Action[AnyContent] = Action.async {
    unitTypeService.find(name).map {
      case Some(unitType) =>
        Ok(views.html.templates.unitTypeDetails(unitType))
      case None =>
        BadRequest
    }
  }

  def viewCreate: Action[AnyContent] = Action { implicit request =>
    Ok(views.html.templates.unitTypeCreate(form))
  }

  def postCreate: Action[AnyContent] = Action.async { implicit request =>
    form.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(unitTypeCreate(formWithErrors))),
      unitTypeData =>
        unitTypeService
          .createOrFail(
            unitTypeData.name,
            unitTypeData.vendor,
            unitTypeData.description,
            unitTypeData.protocol
          )
          .map {
            case Right(_) =>
              Redirect(CreateUnitType.url)
                .flashing("success" -> s"The Unit Type ${unitTypeData.name} was created")
            case Left(error) =>
              InternalServerError(unitTypeCreate(form.fill(unitTypeData), Some(error)))
        }
    )
  }

  def overview: Action[AnyContent] = Action.async {
    unitTypeService.list.map {
      case Right(unitTypeList) =>
        Ok(unitTypeOverview(unitTypeList))
      case Left(error) =>
        InternalServerError(error)
    }
  }
}

object UnitTypeForm {
  import play.api.data.Form
  import play.api.data.Forms._

  case class UnitType(
      id: Option[Int] = None,
      name: String,
      vendor: String,
      description: String,
      protocol: AcsProtocol
  )

  implicit def provisioningProtocolFormat: Formatter[AcsProtocol] =
    new Formatter[AcsProtocol] {
      override def bind(
          key: String,
          data: Map[String, String]
      ): Either[Seq[FormError], AcsProtocol] =
        data.get(key).flatMap(AcsProtocol.fromString).toRight(Seq(FormError(key, "error.required", Nil)))
      override def unbind(
          key: String,
          value: AcsProtocol
      ): Map[String, String] =
        Map(key -> value.name)
    }

  val form = Form(
    mapping(
      "id"          -> optional(number),
      "name"        -> nonEmptyText(minLength = 3),
      "vendor"      -> text,
      "description" -> text,
      "protocol"    -> Forms.of[AcsProtocol]
    )(UnitType.apply)(UnitType.unapply)
  )
}
