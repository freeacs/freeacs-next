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
      s"$oui-$productClass-$serialNumber"
    else
      s"$oui-$serialNumber"
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

  class Manufacturer(val underlying: String) extends AnyVal {
    override def toString: String = underlying
  }
  object Manufacturer {
    def apply(v: String): Manufacturer = new Manufacturer(v)
  }

  class OUI(val underlying: String) extends AnyVal {
    override def toString: String = underlying
  }
  object OUI {
    def apply(v: String): OUI = new OUI(v)
  }

  class ProductClass(val underlying: String) extends AnyVal {
    override def toString: String = underlying
  }
  object ProductClass {
    import util.UnsafeCharFilter._
    def apply(productClass: String): ProductClass =
      new ProductClass(filterUnsafeChars(productClass))
  }

  class SerialNumber(val underlying: String) extends AnyVal {
    override def toString: String = underlying
  }
  object SerialNumber {
    def apply(v: String): SerialNumber = new SerialNumber(v)
  }
}
