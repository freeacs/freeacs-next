package models

sealed trait AcsProtocol { self =>
  lazy val name: String = self.getClass.getSimpleName.stripSuffix("$")
}

object AcsProtocol {
  case object TR069 extends AcsProtocol

  val values = Seq(TR069)

  def fromString(protocol: String): Option[AcsProtocol] =
    values.find(_.name == protocol)

  def unsafeFromString(protocol: String): AcsProtocol =
    fromString(protocol).get
}
