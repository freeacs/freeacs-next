package models

case class SessionData(
    sessionId: String,
    unitId: Option[String] = None,
    header: Option[HeaderStruct] = None,
    deviceId: Option[DeviceIdStruct] = None,
    events: Seq[EventStruct] = Seq.empty,
    params: Seq[ParameterValueStruct] = Seq.empty,
    requests: Seq[CwmpMethod] = Seq.empty
)
