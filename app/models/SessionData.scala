package models

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
  def unitId = unit.map(_.unitId).orElse(deviceId.map(_.unitId))
}
