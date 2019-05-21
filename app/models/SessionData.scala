package models

case class SessionData(
    sessionId: String,
    unit: AcsUnit,
    username: String,
    header: HeaderStruct,
    deviceId: DeviceIdStruct,
    events: Seq[EventStruct] = Seq.empty,
    params: Seq[ParameterValueStruct] = Seq.empty,
    requests: Seq[CwmpMethod] = Seq.empty,
    recentlyCreated: Seq[String] = Seq.empty,
    cwmpVersion: String = "1-0"
) {
  import SessionData._
  lazy val keyRoot: Option[String]          = getKeyRoot(params)
  lazy val CONFIG_FILES: String             = keyRoot.map(_ + "DeviceInfo.VendorConfigFile.").get
  lazy val SOFTWARE_VERSION: String         = keyRoot.map(_ + "DeviceInfo.SoftwareVersion").get
  lazy val PERIODIC_INFORM_INTERVAL: String = keyRoot.map(_ + "ManagementServer.PeriodicInformInterval").get
  lazy val CONNECTION_URL: String           = keyRoot.map(_ + "ManagementServer.ConnectionRequestURL").get
  lazy val CONNECTION_PASSWORD: String      = keyRoot.map(_ + "ManagementServer.ConnectionRequestPassword").get
  lazy val CONNECTION_USERNAME: String      = keyRoot.map(_ + "ManagementServer.ConnectionRequestUsername").get
}

object SessionData {
  def getKeyRoot(params: Seq[ParameterValueStruct]): Option[String] =
    params
      .map(_.name.split('.'))
      .find(p => p(0) == "Device" || p(0) == "InternetGatewayDevice")
      .map(_(0) + '.')
}
