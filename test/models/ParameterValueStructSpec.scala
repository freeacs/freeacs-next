package models
import org.scalatestplus.play.PlaySpec

import scala.xml.Node

class ParameterValueStructSpec extends PlaySpec {

  val event =
    <Event SOAP-ENC:arrayType="cwmp:EventStruct[3]">
      <EventStruct>
        <EventCode>0 BOOTSTRAP</EventCode>
        <CommandKey>TR069_FakeManufacturer_HOMEGATEWAY</CommandKey>
      </EventStruct>
      <EventStruct>
        <EventCode>1 BOOT</EventCode>
        <CommandKey></CommandKey>
      </EventStruct>
      <EventStruct>
        <EventCode>4 VALUE CHANGE</EventCode>
        <CommandKey></CommandKey>
      </EventStruct>
    </Event>

  val deviceId =
    <DeviceId>
      <Manufacturer>FakeManufacturer</Manufacturer>
      <OUI>000000</OUI>
      <ProductClass>FakeProductClass</ProductClass>
      <SerialNumber>FakeSerialNumber</SerialNumber>
    </DeviceId>

  val parameterList =
    <ParameterList SOAP-ENC:arrayType="cwmp:ParameterValueStruct[10]">
      <ParameterValueStruct>
        <Name>InternetGatewayDevice.DeviceSummary</Name>
        <Value xsi:type="xsd:string">InternetGatewayDevice:1.0[](Baseline:1, EthernetLAN:4,GE:4,WiFi:1,PONWAN:1, Voip:0, Time:1, IPPing:1)</Value>
      </ParameterValueStruct>
      <ParameterValueStruct>
        <Name>InternetGatewayDevice.DeviceInfo.SpecVersion</Name>
        <Value xsi:type="xsd:string">1.0</Value>
      </ParameterValueStruct>
      <ParameterValueStruct>
        <Name>InternetGatewayDevice.DeviceInfo.HardwareVersion</Name>
        <Value xsi:type="xsd:string">V5.2</Value>
      </ParameterValueStruct>
      <ParameterValueStruct>
        <Name>InternetGatewayDevice.DeviceInfo.SoftwareVersion</Name>
        <Value xsi:type="xsd:string">V5.2.10P4T26</Value>
      </ParameterValueStruct>
    </ParameterList>

  def mkInform(parameterList: Node = parameterList, event: Node = event, deviceId: Node = deviceId) =
    <SOAP-ENV:Envelope
    xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/"
    xmlns:SOAP-ENC="http://schemas.xmlsoap.org/soap/encoding/"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:xsd="http://www.w3.org/2001/XMLSchema"
    xmlns:cwmp="urn:dslforum-org:cwmp-1-2">
      <SOAP-ENV:Header>
        <cwmp:ID SOAP-ENV:mustUnderstand="1">1</cwmp:ID>
      </SOAP-ENV:Header>
      <SOAP-ENV:Body>
        <cwmp:Inform>
          ${deviceId}
          ${event}
          <MaxEnvelopes>1</MaxEnvelopes>
          <CurrentTime>1970-01-02T00:08:34</CurrentTime>
          <RetryCount>0</RetryCount>
          ${parameterList}
        </cwmp:Inform>
      </SOAP-ENV:Body>
    </SOAP-ENV:Envelope>

  "A ParameterValueStruct" should {
    "be able to parse parameters" in {
      val inform = mkInform()
      val params = ParameterValueStruct.fromNode(inform)
      params.length mustBe 4

      params.head.name mustBe "InternetGatewayDevice.DeviceSummary"
      params.head.value mustBe "InternetGatewayDevice:1.0[](Baseline:1, EthernetLAN:4,GE:4,WiFi:1,PONWAN:1, Voip:0, Time:1, IPPing:1)"
      params.head.`type` mustBe "xsd:string"
      params.head.string mustBe true

      params(1).name mustBe "InternetGatewayDevice.DeviceInfo.SpecVersion"
      params(1).value mustBe "1.0"
      params(1).`type` mustBe "xsd:string"
      params(1).string mustBe true

      params(2).name mustBe "InternetGatewayDevice.DeviceInfo.HardwareVersion"
      params(2).value mustBe "V5.2"
      params(2).`type` mustBe "xsd:string"
      params(2).string mustBe true

      params(3).name mustBe "InternetGatewayDevice.DeviceInfo.SoftwareVersion"
      params(3).value mustBe "V5.2.10P4T26"
      params(3).`type` mustBe "xsd:string"
      params(3).string mustBe true
    }
  }
}
