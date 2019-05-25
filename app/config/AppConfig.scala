package config
import com.typesafe.config.Config
import play.api.ConfigLoader

case class AppConfig(discoveryMode: Boolean, authMethod: String, digestSecret: String)

object AppConfig {
  implicit val configLoader: ConfigLoader[AppConfig] = (rootConfig: Config, path: String) => {
    val config = rootConfig.getConfig(path)
    AppConfig(
      discoveryMode = config.getBoolean("discovery.mode"),
      authMethod = config.getString("auth.method"),
      digestSecret = config.getString("digest.secret")
    )
  }
}
