# FreeACS Next

The goal of this project is to combine that which has previously been developed and deployed individually in the FreeACS project. 

## Send an inform

1. Change the digest.secret in application.conf
2. Start the application either with sbt run or from your preferred IDE
3. Send an Inform to the acs like the curl below:

```
curl -v -d '<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/" xmlns:SOAP-ENC="http://schemas.xmlsoap.org/soap/encoding/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:cwmp="urn:dslforum-org:cwmp-1-2"><SOAP-ENV:Header><cwmp:ID SOAP-ENV:mustUnderstand="1">1</cwmp:ID></SOAP-ENV:Header><SOAP-ENV:Body><cwmp:Inform><DeviceId><Manufacturer>FakeManufacturer</Manufacturer><OUI>000000</OUI><ProductClass>FakeProductClass</ProductClass><SerialNumber>FakeSerialNumber</SerialNumber></DeviceId><Event SOAP-ENC:arrayType="cwmp:EventStruct[3]"><EventStruct><EventCode>0 BOOTSTRAP</EventCode><CommandKey>TR069_FakeManufacturer_HOMEGATEWAY</CommandKey></EventStruct><EventStruct><EventCode>1 BOOT</EventCode><CommandKey></CommandKey></EventStruct><EventStruct><EventCode>4 VALUE CHANGE</EventCode><CommandKey></CommandKey></EventStruct></Event><MaxEnvelopes>1</MaxEnvelopes><CurrentTime>1970-01-02T00:08:34</CurrentTime><RetryCount>0</RetryCount><ParameterList SOAP-ENC:arrayType="cwmp:ParameterValueStruct[10]"><ParameterValueStruct><Name>InternetGatewayDevice.DeviceSummary</Name><Value xsi:type="xsd:string">InternetGatewayDevice:1.0[](Baseline:1, EthernetLAN:4,GE:4,WiFi:1,PONWAN:1, Voip:0, Time:1, IPPing:1)</Value></ParameterValueStruct><ParameterValueStruct><Name>InternetGatewayDevice.DeviceInfo.SpecVersion</Name><Value xsi:type="xsd:string">1.0</Value></ParameterValueStruct><ParameterValueStruct><Name>InternetGatewayDevice.DeviceInfo.HardwareVersion</Name><Value xsi:type="xsd:string">V5.2</Value></ParameterValueStruct><ParameterValueStruct><Name>InternetGatewayDevice.DeviceInfo.SoftwareVersion</Name><Value xsi:type="xsd:string">V5.2.10P4T26</Value></ParameterValueStruct></ParameterList></cwmp:Inform></SOAP-ENV:Body></SOAP-ENV:Envelope>' \
    -H "Content-Type: text/xml"  \
    --digest -u test:test \
    -X POST http://localhost:9000/tr069
```

The server will log this:

```
[info] c.SecureAction - Receiving request from 127.0.0.1
[debug] c.SecureAction - HTTP request: POST /tr069
[warn] c.Tr069Controller - Got an Inform from unit [test]. SessionData:
SessionData(
  "90253ea0-7465-4ecc-bc97-8bd38b968273",
  Some("test"),
  Some(HeaderStruct("1", false, false)),
  Some(DeviceIdStruct(FakeManufacturer, 000000, FakeProductClass, FakeSerialNumber)),
  List(
    EventStruct("0 BOOTSTRAP", Some("TR069_FakeManufacturer_HOMEGATEWAY")),
    EventStruct("1 BOOT", None),
    EventStruct("4 VALUE CHANGE", None)
  ),
  List(
    ParameterValueStruct(
      "InternetGatewayDevice.DeviceSummary",
      "InternetGatewayDevice:1.0[](Baseline:1, EthernetLAN:4,GE:4,WiFi:1,PONWAN:1, Voip:0, Time:1, IPPing:1)",
      "xsd:string"
    ),
    ParameterValueStruct("InternetGatewayDevice.DeviceInfo.SpecVersion", "1.0", "xsd:string"),
    ParameterValueStruct("InternetGatewayDevice.DeviceInfo.HardwareVersion", "V5.2", "xsd:string"),
    ParameterValueStruct(
      "InternetGatewayDevice.DeviceInfo.SoftwareVersion",
      "V5.2.10P4T26",
      "xsd:string"
    )
  ),
  List()
)
```
