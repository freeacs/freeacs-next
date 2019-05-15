package models
import SystemParameters._

case class SessionData(
    sessionId: String,
    unit: Option[AcsUnit] = None,
    username: Option[String] = None,
    header: Option[HeaderStruct] = None,
    deviceId: Option[DeviceIdStruct] = None,
    events: Seq[EventStruct] = Seq.empty,
    params: Seq[ParameterValueStruct] = Seq.empty,
    requests: Seq[CwmpMethod] = Seq.empty,
    cwmpVersion: String = "1-0"
) {
  import SessionData._
  lazy val keyRoot                  = getKeyRoot(params)
  lazy val CONFIG_FILES             = keyRoot.map(_ + "DeviceInfo.VendorConfigFile.").get
  lazy val SOFTWARE_VERSION         = keyRoot.map(_ + "DeviceInfo.SoftwareVersion").get
  lazy val PERIODIC_INFORM_INTERVAL = keyRoot.map(_ + "ManagementServer.PeriodicInformInterval").get
  lazy val CONNECTION_URL           = keyRoot.map(_ + "ManagementServer.ConnectionRequestURL").get
  lazy val CONNECTION_PASSWORD      = keyRoot.map(_ + "ManagementServer.ConnectionRequestPassword").get
  lazy val CONNECTION_USERNAME      = keyRoot.map(_ + "ManagementServer.ConnectionRequestUsername").get
  lazy val unitId                   = username.orElse(unit.map(_.unitId).orElse(deviceId.map(_.unitId)))
  lazy val serialNumber             = deviceId.map(_.serialNumber.underlying)
  lazy val firstConnect             = unit.exists(!_.params.exists(_.unitTypeParamName != SECRET.name))
  lazy val unsafeGetUnitId          = unitId.get
  def unsafeGetProductClass(appendHwVersion: Boolean): String =
    SessionData.unsafeGetProductClass(deviceId, appendHwVersion)
}

object SessionData {
  def unsafeGetProductClass(deviceId: Option[DeviceIdStruct], appendHwVersion: Boolean): String =
    deviceId.map { deviceId =>
      // TODO
      deviceId.productClass.underlying
    }.get

  def getKeyRoot(params: Seq[ParameterValueStruct]) =
    params
      .map(_.name.split('.'))
      .find(p => p(0) == "Device" || p(0) == "InternetGatewayDevice")
      .map(_(0) + '.')
}
