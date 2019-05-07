package models
import scala.xml.Node

sealed abstract class Method(val name: String) { self =>
  lazy val abbr: String = self.getClass.getSimpleName.stripSuffix("$")
}

object Method {
  case object IN   extends Method("Inform")
  case object INr  extends Method("InformResponse")
  case object ATC  extends Method("AutonomousTransferComplete")
  case object TC   extends Method("TransferComplete")
  case object GPV  extends Method("GetParameterValues")
  case object GPVr extends Method("GetParameterValuesResponse")
  case object SPV  extends Method("SetParameterValues")
  case object SPVr extends Method("SetParameterValuesResponse")
  case object GPN  extends Method("GetParameterNames")
  case object GPNr extends Method("GetParameterNamesResponse")
  case object RE   extends Method("Reboot")
  case object REr  extends Method("RebootResponse")
  case object FR   extends Method("FactoryReset")
  case object FRr  extends Method("FactoryResetResponse")
  case object GM   extends Method("GetRPCMethods")
  case object GMr  extends Method("GetRPCMethodsResponse")
  case object DO   extends Method("Download")
  case object DOr  extends Method("DownloadResponse")

  val values = Seq(IN, INr, ATC, TC, GPV, GPVr, GPN, GPNr, SPV, SPVr, RE, REr, FR, FRr, GM, GMr, DO, DOr)

  def fromName(name: String): Option[Method] =
    values.find(_.name == name)

  def fromNode(xml: Node): Option[Method] =
    (for {
      body       <- xml \ "Body"
      firstElem  <- body.child.find(child => !child.isAtom) // filter out text nodes
      methodName <- fromName(firstElem.label)
    } yield methodName).headOption
}
