package controllers

import dbi.Unittype.ProvisioningProtocol
import play.api.data.{FormError, Forms}
import play.api.data.format.Formatter

object UnitTypeForm {
  import play.api.data.Forms._
  import play.api.data.Form

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
      "vendor" -> text,
      "name" -> nonEmptyText,
      "description" -> text,
      "protocol" -> Forms.of[ProvisioningProtocol]
    )(UnitType.apply)(UnitType.unapply)
  )
}
