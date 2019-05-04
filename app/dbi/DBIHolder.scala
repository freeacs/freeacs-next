package dbi

import com.typesafe.config.Config
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import dbi.SyslogConstants.FACILITY_TR069
import javax.inject.{Inject, Singleton}
import javax.sql.DataSource

@Singleton
class DBIHolder @Inject()(config: Config) {

  val dbi = getDBI(getDataSource(config))

  private def getDBI(dataSource: DataSource): DBI = {
    ACS.setStrictOrder(false)
    val users = new Users(dataSource)
    val id = new Identity(FACILITY_TR069, "latest", users.getUnprotected(Users.USER_ADMIN))
    val syslog = new Syslog(dataSource, id)
    new DBI(Integer.MAX_VALUE, dataSource, syslog)
  }

  private def getDataSource(config: Config): DataSource = {
    val hikariConfig = new HikariConfig
    hikariConfig.setDriverClassName(config.getString("main.datasource.driverClassName"))
    hikariConfig.setJdbcUrl(config.getString("main.datasource.jdbcUrl"))
    hikariConfig.setUsername(config.getString("main.datasource.username"))
    hikariConfig.setPassword(config.getString("main.datasource.password"))
    hikariConfig.setMinimumIdle(config.getInt("main.datasource.minimum-idle"))
    hikariConfig.setMaximumPoolSize(config.getInt("main.datasource.maximum-pool-size"))
    hikariConfig.setConnectionTestQuery("SELECT 1")
    hikariConfig.setPoolName(config.getString("main.datasource.poolName"))
    hikariConfig.addDataSourceProperty("dataSource.cachePrepStmts", "true")
    hikariConfig.addDataSourceProperty("dataSource.prepStmtCacheSize", "250")
    hikariConfig.addDataSourceProperty("dataSource.prepStmtCacheSqlLimit", "2048")
    hikariConfig.addDataSourceProperty("dataSource.useServerPrepStmts", "true")
    hikariConfig.setAutoCommit(true)
    new HikariDataSource(hikariConfig)
  }
}
