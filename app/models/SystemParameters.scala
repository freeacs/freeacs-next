package models

object SystemParameters {

  case class ParameterDefinition(name: String, flag: String)

  val DESIRED_SOFTWARE_VERSION  = ParameterDefinition("System.X_FREEACS-COM.DesiredSoftwareVersion", "X")
  val SOFTWARE_URL              = ParameterDefinition("System.X_FREEACS-COM.SoftwareURL", "X")
  val SECRET                    = ParameterDefinition("System.X_FREEACS-COM.Secret", "XC")
  val RESTART                   = ParameterDefinition("System.X_FREEACS-COM.Restart", "X")
  val RESET                     = ParameterDefinition("System.X_FREEACS-COM.Reset", "X")
  val DISCOVER                  = ParameterDefinition("System.X_FREEACS-COM.Discover", "X")
  val COMMENT                   = ParameterDefinition("System.X_FREEACS-COM.Comment", "X")
  val FIRST_CONNECT_TMS         = ParameterDefinition("System.X_FREEACS-COM.FirstConnectTms", "X")
  val LAST_CONNECT_TMS          = ParameterDefinition("System.X_FREEACS-COM.LastConnectTms", "X")
  val PROVISIONING_MODE         = ParameterDefinition("System.X_FREEACS-COM.ProvisioningMode", "X")
  val INSPECTION_MESSAGE        = ParameterDefinition("System.X_FREEACS-COM.IM.Message", "X")
  val SERVICE_WINDOW_ENABLE     = ParameterDefinition("System.X_FREEACS-COM.ServiceWindow.Enable", "X")
  val SERVICE_WINDOW_REGULAR    = ParameterDefinition("System.X_FREEACS-COM.ServiceWindow.Regular", "X")
  val SERVICE_WINDOW_DISRUPTIVE = ParameterDefinition("System.X_FREEACS-COM.ServiceWindow.Disruptive", "X")
  val SERVICE_WINDOW_FREQUENCY  = ParameterDefinition("System.X_FREEACS-COM.ServiceWindow.Frequency", "X")
  val SERVICE_WINDOW_SPREAD     = ParameterDefinition("System.X_FREEACS-COM.ServiceWindow.Spread", "X")
  val DEBUG                     = ParameterDefinition("System.X_FREEACS-COM.Debug", "X")
  val JOB_CURRENT               = ParameterDefinition("System.X_FREEACS-COM.Job.Current", "X")
  val JOB_CURRENT_KEY           = ParameterDefinition("System.X_FREEACS-COM.Job.CurrentKey", "X")
  val JOB_HISTORY               = ParameterDefinition("System.X_FREEACS-COM.Job.History", "X")
  val JOB_DISRUPTIVE            = ParameterDefinition("System.X_FREEACS-COM.Job.Disruptive", "X")
  val SERIAL_NUMBER             = ParameterDefinition("System.X_FREEACS-COM.Device.SerialNumber", "X")
  val SOFTWARE_VERSION          = ParameterDefinition("System.X_FREEACS-COM.Device.SoftwareVersion", "X")
  val PERIODIC_INTERVAL         = ParameterDefinition("System.X_FREEACS-COM.Device.PeriodicInterval", "X")
  val IP_ADDRESS                = ParameterDefinition("System.X_FREEACS-COM.Device.PublicIPAddress", "X")
  val PROTOCOL                  = ParameterDefinition("System.X_FREEACS-COM.Device.PublicProtocol", "X")
  val PORT                      = ParameterDefinition("System.X_FREEACS-COM.Device.PublicPort", "X")
  val GUI_URL                   = ParameterDefinition("System.X_FREEACS-COM.Device.GUIURL", "X")

  val values = Seq(
    DESIRED_SOFTWARE_VERSION,
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
    SERVICE_WINDOW_DISRUPTIVE,
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
