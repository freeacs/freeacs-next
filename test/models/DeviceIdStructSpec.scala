package models
import org.scalatestplus.play.PlaySpec

class DeviceIdStructSpec extends PlaySpec {
  "A DeviceIdStruct" should {
    "be able to parse device info" in new CwmpContext() {
      val xml        = mkIN()
      val deviceInfo = DeviceIdStruct.fromNode(xml)
      deviceInfo mustBe Some(
        DeviceIdStruct("FakeManufacturer", "000000", "FakeProductClass", "FakeSerialNumber")
      )
    }
  }
}
