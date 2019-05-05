package freeacs.dbi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.api.db.Database;

import java.sql.SQLException;

/**
 * Summary
 *
 * <p>This class will act as a perfect cache for a lot of the objects found in this package. The
 * properties of a perfect cache is:
 *
 * <p>- fast access - updated data
 *
 * <p>This last requirement is only possible since DBI will build upon a table called 'message',
 * which will convey messages from ACS applications and inform all other ACS applications about
 * changes to the tables/data.
 *
 * <p>Details
 *
 * <p>The main idea of this class is to be both a listener (subscriber) and a broadcaster
 * (publisher) of changes to the various objects (data) found in the ACS database tables. To do the
 * "listening" we need one thread which constantly polls on a special table called "message". By
 * polling every second we would quickly discover changes made to the various objects in DBI. If
 * user of DBI changes some data in an object (table), the user should in general notify DBI of such
 * a change, to allow DBI to send this information to the message table.
 *
 * <p>These objects/tables are under DBI caching mechanism:
 *
 * <p>Objects Tables ------------------------------------------------------ DBI message ACS
 * filestore (firmware) group_ group_param profile profile_param syslog_event unit_type
 * unit_type_param unit_type_param_value job job_param (only parameters for unitid =
 * ANY_UNIT_IN_GROUP)
 *
 * <p>These objects/tables are *not* part of DBI caching mechanism, and should never be expected to
 * be updated on application level unless explicitly updated.
 *
 * <p>Objects Tables ------------------------------------------------------ Syslog syslog UnitJobs
 * unit_job Users user_ permission_ ACSJobs job_param (all parameters for unitid !=
 * ANY_UNIT_IN_GROUP) ACSUnit unit unit_param
 *
 * <p>Messages that are sent/received must contain this information:
 *
 * <p>Sender Receiver Type Object-type Object-id Timestamp Content
 *
 * @author Morten
 */
public class DBI {
  private static Logger logger = LoggerFactory.getLogger(DBI.class);

  private Database dataSource;
  private ACS acs;
  private Syslog syslog;

  public DBI(Database dataSource, Syslog syslog) throws SQLException {
    this.dataSource = dataSource;
    this.syslog = syslog;
    this.acs = new ACS(dataSource, syslog);
    acs.setDbi(this);
    logger.debug("DBI is loaded for user " + syslog.getIdentity().getUser().getFullname());
  }

  public ACS getAcs() {
    return acs;
  }

  public Database getDataSource() {
    return dataSource;
  }

  public Syslog getSyslog() {
    return syslog;
  }

  public ACSUnit getACSUnit() {
    return new ACSUnit(dataSource, acs, syslog);
  }
}
