package freeacs.dbi

import com.typesafe.config.Config
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import freeacs.dbi.SyslogConstants.FACILITY_TR069
import javax.inject.{Inject, Singleton}
import javax.sql.DataSource

@Singleton
class DBIHolder @Inject()(config: Config, dataSource: DataSource) {

  val dbi: DBI = getDBI(dataSource)

  private def getDBI(dataSource: DataSource): DBI = {
    ACS.setStrictOrder(false)
    val users = new Users(dataSource)
    val id = new Identity(FACILITY_TR069, "latest", users.getUnprotected(Users.USER_ADMIN))
    val syslog = new Syslog(dataSource, id)
    new DBI(Integer.MAX_VALUE, dataSource, syslog)
  }
}
