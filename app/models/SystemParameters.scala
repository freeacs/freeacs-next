package models

object SystemParameters {

  sealed abstract class Parameter(val name: String, val flag: String)

  case object desiredSoftwareVersion   extends Parameter("System.X_FREEACS-COM.DesiredSoftwareVersion", "X")
  case object SOFTWARE_URL             extends Parameter("System.X_FREEACS-COM.SoftwareURL", "X")
  case object SECRET                   extends Parameter("System.X_FREEACS-COM.Secret", "XC")
  case object RESTART                  extends Parameter("System.X_FREEACS-COM.Restart", "X")
  case object RESET                    extends Parameter("System.X_FREEACS-COM.Reset", "X")
  case object DISCOVER                 extends Parameter("System.X_FREEACS-COM.Discover", "X")
  case object COMMENT                  extends Parameter("System.X_FREEACS-COM.Comment", "X")
  case object FIRST_CONNECT_TMS        extends Parameter("System.X_FREEACS-COM.FirstConnectTms", "X")
  case object LAST_CONNECT_TMS         extends Parameter("System.X_FREEACS-COM.LastConnectTms", "X")
  case object PROVISIONING_MODE        extends Parameter("System.X_FREEACS-COM.ProvisioningMode", "X")
  case object INSPECTION_MESSAGE       extends Parameter("System.X_FREEACS-COM.IM.Message", "X")
  case object SERVICE_WINDOW_ENABLE    extends Parameter("System.X_FREEACS-COM.ServiceWindow.Enable", "X")
  case object SERVICE_WINDOW_REGULAR   extends Parameter("System.X_FREEACS-COM.ServiceWindow.Regular", "X")
  case object SERVICE_WINDOW_DISRUPT   extends Parameter("System.X_FREEACS-COM.ServiceWindow.Disruptive", "X")
  case object SERVICE_WINDOW_FREQUENCY extends Parameter("System.X_FREEACS-COM.ServiceWindow.Frequency", "X")
  case object SERVICE_WINDOW_SPREAD    extends Parameter("System.X_FREEACS-COM.ServiceWindow.Spread", "X")
  case object DEBUG                    extends Parameter("System.X_FREEACS-COM.Debug", "X")
  case object JOB_CURRENT              extends Parameter("System.X_FREEACS-COM.Job.Current", "X")
  case object JOB_CURRENT_KEY          extends Parameter("System.X_FREEACS-COM.Job.CurrentKey", "X")
  case object JOB_HISTORY              extends Parameter("System.X_FREEACS-COM.Job.History", "X")
  case object JOB_DISRUPTIVE           extends Parameter("System.X_FREEACS-COM.Job.Disruptive", "X")
  case object SERIAL_NUMBER            extends Parameter("System.X_FREEACS-COM.Device.SerialNumber", "X")
  case object SOFTWARE_VERSION         extends Parameter("System.X_FREEACS-COM.Device.SoftwareVersion", "X")
  case object PERIODIC_INTERVAL        extends Parameter("System.X_FREEACS-COM.Device.PeriodicInterval", "X")
  case object IP_ADDRESS               extends Parameter("System.X_FREEACS-COM.Device.PublicIPAddress", "X")
  case object PROTOCOL                 extends Parameter("System.X_FREEACS-COM.Device.PublicProtocol", "X")
  case object PORT                     extends Parameter("System.X_FREEACS-COM.Device.PublicPort", "X")
  case object GUI_URL                  extends Parameter("System.X_FREEACS-COM.Device.GUIURL", "X")

  val values = Seq(
    desiredSoftwareVersion,
    SOFTWARE_URL,
    SECRET,
    RESTART,
    RESET,
    DISCOVER,
    COMMENT,
    FIRST_CONNECT_TMS,
    LAST_CONNECT_TMS,
    PROVISIONING_MODE,
    INSPECTION_MESSAGE,
    SERVICE_WINDOW_ENABLE,
    SERVICE_WINDOW_REGULAR,
    SERVICE_WINDOW_DISRUPT,
    SERVICE_WINDOW_FREQUENCY,
    SERVICE_WINDOW_SPREAD,
    DEBUG,
    JOB_CURRENT,
    JOB_CURRENT_KEY,
    JOB_HISTORY,
    JOB_DISRUPTIVE,
    SERIAL_NUMBER,
    SOFTWARE_VERSION,
    PERIODIC_INTERVAL,
    IP_ADDRESS,
    PROTOCOL,
    PORT,
    GUI_URL
  )
}
