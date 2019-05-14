package config
import com.typesafe.config.ConfigFactory

trait AppConfig {
  lazy val config = ConfigFactory.load()

  object Settings {
    val defaultDatabaseName = "default"
    lazy val discoveryMode  = config.getBoolean("discovery.mode")
  }
}
