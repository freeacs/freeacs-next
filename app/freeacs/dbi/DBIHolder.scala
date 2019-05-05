package freeacs.dbi

import com.typesafe.config.Config
import freeacs.dbi.SyslogConstants.FACILITY_TR069
import javax.inject.{Inject, Singleton}
import play.api.db.Database

@Singleton
class DBIHolder @Inject()(config: Config, database: Database) {

  lazy val dbi: DBI = getDBI(database)

  private def getDBI(database: Database): DBI = {
    ACS.setStrictOrder(false)
    val users = new Users(database)
    val id = new Identity(FACILITY_TR069, "latest", users.getUnprotected(Users.USER_ADMIN))
    val syslog = new Syslog(database, id)
    new DBI(database, syslog)
  }
}
