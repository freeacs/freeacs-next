package models

case class SessionData(
    sessionId: String,
    unit: Option[freeacs.dbi.Unit],
    header: HeaderStruct,
    deviceId: Option[DeviceIdStruct],
    events: Seq[EventStruct],
    params: Seq[ParameterValueStruct]
)
