package config
import com.typesafe.config.ConfigFactory
import freeacs.tr069.Properties

trait AppConfig {
  lazy val config     = ConfigFactory.load()
  lazy val properties = new Properties(config)

  val defaultDatabaseName = "default"
}
