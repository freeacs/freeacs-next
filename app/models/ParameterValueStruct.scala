package models
import scala.xml.Node

case class ParameterValueStruct(name: String, value: String, `type`: String) {
  lazy val string = `type` == "xsd:string"
}

object ParameterValueStruct {
  def fromNode(node: Seq[Node]): Seq[ParameterValueStruct] =
    (node \\ "ParameterValueStruct").flatMap { pvs =>
      (pvs \\ "Name").headOption.map(
        name => {
          ParameterValueStruct(
            name.text,
            (pvs \\ "Value").headOption.map(_.text).getOrElse(""),
            (pvs \\ "Value").headOption.map(_.attributes.asAttrMap("xsi:type")).getOrElse("")
          )
        }
      )
    }
}
