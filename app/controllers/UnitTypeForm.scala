package controllers

import freeacs.dbi.ProvisioningProtocol
import play.api.data.format.Formatter
import play.api.data.{FormError, Forms}

object  UnitTypeForm {
  import play.api.data.Form
  import play.api.data.Forms._

  case class UnitType(
    id: Option[Long] = None,
    name: String,
    vendor: String,
    description: String,
    protocol: ProvisioningProtocol
  )

  implicit def provisioningProtocolFormat: Formatter[ProvisioningProtocol] = new Formatter[ProvisioningProtocol] {
    override def bind(key: String, data: Map[String, String]): Either[Seq[FormError], ProvisioningProtocol] =
      data.get(key)
        .map(ProvisioningProtocol.valueOf)
        .toRight(Seq(FormError(key, "error.required", Nil)))
    override def unbind(key: String, value: ProvisioningProtocol): Map[String, String] =
      Map(key -> value.toString)
  }

  val form = Form(
    mapping(
      "id" -> optional(longNumber),
      "name" -> nonEmptyText,
      "vendor" -> text,
      "description" -> text,
      "protocol" -> Forms.of[ProvisioningProtocol]
    )(UnitType.apply)(UnitType.unapply)
  )
}
