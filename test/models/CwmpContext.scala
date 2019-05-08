package models
import scala.xml.Node

trait CwmpContext extends CwmpFragments {

  def mkIN(parameterList: Node = parameterList, event: Node = event, deviceId: Node = deviceId) =
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

  def mkGPNr =
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
        <cwmp:GetParameterNamesResponse>
          ${parameterNameList}
        </cwmp:GetParameterNamesResponse>
      </SOAP-ENV:Body>
    </SOAP-ENV:Envelope>

}
