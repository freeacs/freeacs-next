package models

sealed trait AcsProtocol { self =>
  lazy val name: String = self.getClass.getSimpleName.stripSuffix("$")
}

object AcsProtocol {
  case object Tr069 extends AcsProtocol

  // Can throw match exception
  def unsafeFromString(protocol: String): AcsProtocol =
    protocol match {
      case Tr069.name => Tr069
    }
}
