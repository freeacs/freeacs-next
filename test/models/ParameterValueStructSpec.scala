package models
import org.scalatestplus.play.PlaySpec

import scala.xml.Node

class ParameterValueStructSpec extends PlaySpec {

  "A ParameterValueStruct" should {
    "be able to parse parameters" in new CwmpContext() {
      val inform = mkInform()
      val params = ParameterValueStruct.fromNode(inform)
      params.length mustBe 4

      params.head.name mustBe "InternetGatewayDevice.DeviceSummary"
      params.head.value mustBe Some(
        "InternetGatewayDevice:1.0[](Baseline:1, EthernetLAN:4,GE:4,WiFi:1,PONWAN:1, Voip:0, Time:1, IPPing:1)"
      )
      params.head.`type` mustBe "xsd:string"
      params.head.string mustBe true

      params(1).name mustBe "InternetGatewayDevice.DeviceInfo.SpecVersion"
      params(1).value mustBe Some("1.0")
      params(1).`type` mustBe "xsd:string"
      params(1).string mustBe true

      params(2).name mustBe "InternetGatewayDevice.DeviceInfo.HardwareVersion"
      params(2).value mustBe Some("V5.2")
      params(2).`type` mustBe "xsd:string"
      params(2).string mustBe true

      params(3).name mustBe "InternetGatewayDevice.DeviceInfo.SoftwareVersion"
      params(3).value mustBe Some("V5.2.10P4T26")
      params(3).`type` mustBe "xsd:string"
      params(3).string mustBe true
    }
  }
}
