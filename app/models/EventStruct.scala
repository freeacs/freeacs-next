package models

case class EventStruct(eventCode: String, commandKey: String) {
  lazy val factoryReset               = eventCode.startsWith("0")
  lazy val booted                     = eventCode.startsWith("1")
  lazy val periodic                   = eventCode.startsWith("2")
  lazy val valueChange                = eventCode.startsWith("4")
  lazy val kicked                     = eventCode.startsWith("6")
  lazy val transferComplete           = eventCode.startsWith("7")
  lazy val diagnosticsComplete        = eventCode.startsWith("8")
  lazy val autonomousTransferComplete = eventCode.startsWith("10")
}
