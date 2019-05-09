package config
import com.typesafe.config.ConfigFactory

trait AppConfig {
  lazy val config = ConfigFactory.load()

  val defaultDatabaseName = "default"
}
