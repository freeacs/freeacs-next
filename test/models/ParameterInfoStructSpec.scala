package models
import org.scalatestplus.play.PlaySpec

class ParameterInfoStructSpec extends PlaySpec {

  "A ParameterInfoStruct" should {
    "be able to parse info parameters" in new CwmpContext() {
      val xml    = mkGPNr
      val params = ParameterInfoStruct.fromNode(xml)
      params.length mustBe 18

      params.head.name mustBe "InternetGatewayDevice.DeviceInfo.Manufacturer"
      params.head.writable mustBe false

      params.last.name mustBe "InternetGatewayDevice.ManagementServer.PeriodicInformInterval"
      params.last.writable mustBe true
    }
  }
}
