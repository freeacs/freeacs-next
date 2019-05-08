package models
import scala.xml.Node

case class ParameterValueStruct(name: String, value: Option[String], `type`: String) {
  lazy val string = `type` == "xsd:string"
}

object ParameterValueStruct {
  def fromNode(node: Seq[Node]): Seq[ParameterValueStruct] =
    for {
      paramNode <- node \\ "ParameterValueStruct"
      name      <- (paramNode \\ "Name").map(_.text)
    } yield
      ParameterValueStruct(
        name,
        (paramNode \\ "Value").map(_.text).headOption,
        (paramNode \\ "Value").map(_.attributes.asAttrMap("xsi:type")).headOption.getOrElse("")
      )
}
