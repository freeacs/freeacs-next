package controllers

import io.kanaka.monadic.dsl._
import models.AcsProtocol
import play.api.Logging
import play.api.data.format.Formatter
import play.api.data.{FormError, Forms}
import play.api.i18n.{I18nSupport, MessagesProvider}
import play.api.mvc.{AbstractController, ControllerComponents, Flash, Result}
import services.UnitTypeService
import views.CreateUnitType
import views.html.templates.{unitTypeCreate, unitTypeOverview}

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
      data <- form.bindFromRequest() ?| (form => BadRequest(unitTypeCreate(form)))
      _    <- createUnitType(data) ?| failedToCreate(data)
    } yield Redirect(CreateUnitType.url).flashing("success" -> s"The Unit Type ${data.name} was created")
  }

  private def createUnitType(data: UnitTypeForm.UnitType) =
    unitTypeService.createOrFail(data.name, data.vendor, data.description, data.protocol)

  private def failedToCreate(
      data: UnitType
  )(implicit messagesProvider: MessagesProvider, flash: Flash): String => Result =
    error => InternalServerError(unitTypeCreate(form.fill(data), Some(error)))

  def overview = Action.async {
    for {
      unitTypeList <- unitTypeService.list ?| (e => InternalServerError(e))
    } yield Ok(unitTypeOverview(unitTypeList))
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
