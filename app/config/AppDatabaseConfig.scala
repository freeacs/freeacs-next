package config
import play.api.db.HikariCPComponents
import play.api.db.evolutions.EvolutionsComponents
import play.api.db.slick.evolutions.SlickEvolutionsComponents
import play.api.db.slick.{DbName, SlickComponents}
import slick.jdbc.JdbcProfile

trait AppDatabaseConfig
    extends SlickEvolutionsComponents
    with EvolutionsComponents
    with HikariCPComponents
    with SlickComponents {
  this: AppConfig =>

  applicationEvolutions

  lazy val slickDbConf = slickApi.dbConfig[JdbcProfile](DbName(Settings.defaultDatabaseName))
}
