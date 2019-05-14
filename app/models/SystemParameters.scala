package models

object SystemParameters {

  val DESIRED_SOFTWARE_VERSION  = "System.X_FREEACS-COM.DesiredSoftwareVersion"
  val SOFTWARE_URL              = "System.X_FREEACS-COM.SoftwareURL"
  val SECRET                    = "System.X_FREEACS-COM.Secret"
  val RESTART                   = "System.X_FREEACS-COM.Restart"
  val RESET                     = "System.X_FREEACS-COM.Reset"
  val DISCOVER                  = "System.X_FREEACS-COM.Discover"
  val COMMENT                   = "System.X_FREEACS-COM.Comment"
  val FIRST_CONNECT_TMS         = "System.X_FREEACS-COM.FirstConnectTms"
  val LAST_CONNECT_TMS          = "System.X_FREEACS-COM.LastConnectTms"
  val PROVISIONING_MODE         = "System.X_FREEACS-COM.ProvisioningMode"
  val INSPECTION_MESSAGE        = "System.X_FREEACS-COM.IM.Message"
  val SERVICE_WINDOW_ENABLE     = "System.X_FREEACS-COM.ServiceWindow.Enable"
  val SERVICE_WINDOW_REGULAR    = "System.X_FREEACS-COM.ServiceWindow.Regular"
  val SERVICE_WINDOW_DISRUPTIVE = "System.X_FREEACS-COM.ServiceWindow.Disruptive"
  val SERVICE_WINDOW_FREQUENCY  = "System.X_FREEACS-COM.ServiceWindow.Frequency"
  val SERVICE_WINDOW_SPREAD     = "System.X_FREEACS-COM.ServiceWindow.Spread"
  val DEBUG                     = "System.X_FREEACS-COM.Debug"
  val JOB_CURRENT               = "System.X_FREEACS-COM.Job.Current"
  val JOB_CURRENT_KEY           = "System.X_FREEACS-COM.Job.CurrentKey"
  val JOB_HISTORY               = "System.X_FREEACS-COM.Job.History"
  val JOB_DISRUPTIVE            = "System.X_FREEACS-COM.Job.Disruptive"
  val SERIAL_NUMBER             = "System.X_FREEACS-COM.Device.SerialNumber"
  val SOFTWARE_VERSION          = "System.X_FREEACS-COM.Device.SoftwareVersion"
  val PERIODIC_INTERVAL         = "System.X_FREEACS-COM.Device.PeriodicInterval"
  val IP_ADDRESS                = "System.X_FREEACS-COM.Device.PublicIPAddress"
  val PROTOCOL                  = "System.X_FREEACS-COM.Device.PublicProtocol"
  val PORT                      = "System.X_FREEACS-COM.Device.PublicPort"
  val GUI_URL                   = "System.X_FREEACS-COM.Device.GUIURL"

  var commonParameters = Map[String, String](
    DESIRED_SOFTWARE_VERSION  -> "X",
    SOFTWARE_URL              -> "X",
    PROVISIONING_MODE         -> "X",
    INSPECTION_MESSAGE        -> "X",
    SERVICE_WINDOW_ENABLE     -> "X",
    SERVICE_WINDOW_REGULAR    -> "X",
    SERVICE_WINDOW_DISRUPTIVE -> "X",
    SERVICE_WINDOW_FREQUENCY  -> "X",
    SERVICE_WINDOW_SPREAD     -> "X",
    DEBUG                     -> "X",
    JOB_CURRENT               -> "X",
    JOB_CURRENT_KEY           -> "X",
    JOB_HISTORY               -> "X",
    JOB_DISRUPTIVE            -> "X",
    FIRST_CONNECT_TMS         -> "X",
    LAST_CONNECT_TMS          -> "X",
    SERIAL_NUMBER             -> "X",
    RESTART                   -> "X",
    RESET                     -> "X",
    DISCOVER                  -> "X",
    COMMENT                   -> "X",
    SECRET                    -> "XC",
    SOFTWARE_VERSION          -> "X",
    PERIODIC_INTERVAL         -> "X",
    IP_ADDRESS                -> "X",
    PROTOCOL                  -> "X",
    PORT                      -> "X",
    GUI_URL                   -> "X"
  )
}
