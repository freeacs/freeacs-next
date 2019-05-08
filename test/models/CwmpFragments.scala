package models

trait CwmpFragments {
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

  val parameterNameList =
    <ParameterList soap:arrayType="cwmp:ParameterInfoStruct[18]">
      <ParameterInfoStruct>
        <Name>InternetGatewayDevice.DeviceInfo.Manufacturer</Name>
        <Writable>0</Writable>
      </ParameterInfoStruct>
      <ParameterInfoStruct>
        <Name>InternetGatewayDevice.DeviceInfo.ManufacturerOUI</Name>
        <Writable>0</Writable>
      </ParameterInfoStruct>
      <ParameterInfoStruct>
        <Name>InternetGatewayDevice.DeviceInfo.ModelName</Name>
        <Writable>0</Writable>
      </ParameterInfoStruct>
      <ParameterInfoStruct>
        <Name>InternetGatewayDevice.DeviceInfo.Description</Name>
        <Writable>0</Writable>
      </ParameterInfoStruct>
      <ParameterInfoStruct>
        <Name>InternetGatewayDevice.DeviceInfo.ProductClass</Name>
        <Writable>0</Writable>
      </ParameterInfoStruct>
      <ParameterInfoStruct>
        <Name>InternetGatewayDevice.DeviceInfo.SerialNumber</Name>
        <Writable>0</Writable>
      </ParameterInfoStruct>
      <ParameterInfoStruct>
        <Name>InternetGatewayDevice.DeviceInfo.HardwareVersion</Name>
        <Writable>0</Writable>
      </ParameterInfoStruct>
      <ParameterInfoStruct>
        <Name>InternetGatewayDevice.DeviceInfo.SoftwareVersion</Name>
        <Writable>0</Writable>
      </ParameterInfoStruct>
      <ParameterInfoStruct>
        <Name>InternetGatewayDevice.DeviceInfo.ModemFirmwareVersion</Name>
        <Writable>0</Writable>
      </ParameterInfoStruct>
      <ParameterInfoStruct>
        <Name>InternetGatewayDevice.DeviceInfo.AdditionalHardwareVersion</Name>
        <Writable>0</Writable>
      </ParameterInfoStruct>
      <ParameterInfoStruct>
        <Name>InternetGatewayDevice.DeviceInfo.SpecVersion</Name>
        <Writable>0</Writable>
      </ParameterInfoStruct>
      <ParameterInfoStruct>
        <Name>InternetGatewayDevice.DeviceInfo.ProvisioningCode</Name>
        <Writable>1</Writable>
      </ParameterInfoStruct>
      <ParameterInfoStruct>
        <Name>InternetGatewayDevice.DeviceInfo.UpTime</Name>
        <Writable>0</Writable>
      </ParameterInfoStruct>
      <ParameterInfoStruct>
        <Name>InternetGatewayDevice.DeviceInfo.X_000E50_Country</Name>
        <Writable>1</Writable>
      </ParameterInfoStruct>
      <ParameterInfoStruct>
        <Name>InternetGatewayDevice.DeviceInfo.X_000E50_SIMLocked</Name>
        <Writable>0</Writable>
      </ParameterInfoStruct>
      <ParameterInfoStruct>
        <Name>InternetGatewayDevice.DeviceInfo.VendorConfigFileNumberOfEntries</Name>
        <Writable>0</Writable>
      </ParameterInfoStruct>
      <ParameterInfoStruct>
        <Name>InternetGatewayDevice.DeviceInfo.VendorConfigFile.</Name>
        <Writable>0</Writable>
      </ParameterInfoStruct>
      <ParameterInfoStruct>
        <Name>InternetGatewayDevice.ManagementServer.PeriodicInformInterval</Name>
        <Writable>1</Writable>
      </ParameterInfoStruct>
    </ParameterList>
}
