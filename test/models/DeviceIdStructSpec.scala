package models
import models.DeviceIdStruct._
import org.scalatestplus.play.PlaySpec

class DeviceIdStructSpec extends PlaySpec {
  "A DeviceIdStruct" should {
    "be able to parse device info" in new CwmpContext() {
      val xml        = mkIN()
      val deviceInfo = DeviceIdStruct.fromNode(xml)
      val expected = DeviceIdStruct(
        Manufacturer("FakeManufacturer"),
        OUI("000000"),
        ProductClass("FakeProductClass"),
        SerialNumber("FakeSerialNumber")
      )
      deviceInfo mustBe Some(expected)
    }

    "be producing a valid unitId with no product class" in {
      val expected = DeviceIdStruct(
        Manufacturer("FakeManufacturer"),
        OUI("000000"),
        ProductClass(""),
        SerialNumber("FakeSerialNumber")
      )
      expected.unitId mustBe "000000-FakeSerialNumber"
    }

    "be producing a valid unitId with product class" in {
      val expected = DeviceIdStruct(
        Manufacturer("FakeManufacturer"),
        OUI("000000"),
        ProductClass("ProductClass"),
        SerialNumber("FakeSerialNumber")
      )
      expected.unitId mustBe "000000-ProductClass-FakeSerialNumber"
    }
  }
}
