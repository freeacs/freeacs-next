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
  lazy val CONFIG_FILES             = keyRoot + "DeviceInfo.VendorConfigFile."
  lazy val SOFTWARE_VERSION         = keyRoot + "DeviceInfo.SoftwareVersion"
  lazy val PERIODIC_INFORM_INTERVAL = keyRoot + "ManagementServer.PeriodicInformInterval"
  lazy val CONNECTION_URL           = keyRoot + "ManagementServer.ConnectionRequestURL"
  lazy val CONNECTION_PASSWORD      = keyRoot + "ManagementServer.ConnectionRequestPassword"
  lazy val CONNECTION_USERNAME      = keyRoot + "ManagementServer.ConnectionRequestUsername"
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
