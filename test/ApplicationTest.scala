import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneServerPerTest
import play.api.{Application, Configuration, Mode}
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.ws.{DefaultWSCookie, WSClient, WSResponse}
import services.UnitService

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.xml.Utility.trim
import scala.concurrent.ExecutionContext.Implicits.global
import scala.xml.Elem

class ApplicationTest extends PlaySpec with GuiceOneServerPerTest {

  override def fakeApplication(): Application =
    GuiceApplicationBuilder()
      .loadConfig(
        env =>
          Configuration.load(
            env,
            Map(
              "app.auth.method" -> "none"
            )
        )
      )
      .in(Mode.Test)
      .build()

  "the tr069 server can provision a unit" in new StatefulTr069Conversation {
    // 1. IN
    var response = post(baseUrl, Some(informRequest))
    response.status mustBe 200
    trim(response.xml) mustBe trim(informResponse)
    val unitService = app.injector.instanceOf[UnitService]
    unitService.find("000000-FakeProductClass-FakeSerialNumber") must not be None

    // 2. EM
    response = post(baseUrl, None)
    response.status mustBe 200
    trim(response.xml) mustBe trim(getParameterValuesRequest)

    // 3. TODO
  }

  trait StatefulTr069Conversation {
    val baseUrl: String                  = s"http://localhost:$port/tr069"
    var session: Option[DefaultWSCookie] = None

    def post(url: String, body: Option[Elem]): WSResponse = {
      val client  = app.injector.instanceOf[WSClient]
      val request = session.map(client.url(url).withCookies(_)).getOrElse(client.url(url))
      Await.result(
        body.map(request.post(_)).getOrElse(request.post("")).map { response =>
          session = response.header("Set-Cookie").map { header =>
            DefaultWSCookie("SESSION", header.stripPrefix("SESSION=").split(";")(0))
          }
          response
        },
        Duration.Inf
      )
    }
  }

  val getParameterValuesRequest =
    <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                      xmlns:soapenc="http://schemas.xmlsoap.org/soap/encoding/"
                      xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                      xmlns:cwmp="urn:dslforum-org:cwmp-1-0">
      <soapenv:Header>
        <cwmp:ID soapenv:mustUnderstand="1">1</cwmp:ID>
      </soapenv:Header>
      <soapenv:Body>
        <cwmp:GetParameterValues>
          <ParameterNames soapenc:arrayType="xsd:string[1]">
            <string>InternetGatewayDevice.ManagementServer.PeriodicInformInterval</string>
          </ParameterNames>
        </cwmp:GetParameterValues>
      </soapenv:Body>
    </soapenv:Envelope>

  val informResponse =
    <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                      xmlns:soapenc="http://schemas.xmlsoap.org/soap/encoding/"
                      xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                      xmlns:cwmp={s"urn:dslforum-org:cwmp-1-2"}>
      <soapenv:Header>
        <cwmp:ID soapenv:mustUnderstand="1">1</cwmp:ID>
      </soapenv:Header>
      <soapenv:Body>
        <cwmp:InformResponse>
          <MaxEnvelopes>1</MaxEnvelopes>
        </cwmp:InformResponse>
      </soapenv:Body>
    </soapenv:Envelope>

  val informRequest =
    <SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/"
                       xmlns:SOAP-ENC="http://schemas.xmlsoap.org/soap/encoding/"
                       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                       xmlns:cwmp="urn:dslforum-org:cwmp-1-2">
      <SOAP-ENV:Header>
        <cwmp:ID SOAP-ENV:mustUnderstand="1">1</cwmp:ID>
      </SOAP-ENV:Header>
      <SOAP-ENV:Body>
        <cwmp:Inform>
          <DeviceId>
            <Manufacturer>FakeManufacturer</Manufacturer>
            <OUI>000000</OUI>
            <ProductClass>FakeProductClass</ProductClass>
            <SerialNumber>FakeSerialNumber</SerialNumber>
          </DeviceId>
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
          <MaxEnvelopes>1</MaxEnvelopes>
          <CurrentTime>1970-01-02T00:08:34</CurrentTime>
          <RetryCount>0</RetryCount>
          <ParameterList SOAP-ENC:arrayType="cwmp:ParameterValueStruct[10]">
            <ParameterValueStruct>
              <Name>InternetGatewayDevice.DeviceSummary</Name>
              <Value xsi:type="xsd:string">InternetGatewayDevice:1.0[](Baseline:1, EthernetLAN:4,GE:4,WiFi:1,
                PONWAN:1, Voip:0, Time:1, IPPing:1)
              </Value>
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
        </cwmp:Inform>
      </SOAP-ENV:Body>
    </SOAP-ENV:Envelope>
}
