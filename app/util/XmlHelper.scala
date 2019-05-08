package util
import scala.xml.Node

object XmlHelper {
  def getBoolean(headerNode: Node, key: String): Option[Boolean] =
    (headerNode \\ key).map(_.text).headOption.orElse(Some("0")).map {
      case "1" => true
      case "0" => false
    }
}
