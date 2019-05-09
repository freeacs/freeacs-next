package config
import play.api.db.{DBComponents, HikariCPComponents}
import play.api.db.evolutions.EvolutionsComponents
import play.api.db.slick.{DbName, SlickComponents}
import slick.jdbc.JdbcProfile

trait AppDatabaseConfig extends DBComponents with EvolutionsComponents with HikariCPComponents with SlickComponents {
  this: AppConfig =>

  applicationEvolutions

  lazy val slickDbConf = slickApi.dbConfig[JdbcProfile](DbName(defaultDatabaseName))

  lazy val playDb = dbApi.database(defaultDatabaseName)

}
