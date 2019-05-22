package config
import com.typesafe.config.{Config, ConfigFactory}

trait AppConfig {
  lazy val config   = ConfigFactory.load()
  lazy val settings = new Settings(config)
}

class Settings(config: Config) {
  val defaultDatabaseName = "default"
  val discoveryMode       = config.getBoolean("discovery.mode")
  val appendHwVersion     = config.getBoolean("unit.type.append-hw-version")
}
