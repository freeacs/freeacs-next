package models
import scala.xml.Node

case class ParameterInfoStruct(name: String, writable: Boolean)

object ParameterInfoStruct {
  def fromNode(node: Node): Seq[ParameterInfoStruct] =
    for {
      paramNode <- node \\ "ParameterInfoStruct"
      name      <- (paramNode \\ "Name").map(_.text)
      writable <- (paramNode \\ "Writable")
                   .map(_.text)
                   .map {
                     case "1" => true
                     case "0" => false
                   }
                   .headOption
    } yield
      ParameterInfoStruct(
        name = name,
        writable = writable
      )
}
