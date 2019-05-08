package models
import scala.xml.Node

case class HeaderStruct(id: String, holdRequests: Boolean, noMoreRequests: Boolean)

object HeaderStruct {
  def fromNode(node: Node): Option[HeaderStruct] =
    (for {
      headerNode <- node \\ "Header"
      id         <- (headerNode \\ "ID").map(_.text)
      holdRequests <- (headerNode \\ "HoldRequests").map(_.text).headOption.orElse(Some("0")).map {
                       case "1" => true
                       case "0" => false
                     }
      noMoreRequests <- (headerNode \\ "NoMoreRequests").map(_.text).headOption.orElse(Some("0")).map {
                         case "1" => true
                         case "0" => false
                       }
    } yield HeaderStruct(id, holdRequests, noMoreRequests)).headOption
}
