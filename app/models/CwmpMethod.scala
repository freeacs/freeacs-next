package models
import scala.xml.Node

sealed abstract class CwmpMethod(val name: String) { self =>
  lazy val abbr: String = self.getClass.getSimpleName.stripSuffix("$")
}

object CwmpMethod {
  case object EM   extends CwmpMethod("")
  case object IN   extends CwmpMethod("Inform")
  case object INr  extends CwmpMethod("InformResponse")
  case object ATC  extends CwmpMethod("AutonomousTransferComplete")
  case object TC   extends CwmpMethod("TransferComplete")
  case object GPV  extends CwmpMethod("GetParameterValues")
  case object GPVr extends CwmpMethod("GetParameterValuesResponse")
  case object SPV  extends CwmpMethod("SetParameterValues")
  case object SPVr extends CwmpMethod("SetParameterValuesResponse")
  case object GPN  extends CwmpMethod("GetParameterNames")
  case object GPNr extends CwmpMethod("GetParameterNamesResponse")
  case object RE   extends CwmpMethod("Reboot")
  case object REr  extends CwmpMethod("RebootResponse")
  case object FR   extends CwmpMethod("FactoryReset")
  case object FRr  extends CwmpMethod("FactoryResetResponse")
  case object GM   extends CwmpMethod("GetRPCMethods")
  case object GMr  extends CwmpMethod("GetRPCMethodsResponse")
  case object DO   extends CwmpMethod("Download")
  case object DOr  extends CwmpMethod("DownloadResponse")

  val values = Seq(IN, INr, ATC, TC, GPV, GPVr, GPN, GPNr, SPV, SPVr, RE, REr, FR, FRr, GM, GMr, DO, DOr)

  def fromName(name: String): Option[CwmpMethod] =
    values.find(_.name == name)

  def fromNode(xml: Node): Option[CwmpMethod] =
    (for {
      body       <- xml \ "Body"
      firstElem  <- body.child.find(child => !child.isAtom) // filter out text nodes
      methodName <- fromName(firstElem.label)
    } yield methodName).headOption
}
