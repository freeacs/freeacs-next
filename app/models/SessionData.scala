package models

case class SessionData(
    sessionId: String,
    unit: Option[freeacs.dbi.Unit],
    header: HeaderStruct,
    events: Seq[EventStruct],
    params: Seq[ParameterValueStruct],
    deviceIdStruct: DeviceIdStruct
)
