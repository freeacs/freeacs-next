package models

case class SessionData(
    sessionId: String,
    header: HeaderStruct,
    unit: Option[freeacs.dbi.Unit] = None,
    deviceId: Option[DeviceIdStruct] = None,
    events: Seq[EventStruct] = Seq.empty,
    params: Seq[ParameterValueStruct] = Seq.empty
)
