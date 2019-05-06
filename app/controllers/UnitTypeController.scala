package controllers

import freeacs.dbi.{ProvisioningProtocol, Unittype}
import play.api.Logging
import play.api.data.format.Formatter
import play.api.data.{FormError, Forms}
import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import services.UnitTypeService
import views.CreateUnitType

import scala.concurrent.{ExecutionContext, Future}

class UnitTypeController(cc: ControllerComponents, unitTypeService: UnitTypeService)(
    implicit ec: ExecutionContext
) extends AbstractController(cc)
    with I18nSupport
    with Logging {
  import UnitTypeForm._

  def viewCreate = Action { implicit request =>
    Ok(views.html.templates.unitTypeCreate(form))
  }

  def postCreate = Action.async { implicit request =>
    form.bindFromRequest.fold(
      formWithErrors => {
        Future.successful(BadRequest(views.html.templates.unitTypeCreate(formWithErrors)))
      },
      formData => {
        unitTypeService
          .create(
            new Unittype(
              formData.name,
              formData.vendor,
              formData.description,
              formData.protocol
            )
          )
          .map(
            _ =>
              Redirect(CreateUnitType.url).flashing(
                "success" -> s"The Unit Type ${formData.name} was created"
            )
          )
          .recover {
            case e =>
              logger.error("Failed to create Unit Type", e)
              InternalServerError(views.html.templates.unitTypeCreate(form)).flashing(
                "failure" -> s"Failed to create Unit Type ${formData.name}: ${e.getMessage}"
              )
          }
      }
    )
  }

  def overview = Action.async {
    unitTypeService.list
      .map(unitTypeList => {
        Ok(
          views.html.templates.unitTypeOverview(unitTypeList.toList)
        )
      })
      .recover {
        case exception: Exception =>
          InternalServerError(exception.getMessage)
      }
  }
}

object UnitTypeForm {
  import play.api.data.Form
  import play.api.data.Forms._

  case class UnitType(
      id: Option[Long] = None,
      name: String,
      vendor: String,
      description: String,
      protocol: ProvisioningProtocol
  )

  implicit def provisioningProtocolFormat: Formatter[ProvisioningProtocol] =
    new Formatter[ProvisioningProtocol] {
      override def bind(
          key: String,
          data: Map[String, String]
      ): Either[Seq[FormError], ProvisioningProtocol] =
        data.get(key).map(ProvisioningProtocol.valueOf).toRight(Seq(FormError(key, "error.required", Nil)))
      override def unbind(
          key: String,
          value: ProvisioningProtocol
      ): Map[String, String] =
        Map(key -> value.toString)
    }

  val form = Form(
    mapping(
      "id"          -> optional(longNumber),
      "name"        -> nonEmptyText(minLength = 3),
      "vendor"      -> text,
      "description" -> text,
      "protocol"    -> Forms.of[ProvisioningProtocol]
    )(UnitType.apply)(UnitType.unapply)
  )
}
