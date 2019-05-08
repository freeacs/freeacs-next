package models
import scala.xml.Node

case class ParameterValueStruct(name: String, value: String, `type`: String) {
  lazy val string = `type` == "xsd:string"
}

object ParameterValueStruct {
  def fromNode(node: Seq[Node]): Seq[ParameterValueStruct] =
    for {
      paramNode <- node \\ "ParameterValueStruct"
      nameNode  <- paramNode \\ "Name"
      valueNode <- paramNode \\ "Value"
    } yield
      ParameterValueStruct(
        nameNode.text,
        valueNode.text,
        valueNode.attributes.asAttrMap.getOrElse("xsi:type", "")
      )
}
