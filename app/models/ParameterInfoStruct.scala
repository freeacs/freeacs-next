package models
import scala.xml.Node

case class ParameterInfoStruct(name: String, writable: Boolean)

object ParameterInfoStruct {
  def fromNode(node: Node): Seq[ParameterInfoStruct] =
    (node \\ "ParameterInfoStruct").flatMap { infoStruct =>
      (infoStruct \\ "Name").headOption.map(
        name => {
          ParameterInfoStruct(
            name.text,
            (infoStruct \\ "Writable")
              .map(_.text)
              .map {
                case "1" => true
                case "0" => false
              }
              .headOption
              .getOrElse(false)
          )
        }
      )
    }
}
