package models
import scala.xml.Node

case class ParameterInfoStruct(name: String, writable: Boolean)

object ParameterInfoStruct {
  import util.XmlHelper._

  def fromNode(node: Node): Seq[ParameterInfoStruct] =
    for {
      paramNode <- node \\ "ParameterInfoStruct"
      name      <- (paramNode \\ "Name").map(_.text)
      writable  <- getBoolean(paramNode, "Writable")
    } yield ParameterInfoStruct(name, writable)
}
