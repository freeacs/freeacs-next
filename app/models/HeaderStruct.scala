package models
import scala.xml.Node

case class HeaderStruct(id: String, holdRequests: Boolean, noMoreRequests: Boolean)

object HeaderStruct {
  import util.XmlHelper._

  def fromNode(node: Node): Option[HeaderStruct] =
    (for {
      headerNode     <- node \\ "Header"
      id             <- (headerNode \\ "ID").map(_.text)
      holdRequests   <- getBoolean(headerNode, "HoldRequests")
      noMoreRequests <- getBoolean(headerNode, "NoMoreRequests")
    } yield HeaderStruct(id, holdRequests, noMoreRequests)).headOption
}
