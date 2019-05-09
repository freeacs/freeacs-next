package controllers

import freeacs.dbi.ProvisioningProtocol
import io.kanaka.monadic.dsl._
import play.api.Logging
import play.api.data.format.Formatter
import play.api.data.{FormError, Forms}
import play.api.i18n.{I18nSupport, MessagesProvider}
import play.api.mvc.{AbstractController, ControllerComponents, Flash}
import services.UnitTypeService
import views.CreateUnitType
import views.html.templates.unitTypeCreate

import scala.concurrent.ExecutionContext

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
    for {
      form <- form.bindFromRequest() ?| (form => BadRequest(unitTypeCreate(form)))
      _    <- unitTypeService.create(form.name, form.vendor, form.description, form.protocol) ?| (e => failed(form, e))
    } yield Redirect(CreateUnitType.url).flashing("success" -> s"The Unit Type ${form.name} was created")
  }

  private def failed(formData: UnitTypeForm.UnitType, e: Throwable)(implicit messagesProvider: MessagesProvider, flash: Flash) =
    InternalServerError(unitTypeCreate(form.fill(formData)))
      .flashing("failure" -> s"Failed to create Unit Type ${formData.name}: ${e.getMessage}")

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
