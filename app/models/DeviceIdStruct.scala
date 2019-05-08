package models
import scala.xml.Node

case class DeviceIdStruct(manufacturer: String, oui: String, productClass: String, serialNumber: String)

object DeviceIdStruct {
  import util.UnsafeCharFilter._

  def fromNode(node: Node): Option[DeviceIdStruct] =
    (for {
      deviceIdNode <- node \\ "DeviceId"
      manufacturer <- deviceIdNode \\ "Manufacturer"
      oui          <- deviceIdNode \\ "OUI"
      productClass <- deviceIdNode \\ "ProductClass"
      serialNumber <- deviceIdNode \\ "SerialNumber"
    } yield
      DeviceIdStruct(
        manufacturer = manufacturer.text,
        oui = oui.text,
        productClass = filterUnsafeChars(productClass.text),
        serialNumber = serialNumber.text
      )).headOption
}
