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
  lazy val keyRoot: Option[String] = params
    .find(_.name.startsWith("Device."))
    .orElse(params.find(_.name.startsWith("InternetGatewayDevice.")))
    .map(_.name.split('.')(0) + '.')
  lazy val unitId: Option[String]       = username.orElse(unit.map(_.unitId).orElse(deviceId.map(_.unitId)))
  lazy val serialNumber: Option[String] = deviceId.map(_.serialNumber.underlying)
  lazy val firstConnect: Boolean        = unit.exists(!_.params.exists(_.unitTypeParamName != SECRET))
  def unsafeGetUnitId: String           = unitId.get
  def unsafeGetProductClass(appendHwVersion: Boolean): String =
    deviceId.map { deviceId =>
      // TODO
      deviceId.productClass.underlying
    }.get
}
