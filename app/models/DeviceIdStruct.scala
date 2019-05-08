package models
import scala.xml.Node
import DeviceIdStruct._

case class DeviceIdStruct(
    manufacturer: Manufacturer,
    oui: OUI,
    productClass: ProductClass,
    serialNumber: SerialNumber
) {
  lazy val unitId =
    if (!productClass.underlying.isEmpty)
      s"${oui.underlying}-${productClass.underlying}-${serialNumber.underlying}"
    else s"${oui.underlying}-${serialNumber.underlying}"
}

object DeviceIdStruct {

  def fromNode(node: Node): Option[DeviceIdStruct] =
    (for {
      deviceIdNode <- node \\ "DeviceId"
      manufacturer <- deviceIdNode \\ "Manufacturer"
      oui          <- deviceIdNode \\ "OUI"
      productClass <- deviceIdNode \\ "ProductClass"
      serialNumber <- deviceIdNode \\ "SerialNumber"
    } yield
      DeviceIdStruct(
        Manufacturer(manufacturer.text),
        OUI(oui.text),
        ProductClass(productClass.text),
        SerialNumber(serialNumber.text)
      )).headOption

  class Manufacturer(val underlying: String) extends AnyVal
  object Manufacturer {
    def apply(v: String): Manufacturer = new Manufacturer(v)
  }

  class OUI(val underlying: String) extends AnyVal
  object OUI {
    def apply(v: String): OUI = new OUI(v)
  }

  class ProductClass(val underlying: String) extends AnyVal
  object ProductClass {
    import util.UnsafeCharFilter._
    def apply(productClass: String): ProductClass =
      new ProductClass(filterUnsafeChars(productClass))
  }

  class SerialNumber(val underlying: String) extends AnyVal
  object SerialNumber {
    def apply(v: String): SerialNumber = new SerialNumber(v)
  }
}
