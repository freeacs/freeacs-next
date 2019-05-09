package controllers

import freeacs.dbi.ProvisioningProtocol
import io.kanaka.monadic.dsl._
import play.api.Logging
import play.api.data.format.Formatter
import play.api.data.{FormError, Forms}
import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, ControllerComponents}
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
      _    <- createUnitType(data) ?| (error => InternalServerError(unitTypeCreate(form.fill(data), Some(error))))
    } yield Redirect(CreateUnitType.url).flashing("success" -> s"The Unit Type ${data.name} was created")
  }

  private def createUnitType(data: UnitType) =
    unitTypeService.create(data.name, data.vendor, data.description, data.protocol)

  def overview = Action.async {
    for {
      unitTypeList <- unitTypeService.list ?| (e => InternalServerError(e))
    } yield Ok(unitTypeOverview(unitTypeList.toList))
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
