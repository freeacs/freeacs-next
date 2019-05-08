package models

case class SessionData(
    sessionId: String,
    unit: Option[freeacs.dbi.Unit],
    header: HeaderStruct,
    deviceIdStruct: DeviceIdStruct,
    events: Seq[EventStruct],
    params: Seq[ParameterValueStruct]
)
