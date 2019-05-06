package dao
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.jdbc.MySQLProfile
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  import slick.collection.heterogeneous._
  import slick.collection.heterogeneous.syntax._
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Array(Certificate.schema, Filestore.schema, Group.schema, GroupParam.schema, Heartbeat.schema, Job.schema, JobParam.schema, Message.schema, MonitorEvent.schema, Permission.schema, Profile.schema, ProfileParam.schema, ReportGatewayTr.schema, ReportGroup.schema, ReportHw.schema, ReportHwTr.schema, ReportJob.schema, ReportProv.schema, ReportSyslog.schema, ReportUnit.schema, ReportVoip.schema, ReportVoipTr.schema, ScriptExecution.schema, Syslog.schema, SyslogEvent.schema, TestCase.schema, TestCaseFiles.schema, TestCaseParam.schema, TestHistory.schema, Trigger.schema, TriggerEvent.schema, TriggerRelease.schema, Unit.schema, UnitJob.schema, UnitParam.schema, UnitParamSession.schema, UnitType.schema, UnitTypeParam.schema, UnitTypeParamValue.schema, User.schema).reduceLeft(_ ++ _)
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Certificate
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(VARCHAR), Length(64,true)
   *  @param certificate Database column certificate SqlType(VARCHAR), Length(256,true) */
  case class CertificateRow(id: Int, name: String, certificate: String)
  /** GetResult implicit for fetching CertificateRow objects using plain SQL queries */
  implicit def GetResultCertificateRow(implicit e0: GR[Int], e1: GR[String]): GR[CertificateRow] = GR{
    prs => import prs._
    CertificateRow.tupled((<<[Int], <<[String], <<[String]))
  }
  /** Table description of table certificate. Objects of this class serve as prototypes for rows in queries. */
  class Certificate(_tableTag: Tag) extends profile.api.Table[CertificateRow](_tableTag, Some("acs"), "certificate") {
    def * = (id, name, certificate) <> (CertificateRow.tupled, CertificateRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(name), Rep.Some(certificate))).shaped.<>({r=>import r._; _1.map(_=> CertificateRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(VARCHAR), Length(64,true) */
    val name: Rep[String] = column[String]("name", O.Length(64,varying=true))
    /** Database column certificate SqlType(VARCHAR), Length(256,true) */
    val certificate: Rep[String] = column[String]("certificate", O.Length(256,varying=true))

    /** Uniqueness Index over (name) (database name idx_name) */
    val index1 = index("idx_name", name, unique=true)
  }
  /** Collection-like TableQuery object for table Certificate */
  lazy val Certificate = new TableQuery(tag => new Certificate(tag))

  /** Entity class storing rows of table Filestore
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(VARCHAR), Length(64,true)
   *  @param unitTypeId Database column unit_type_id SqlType(INT)
   *  @param `type` Database column type SqlType(VARCHAR), Length(64,true), Default(SOFTWARE)
   *  @param description Database column description SqlType(VARCHAR), Length(2000,true), Default(None)
   *  @param version Database column version SqlType(VARCHAR), Length(64,true)
   *  @param content Database column content SqlType(LONGBLOB)
   *  @param timestamp Database column timestamp_ SqlType(DATETIME)
   *  @param targetName Database column target_name SqlType(VARCHAR), Length(128,true), Default(None)
   *  @param owner Database column owner SqlType(INT), Default(None) */
  case class FilestoreRow(id: Int, name: String, unitTypeId: Int, `type`: String = "SOFTWARE", description: Option[String] = None, version: String, content: java.sql.Blob, timestamp: java.sql.Timestamp, targetName: Option[String] = None, owner: Option[Int] = None)
  /** GetResult implicit for fetching FilestoreRow objects using plain SQL queries */
  implicit def GetResultFilestoreRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[String]], e3: GR[java.sql.Blob], e4: GR[java.sql.Timestamp], e5: GR[Option[Int]]): GR[FilestoreRow] = GR{
    prs => import prs._
    FilestoreRow.tupled((<<[Int], <<[String], <<[Int], <<[String], <<?[String], <<[String], <<[java.sql.Blob], <<[java.sql.Timestamp], <<?[String], <<?[Int]))
  }
  /** Table description of table filestore. Objects of this class serve as prototypes for rows in queries.
   *  NOTE: The following names collided with Scala keywords and were escaped: type */
  class Filestore(_tableTag: Tag) extends profile.api.Table[FilestoreRow](_tableTag, Some("acs"), "filestore") {
    def * = (id, name, unitTypeId, `type`, description, version, content, timestamp, targetName, owner) <> (FilestoreRow.tupled, FilestoreRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(name), Rep.Some(unitTypeId), Rep.Some(`type`), description, Rep.Some(version), Rep.Some(content), Rep.Some(timestamp), targetName, owner)).shaped.<>({r=>import r._; _1.map(_=> FilestoreRow.tupled((_1.get, _2.get, _3.get, _4.get, _5, _6.get, _7.get, _8.get, _9, _10)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(VARCHAR), Length(64,true) */
    val name: Rep[String] = column[String]("name", O.Length(64,varying=true))
    /** Database column unit_type_id SqlType(INT) */
    val unitTypeId: Rep[Int] = column[Int]("unit_type_id")
    /** Database column type SqlType(VARCHAR), Length(64,true), Default(SOFTWARE)
     *  NOTE: The name was escaped because it collided with a Scala keyword. */
    val `type`: Rep[String] = column[String]("type", O.Length(64,varying=true), O.Default("SOFTWARE"))
    /** Database column description SqlType(VARCHAR), Length(2000,true), Default(None) */
    val description: Rep[Option[String]] = column[Option[String]]("description", O.Length(2000,varying=true), O.Default(None))
    /** Database column version SqlType(VARCHAR), Length(64,true) */
    val version: Rep[String] = column[String]("version", O.Length(64,varying=true))
    /** Database column content SqlType(LONGBLOB) */
    val content: Rep[java.sql.Blob] = column[java.sql.Blob]("content")
    /** Database column timestamp_ SqlType(DATETIME) */
    val timestamp: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("timestamp_")
    /** Database column target_name SqlType(VARCHAR), Length(128,true), Default(None) */
    val targetName: Rep[Option[String]] = column[Option[String]]("target_name", O.Length(128,varying=true), O.Default(None))
    /** Database column owner SqlType(INT), Default(None) */
    val owner: Rep[Option[Int]] = column[Option[Int]]("owner", O.Default(None))

    /** Foreign key referencing UnitType (database name fk_filestore_unit_type_id) */
    lazy val unitTypeFk = foreignKey("fk_filestore_unit_type_id", unitTypeId, UnitType)(r => r.unitTypeId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing User (database name fk_filestore_owner) */
    lazy val userFk = foreignKey("fk_filestore_owner", owner, User)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

    /** Uniqueness Index over (unitTypeId,name) (database name idx_filestore_utpid_name) */
    val index1 = index("idx_filestore_utpid_name", (unitTypeId, name), unique=true)
    /** Uniqueness Index over (unitTypeId,`type`,version) (database name idx_utpid_t_st_v) */
    val index2 = index("idx_utpid_t_st_v", (unitTypeId, `type`, version), unique=true)
  }
  /** Collection-like TableQuery object for table Filestore */
  lazy val Filestore = new TableQuery(tag => new Filestore(tag))

  /** Entity class storing rows of table Group
   *  @param groupId Database column group_id SqlType(INT), AutoInc, PrimaryKey
   *  @param unitTypeId Database column unit_type_id SqlType(INT)
   *  @param groupName Database column group_name SqlType(VARCHAR), Length(64,true)
   *  @param description Database column description SqlType(VARCHAR), Length(2000,true), Default(None)
   *  @param parentGroupId Database column parent_group_id SqlType(INT), Default(None)
   *  @param profileId Database column profile_id SqlType(INT), Default(None)
   *  @param count Database column count SqlType(INT), Default(None)
   *  @param timeParamId Database column time_param_id SqlType(INT), Default(None)
   *  @param timeRollingRule Database column time_rolling_rule SqlType(VARCHAR), Length(32,true), Default(None) */
  case class GroupRow(groupId: Int, unitTypeId: Int, groupName: String, description: Option[String] = None, parentGroupId: Option[Int] = None, profileId: Option[Int] = None, count: Option[Int] = None, timeParamId: Option[Int] = None, timeRollingRule: Option[String] = None)
  /** GetResult implicit for fetching GroupRow objects using plain SQL queries */
  implicit def GetResultGroupRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[String]], e3: GR[Option[Int]]): GR[GroupRow] = GR{
    prs => import prs._
    GroupRow.tupled((<<[Int], <<[Int], <<[String], <<?[String], <<?[Int], <<?[Int], <<?[Int], <<?[Int], <<?[String]))
  }
  /** Table description of table group_. Objects of this class serve as prototypes for rows in queries. */
  class Group(_tableTag: Tag) extends profile.api.Table[GroupRow](_tableTag, Some("acs"), "group_") {
    def * = (groupId, unitTypeId, groupName, description, parentGroupId, profileId, count, timeParamId, timeRollingRule) <> (GroupRow.tupled, GroupRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(groupId), Rep.Some(unitTypeId), Rep.Some(groupName), description, parentGroupId, profileId, count, timeParamId, timeRollingRule)).shaped.<>({r=>import r._; _1.map(_=> GroupRow.tupled((_1.get, _2.get, _3.get, _4, _5, _6, _7, _8, _9)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column group_id SqlType(INT), AutoInc, PrimaryKey */
    val groupId: Rep[Int] = column[Int]("group_id", O.AutoInc, O.PrimaryKey)
    /** Database column unit_type_id SqlType(INT) */
    val unitTypeId: Rep[Int] = column[Int]("unit_type_id")
    /** Database column group_name SqlType(VARCHAR), Length(64,true) */
    val groupName: Rep[String] = column[String]("group_name", O.Length(64,varying=true))
    /** Database column description SqlType(VARCHAR), Length(2000,true), Default(None) */
    val description: Rep[Option[String]] = column[Option[String]]("description", O.Length(2000,varying=true), O.Default(None))
    /** Database column parent_group_id SqlType(INT), Default(None) */
    val parentGroupId: Rep[Option[Int]] = column[Option[Int]]("parent_group_id", O.Default(None))
    /** Database column profile_id SqlType(INT), Default(None) */
    val profileId: Rep[Option[Int]] = column[Option[Int]]("profile_id", O.Default(None))
    /** Database column count SqlType(INT), Default(None) */
    val count: Rep[Option[Int]] = column[Option[Int]]("count", O.Default(None))
    /** Database column time_param_id SqlType(INT), Default(None) */
    val timeParamId: Rep[Option[Int]] = column[Option[Int]]("time_param_id", O.Default(None))
    /** Database column time_rolling_rule SqlType(VARCHAR), Length(32,true), Default(None) */
    val timeRollingRule: Rep[Option[String]] = column[Option[String]]("time_rolling_rule", O.Length(32,varying=true), O.Default(None))

    /** Foreign key referencing Group (database name fk_group__group_id) */
    lazy val groupFk = foreignKey("fk_group__group_id", parentGroupId, Group)(r => Rep.Some(r.groupId), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Profile (database name fk_group__profile_id) */
    lazy val profileFk = foreignKey("fk_group__profile_id", profileId, Profile)(r => Rep.Some(r.profileId), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing UnitType (database name fk_group__unit_type_id) */
    lazy val unitTypeFk = foreignKey("fk_group__unit_type_id", unitTypeId, UnitType)(r => r.unitTypeId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing UnitTypeParam (database name fk_time_param_u_t_p_id) */
    lazy val unitTypeParamFk = foreignKey("fk_time_param_u_t_p_id", timeParamId, UnitTypeParam)(r => Rep.Some(r.unitTypeParamId), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Group */
  lazy val Group = new TableQuery(tag => new Group(tag))

  /** Entity class storing rows of table GroupParam
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param groupId Database column group_id SqlType(INT)
   *  @param unitTypeParamId Database column unit_type_param_id SqlType(INT)
   *  @param operator Database column operator SqlType(VARCHAR), Length(2,true), Default(=)
   *  @param dataType Database column data_type SqlType(VARCHAR), Length(32,true), Default(TEXT)
   *  @param value Database column value SqlType(VARCHAR), Length(255,true), Default(None) */
  case class GroupParamRow(id: Int, groupId: Int, unitTypeParamId: Int, operator: String = "=", dataType: String = "TEXT", value: Option[String] = None)
  /** GetResult implicit for fetching GroupParamRow objects using plain SQL queries */
  implicit def GetResultGroupParamRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[String]]): GR[GroupParamRow] = GR{
    prs => import prs._
    GroupParamRow.tupled((<<[Int], <<[Int], <<[Int], <<[String], <<[String], <<?[String]))
  }
  /** Table description of table group_param. Objects of this class serve as prototypes for rows in queries. */
  class GroupParam(_tableTag: Tag) extends profile.api.Table[GroupParamRow](_tableTag, Some("acs"), "group_param") {
    def * = (id, groupId, unitTypeParamId, operator, dataType, value) <> (GroupParamRow.tupled, GroupParamRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(groupId), Rep.Some(unitTypeParamId), Rep.Some(operator), Rep.Some(dataType), value)).shaped.<>({r=>import r._; _1.map(_=> GroupParamRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column group_id SqlType(INT) */
    val groupId: Rep[Int] = column[Int]("group_id")
    /** Database column unit_type_param_id SqlType(INT) */
    val unitTypeParamId: Rep[Int] = column[Int]("unit_type_param_id")
    /** Database column operator SqlType(VARCHAR), Length(2,true), Default(=) */
    val operator: Rep[String] = column[String]("operator", O.Length(2,varying=true), O.Default("="))
    /** Database column data_type SqlType(VARCHAR), Length(32,true), Default(TEXT) */
    val dataType: Rep[String] = column[String]("data_type", O.Length(32,varying=true), O.Default("TEXT"))
    /** Database column value SqlType(VARCHAR), Length(255,true), Default(None) */
    val value: Rep[Option[String]] = column[Option[String]]("value", O.Length(255,varying=true), O.Default(None))

    /** Foreign key referencing Group (database name fk_group_param_group_id) */
    lazy val groupFk = foreignKey("fk_group_param_group_id", groupId, Group)(r => r.groupId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing UnitTypeParam (database name fk_group_param_u_t_p_id) */
    lazy val unitTypeParamFk = foreignKey("fk_group_param_u_t_p_id", unitTypeParamId, UnitTypeParam)(r => r.unitTypeParamId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table GroupParam */
  lazy val GroupParam = new TableQuery(tag => new GroupParam(tag))

  /** Entity class storing rows of table Heartbeat
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(VARCHAR), Length(64,true)
   *  @param unitTypeId Database column unit_type_id SqlType(INT)
   *  @param heartbeatExpression Database column heartbeat_expression SqlType(VARCHAR), Length(64,true)
   *  @param heartbeatGroupId Database column heartbeat_group_id SqlType(INT)
   *  @param heartbeatTimeoutHour Database column heartbeat_timeout_hour SqlType(INT), Default(1) */
  case class HeartbeatRow(id: Int, name: String, unitTypeId: Int, heartbeatExpression: String, heartbeatGroupId: Int, heartbeatTimeoutHour: Int = 1)
  /** GetResult implicit for fetching HeartbeatRow objects using plain SQL queries */
  implicit def GetResultHeartbeatRow(implicit e0: GR[Int], e1: GR[String]): GR[HeartbeatRow] = GR{
    prs => import prs._
    HeartbeatRow.tupled((<<[Int], <<[String], <<[Int], <<[String], <<[Int], <<[Int]))
  }
  /** Table description of table heartbeat. Objects of this class serve as prototypes for rows in queries. */
  class Heartbeat(_tableTag: Tag) extends profile.api.Table[HeartbeatRow](_tableTag, Some("acs"), "heartbeat") {
    def * = (id, name, unitTypeId, heartbeatExpression, heartbeatGroupId, heartbeatTimeoutHour) <> (HeartbeatRow.tupled, HeartbeatRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(name), Rep.Some(unitTypeId), Rep.Some(heartbeatExpression), Rep.Some(heartbeatGroupId), Rep.Some(heartbeatTimeoutHour))).shaped.<>({r=>import r._; _1.map(_=> HeartbeatRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(VARCHAR), Length(64,true) */
    val name: Rep[String] = column[String]("name", O.Length(64,varying=true))
    /** Database column unit_type_id SqlType(INT) */
    val unitTypeId: Rep[Int] = column[Int]("unit_type_id")
    /** Database column heartbeat_expression SqlType(VARCHAR), Length(64,true) */
    val heartbeatExpression: Rep[String] = column[String]("heartbeat_expression", O.Length(64,varying=true))
    /** Database column heartbeat_group_id SqlType(INT) */
    val heartbeatGroupId: Rep[Int] = column[Int]("heartbeat_group_id")
    /** Database column heartbeat_timeout_hour SqlType(INT), Default(1) */
    val heartbeatTimeoutHour: Rep[Int] = column[Int]("heartbeat_timeout_hour", O.Default(1))

    /** Foreign key referencing Group (database name fk_hb_group_id) */
    lazy val groupFk = foreignKey("fk_hb_group_id", heartbeatGroupId, Group)(r => r.groupId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing UnitType (database name fk_hb_unit_type_id) */
    lazy val unitTypeFk = foreignKey("fk_hb_unit_type_id", unitTypeId, UnitType)(r => r.unitTypeId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Heartbeat */
  lazy val Heartbeat = new TableQuery(tag => new Heartbeat(tag))

  /** Entity class storing rows of table Job
   *  @param jobId Database column job_id SqlType(INT), AutoInc, PrimaryKey
   *  @param jobName Database column job_name SqlType(VARCHAR), Length(64,true)
   *  @param jobType Database column job_type SqlType(VARCHAR), Length(32,true)
   *  @param description Database column description SqlType(VARCHAR), Length(2000,true), Default(None)
   *  @param groupId Database column group_id SqlType(INT)
   *  @param unconfirmedTimeout Database column unconfirmed_timeout SqlType(INT)
   *  @param stopRules Database column stop_rules SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param status Database column status SqlType(VARCHAR), Length(32,true)
   *  @param completedNoFailure Database column completed_no_failure SqlType(INT)
   *  @param completedHadFailure Database column completed_had_failure SqlType(INT)
   *  @param confirmedFailed Database column confirmed_failed SqlType(INT)
   *  @param unconfirmedFailed Database column unconfirmed_failed SqlType(INT)
   *  @param startTimestamp Database column start_timestamp SqlType(DATETIME), Default(None)
   *  @param endTimestamp Database column end_timestamp SqlType(DATETIME), Default(None)
   *  @param firmwareId Database column firmware_id SqlType(INT), Default(None)
   *  @param jobIdDependency Database column job_id_dependency SqlType(INT), Default(None)
   *  @param profileId Database column profile_id SqlType(INT), Default(None)
   *  @param repeatCount Database column repeat_count SqlType(INT), Default(None)
   *  @param repeatInterval Database column repeat_interval SqlType(INT), Default(None) */
  case class JobRow(jobId: Int, jobName: String, jobType: String, description: Option[String] = None, groupId: Int, unconfirmedTimeout: Int, stopRules: Option[String] = None, status: String, completedNoFailure: Int, completedHadFailure: Int, confirmedFailed: Int, unconfirmedFailed: Int, startTimestamp: Option[java.sql.Timestamp] = None, endTimestamp: Option[java.sql.Timestamp] = None, firmwareId: Option[Int] = None, jobIdDependency: Option[Int] = None, profileId: Option[Int] = None, repeatCount: Option[Int] = None, repeatInterval: Option[Int] = None)
  /** GetResult implicit for fetching JobRow objects using plain SQL queries */
  implicit def GetResultJobRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[String]], e3: GR[Option[java.sql.Timestamp]], e4: GR[Option[Int]]): GR[JobRow] = GR{
    prs => import prs._
    JobRow.tupled((<<[Int], <<[String], <<[String], <<?[String], <<[Int], <<[Int], <<?[String], <<[String], <<[Int], <<[Int], <<[Int], <<[Int], <<?[java.sql.Timestamp], <<?[java.sql.Timestamp], <<?[Int], <<?[Int], <<?[Int], <<?[Int], <<?[Int]))
  }
  /** Table description of table job. Objects of this class serve as prototypes for rows in queries. */
  class Job(_tableTag: Tag) extends profile.api.Table[JobRow](_tableTag, Some("acs"), "job") {
    def * = (jobId, jobName, jobType, description, groupId, unconfirmedTimeout, stopRules, status, completedNoFailure, completedHadFailure, confirmedFailed, unconfirmedFailed, startTimestamp, endTimestamp, firmwareId, jobIdDependency, profileId, repeatCount, repeatInterval) <> (JobRow.tupled, JobRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(jobId), Rep.Some(jobName), Rep.Some(jobType), description, Rep.Some(groupId), Rep.Some(unconfirmedTimeout), stopRules, Rep.Some(status), Rep.Some(completedNoFailure), Rep.Some(completedHadFailure), Rep.Some(confirmedFailed), Rep.Some(unconfirmedFailed), startTimestamp, endTimestamp, firmwareId, jobIdDependency, profileId, repeatCount, repeatInterval)).shaped.<>({r=>import r._; _1.map(_=> JobRow.tupled((_1.get, _2.get, _3.get, _4, _5.get, _6.get, _7, _8.get, _9.get, _10.get, _11.get, _12.get, _13, _14, _15, _16, _17, _18, _19)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column job_id SqlType(INT), AutoInc, PrimaryKey */
    val jobId: Rep[Int] = column[Int]("job_id", O.AutoInc, O.PrimaryKey)
    /** Database column job_name SqlType(VARCHAR), Length(64,true) */
    val jobName: Rep[String] = column[String]("job_name", O.Length(64,varying=true))
    /** Database column job_type SqlType(VARCHAR), Length(32,true) */
    val jobType: Rep[String] = column[String]("job_type", O.Length(32,varying=true))
    /** Database column description SqlType(VARCHAR), Length(2000,true), Default(None) */
    val description: Rep[Option[String]] = column[Option[String]]("description", O.Length(2000,varying=true), O.Default(None))
    /** Database column group_id SqlType(INT) */
    val groupId: Rep[Int] = column[Int]("group_id")
    /** Database column unconfirmed_timeout SqlType(INT) */
    val unconfirmedTimeout: Rep[Int] = column[Int]("unconfirmed_timeout")
    /** Database column stop_rules SqlType(VARCHAR), Length(255,true), Default(None) */
    val stopRules: Rep[Option[String]] = column[Option[String]]("stop_rules", O.Length(255,varying=true), O.Default(None))
    /** Database column status SqlType(VARCHAR), Length(32,true) */
    val status: Rep[String] = column[String]("status", O.Length(32,varying=true))
    /** Database column completed_no_failure SqlType(INT) */
    val completedNoFailure: Rep[Int] = column[Int]("completed_no_failure")
    /** Database column completed_had_failure SqlType(INT) */
    val completedHadFailure: Rep[Int] = column[Int]("completed_had_failure")
    /** Database column confirmed_failed SqlType(INT) */
    val confirmedFailed: Rep[Int] = column[Int]("confirmed_failed")
    /** Database column unconfirmed_failed SqlType(INT) */
    val unconfirmedFailed: Rep[Int] = column[Int]("unconfirmed_failed")
    /** Database column start_timestamp SqlType(DATETIME), Default(None) */
    val startTimestamp: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("start_timestamp", O.Default(None))
    /** Database column end_timestamp SqlType(DATETIME), Default(None) */
    val endTimestamp: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("end_timestamp", O.Default(None))
    /** Database column firmware_id SqlType(INT), Default(None) */
    val firmwareId: Rep[Option[Int]] = column[Option[Int]]("firmware_id", O.Default(None))
    /** Database column job_id_dependency SqlType(INT), Default(None) */
    val jobIdDependency: Rep[Option[Int]] = column[Option[Int]]("job_id_dependency", O.Default(None))
    /** Database column profile_id SqlType(INT), Default(None) */
    val profileId: Rep[Option[Int]] = column[Option[Int]]("profile_id", O.Default(None))
    /** Database column repeat_count SqlType(INT), Default(None) */
    val repeatCount: Rep[Option[Int]] = column[Option[Int]]("repeat_count", O.Default(None))
    /** Database column repeat_interval SqlType(INT), Default(None) */
    val repeatInterval: Rep[Option[Int]] = column[Option[Int]]("repeat_interval", O.Default(None))

    /** Foreign key referencing Filestore (database name fk_job_firmware_id) */
    lazy val filestoreFk = foreignKey("fk_job_firmware_id", firmwareId, Filestore)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Group (database name fk_job_group_id) */
    lazy val groupFk = foreignKey("fk_job_group_id", groupId, Group)(r => r.groupId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Job (database name fk_job_job_id) */
    lazy val jobFk = foreignKey("fk_job_job_id", jobIdDependency, Job)(r => Rep.Some(r.jobId), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Profile (database name fk_job_profile_id) */
    lazy val profileFk = foreignKey("fk_job_profile_id", profileId, Profile)(r => Rep.Some(r.profileId), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Job */
  lazy val Job = new TableQuery(tag => new Job(tag))

  /** Entity class storing rows of table JobParam
   *  @param jobId Database column job_id SqlType(INT)
   *  @param unitId Database column unit_id SqlType(VARCHAR), Length(64,true)
   *  @param unitTypeParamId Database column unit_type_param_id SqlType(INT)
   *  @param value Database column value SqlType(VARCHAR), Length(255,true), Default(None) */
  case class JobParamRow(jobId: Int, unitId: String, unitTypeParamId: Int, value: Option[String] = None)
  /** GetResult implicit for fetching JobParamRow objects using plain SQL queries */
  implicit def GetResultJobParamRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[String]]): GR[JobParamRow] = GR{
    prs => import prs._
    JobParamRow.tupled((<<[Int], <<[String], <<[Int], <<?[String]))
  }
  /** Table description of table job_param. Objects of this class serve as prototypes for rows in queries. */
  class JobParam(_tableTag: Tag) extends profile.api.Table[JobParamRow](_tableTag, Some("acs"), "job_param") {
    def * = (jobId, unitId, unitTypeParamId, value) <> (JobParamRow.tupled, JobParamRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(jobId), Rep.Some(unitId), Rep.Some(unitTypeParamId), value)).shaped.<>({r=>import r._; _1.map(_=> JobParamRow.tupled((_1.get, _2.get, _3.get, _4)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column job_id SqlType(INT) */
    val jobId: Rep[Int] = column[Int]("job_id")
    /** Database column unit_id SqlType(VARCHAR), Length(64,true) */
    val unitId: Rep[String] = column[String]("unit_id", O.Length(64,varying=true))
    /** Database column unit_type_param_id SqlType(INT) */
    val unitTypeParamId: Rep[Int] = column[Int]("unit_type_param_id")
    /** Database column value SqlType(VARCHAR), Length(255,true), Default(None) */
    val value: Rep[Option[String]] = column[Option[String]]("value", O.Length(255,varying=true), O.Default(None))

    /** Primary key of JobParam (database name job_param_PK) */
    val pk = primaryKey("job_param_PK", (jobId, unitId, unitTypeParamId))

    /** Foreign key referencing Job (database name fk_job_param_job_id) */
    lazy val jobFk = foreignKey("fk_job_param_job_id", jobId, Job)(r => r.jobId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing UnitTypeParam (database name fk_job_param_u_t_p_id) */
    lazy val unitTypeParamFk = foreignKey("fk_job_param_u_t_p_id", unitTypeParamId, UnitTypeParam)(r => r.unitTypeParamId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table JobParam */
  lazy val JobParam = new TableQuery(tag => new JobParam(tag))

  /** Entity class storing rows of table Message
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param `type` Database column type SqlType(VARCHAR), Length(64,true)
   *  @param sender Database column sender SqlType(VARCHAR), Length(64,true)
   *  @param receiver Database column receiver SqlType(VARCHAR), Length(64,true), Default(None)
   *  @param objectType Database column object_type SqlType(VARCHAR), Length(64,true), Default(None)
   *  @param objectId Database column object_id SqlType(VARCHAR), Length(64,true), Default(None)
   *  @param timestamp Database column timestamp_ SqlType(DATETIME)
   *  @param content Database column content SqlType(VARCHAR), Length(2000,true), Default(None) */
  case class MessageRow(id: Int, `type`: String, sender: String, receiver: Option[String] = None, objectType: Option[String] = None, objectId: Option[String] = None, timestamp: java.sql.Timestamp, content: Option[String] = None)
  /** GetResult implicit for fetching MessageRow objects using plain SQL queries */
  implicit def GetResultMessageRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[String]], e3: GR[java.sql.Timestamp]): GR[MessageRow] = GR{
    prs => import prs._
    MessageRow.tupled((<<[Int], <<[String], <<[String], <<?[String], <<?[String], <<?[String], <<[java.sql.Timestamp], <<?[String]))
  }
  /** Table description of table message. Objects of this class serve as prototypes for rows in queries.
   *  NOTE: The following names collided with Scala keywords and were escaped: type */
  class Message(_tableTag: Tag) extends profile.api.Table[MessageRow](_tableTag, Some("acs"), "message") {
    def * = (id, `type`, sender, receiver, objectType, objectId, timestamp, content) <> (MessageRow.tupled, MessageRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(`type`), Rep.Some(sender), receiver, objectType, objectId, Rep.Some(timestamp), content)).shaped.<>({r=>import r._; _1.map(_=> MessageRow.tupled((_1.get, _2.get, _3.get, _4, _5, _6, _7.get, _8)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column type SqlType(VARCHAR), Length(64,true)
     *  NOTE: The name was escaped because it collided with a Scala keyword. */
    val `type`: Rep[String] = column[String]("type", O.Length(64,varying=true))
    /** Database column sender SqlType(VARCHAR), Length(64,true) */
    val sender: Rep[String] = column[String]("sender", O.Length(64,varying=true))
    /** Database column receiver SqlType(VARCHAR), Length(64,true), Default(None) */
    val receiver: Rep[Option[String]] = column[Option[String]]("receiver", O.Length(64,varying=true), O.Default(None))
    /** Database column object_type SqlType(VARCHAR), Length(64,true), Default(None) */
    val objectType: Rep[Option[String]] = column[Option[String]]("object_type", O.Length(64,varying=true), O.Default(None))
    /** Database column object_id SqlType(VARCHAR), Length(64,true), Default(None) */
    val objectId: Rep[Option[String]] = column[Option[String]]("object_id", O.Length(64,varying=true), O.Default(None))
    /** Database column timestamp_ SqlType(DATETIME) */
    val timestamp: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("timestamp_")
    /** Database column content SqlType(VARCHAR), Length(2000,true), Default(None) */
    val content: Rep[Option[String]] = column[Option[String]]("content", O.Length(2000,varying=true), O.Default(None))
  }
  /** Collection-like TableQuery object for table Message */
  lazy val Message = new TableQuery(tag => new Message(tag))

  /** Entity class storing rows of table MonitorEvent
   *  @param eventId Database column event_id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param moduleName Database column module_name SqlType(VARCHAR), Length(32,true)
   *  @param moduleKey Database column module_key SqlType(VARCHAR), Length(32,true)
   *  @param moduleState Database column module_state SqlType(INT)
   *  @param message Database column message SqlType(VARCHAR), Length(2000,true), Default(None)
   *  @param starttime Database column starttime SqlType(TIMESTAMP)
   *  @param endtime Database column endtime SqlType(TIMESTAMP)
   *  @param lastchecked Database column lastchecked SqlType(TIMESTAMP)
   *  @param url Database column url SqlType(VARCHAR), Length(255,true), Default(None) */
  case class MonitorEventRow(eventId: Long, moduleName: String, moduleKey: String, moduleState: Int, message: Option[String] = None, starttime: java.sql.Timestamp, endtime: java.sql.Timestamp, lastchecked: java.sql.Timestamp, url: Option[String] = None)
  /** GetResult implicit for fetching MonitorEventRow objects using plain SQL queries */
  implicit def GetResultMonitorEventRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Int], e3: GR[Option[String]], e4: GR[java.sql.Timestamp]): GR[MonitorEventRow] = GR{
    prs => import prs._
    MonitorEventRow.tupled((<<[Long], <<[String], <<[String], <<[Int], <<?[String], <<[java.sql.Timestamp], <<[java.sql.Timestamp], <<[java.sql.Timestamp], <<?[String]))
  }
  /** Table description of table monitor_event. Objects of this class serve as prototypes for rows in queries. */
  class MonitorEvent(_tableTag: Tag) extends profile.api.Table[MonitorEventRow](_tableTag, Some("acs"), "monitor_event") {
    def * = (eventId, moduleName, moduleKey, moduleState, message, starttime, endtime, lastchecked, url) <> (MonitorEventRow.tupled, MonitorEventRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(eventId), Rep.Some(moduleName), Rep.Some(moduleKey), Rep.Some(moduleState), message, Rep.Some(starttime), Rep.Some(endtime), Rep.Some(lastchecked), url)).shaped.<>({r=>import r._; _1.map(_=> MonitorEventRow.tupled((_1.get, _2.get, _3.get, _4.get, _5, _6.get, _7.get, _8.get, _9)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column event_id SqlType(BIGINT), AutoInc, PrimaryKey */
    val eventId: Rep[Long] = column[Long]("event_id", O.AutoInc, O.PrimaryKey)
    /** Database column module_name SqlType(VARCHAR), Length(32,true) */
    val moduleName: Rep[String] = column[String]("module_name", O.Length(32,varying=true))
    /** Database column module_key SqlType(VARCHAR), Length(32,true) */
    val moduleKey: Rep[String] = column[String]("module_key", O.Length(32,varying=true))
    /** Database column module_state SqlType(INT) */
    val moduleState: Rep[Int] = column[Int]("module_state")
    /** Database column message SqlType(VARCHAR), Length(2000,true), Default(None) */
    val message: Rep[Option[String]] = column[Option[String]]("message", O.Length(2000,varying=true), O.Default(None))
    /** Database column starttime SqlType(TIMESTAMP) */
    val starttime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("starttime")
    /** Database column endtime SqlType(TIMESTAMP) */
    val endtime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("endtime")
    /** Database column lastchecked SqlType(TIMESTAMP) */
    val lastchecked: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("lastchecked")
    /** Database column url SqlType(VARCHAR), Length(255,true), Default(None) */
    val url: Rep[Option[String]] = column[Option[String]]("url", O.Length(255,varying=true), O.Default(None))

    /** Uniqueness Index over (moduleName,moduleKey) (database name NameAndKey) */
    val index1 = index("NameAndKey", (moduleName, moduleKey), unique=true)
  }
  /** Collection-like TableQuery object for table MonitorEvent */
  lazy val MonitorEvent = new TableQuery(tag => new MonitorEvent(tag))

  /** Entity class storing rows of table Permission
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param userId Database column user_id SqlType(INT)
   *  @param unitTypeId Database column unit_type_id SqlType(INT)
   *  @param profileId Database column profile_id SqlType(INT), Default(None) */
  case class PermissionRow(id: Int, userId: Int, unitTypeId: Int, profileId: Option[Int] = None)
  /** GetResult implicit for fetching PermissionRow objects using plain SQL queries */
  implicit def GetResultPermissionRow(implicit e0: GR[Int], e1: GR[Option[Int]]): GR[PermissionRow] = GR{
    prs => import prs._
    PermissionRow.tupled((<<[Int], <<[Int], <<[Int], <<?[Int]))
  }
  /** Table description of table permission_. Objects of this class serve as prototypes for rows in queries. */
  class Permission(_tableTag: Tag) extends profile.api.Table[PermissionRow](_tableTag, Some("acs"), "permission_") {
    def * = (id, userId, unitTypeId, profileId) <> (PermissionRow.tupled, PermissionRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(userId), Rep.Some(unitTypeId), profileId)).shaped.<>({r=>import r._; _1.map(_=> PermissionRow.tupled((_1.get, _2.get, _3.get, _4)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column user_id SqlType(INT) */
    val userId: Rep[Int] = column[Int]("user_id")
    /** Database column unit_type_id SqlType(INT) */
    val unitTypeId: Rep[Int] = column[Int]("unit_type_id")
    /** Database column profile_id SqlType(INT), Default(None) */
    val profileId: Rep[Option[Int]] = column[Option[Int]]("profile_id", O.Default(None))

    /** Foreign key referencing UnitType (database name fk_permission_unit_type_id) */
    lazy val unitTypeFk = foreignKey("fk_permission_unit_type_id", unitTypeId, UnitType)(r => r.unitTypeId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing User (database name fk_permission_user_id) */
    lazy val userFk = foreignKey("fk_permission_user_id", userId, User)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

    /** Uniqueness Index over (userId,unitTypeId,profileId) (database name idx_uid_utpid_pid) */
    val index1 = index("idx_uid_utpid_pid", (userId, unitTypeId, profileId), unique=true)
  }
  /** Collection-like TableQuery object for table Permission */
  lazy val Permission = new TableQuery(tag => new Permission(tag))

  /** Entity class storing rows of table Profile
   *  @param profileId Database column profile_id SqlType(INT), AutoInc, PrimaryKey
   *  @param unitTypeId Database column unit_type_id SqlType(INT)
   *  @param profileName Database column profile_name SqlType(VARCHAR), Length(64,true) */
  case class ProfileRow(profileId: Int, unitTypeId: Int, profileName: String)
  /** GetResult implicit for fetching ProfileRow objects using plain SQL queries */
  implicit def GetResultProfileRow(implicit e0: GR[Int], e1: GR[String]): GR[ProfileRow] = GR{
    prs => import prs._
    ProfileRow.tupled((<<[Int], <<[Int], <<[String]))
  }
  /** Table description of table profile. Objects of this class serve as prototypes for rows in queries. */
  class Profile(_tableTag: Tag) extends profile.api.Table[ProfileRow](_tableTag, Some("acs"), "profile") {
    def * = (profileId, unitTypeId, profileName) <> (ProfileRow.tupled, ProfileRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(profileId), Rep.Some(unitTypeId), Rep.Some(profileName))).shaped.<>({r=>import r._; _1.map(_=> ProfileRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column profile_id SqlType(INT), AutoInc, PrimaryKey */
    val profileId: Rep[Int] = column[Int]("profile_id", O.AutoInc, O.PrimaryKey)
    /** Database column unit_type_id SqlType(INT) */
    val unitTypeId: Rep[Int] = column[Int]("unit_type_id")
    /** Database column profile_name SqlType(VARCHAR), Length(64,true) */
    val profileName: Rep[String] = column[String]("profile_name", O.Length(64,varying=true))

    /** Foreign key referencing UnitType (database name fk_profile_unit_type_id) */
    lazy val unitTypeFk = foreignKey("fk_profile_unit_type_id", unitTypeId, UnitType)(r => r.unitTypeId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

    /** Uniqueness Index over (unitTypeId,profileName) (database name idx_unit_type_id_profile_name) */
    val index1 = index("idx_unit_type_id_profile_name", (unitTypeId, profileName), unique=true)
  }
  /** Collection-like TableQuery object for table Profile */
  lazy val Profile = new TableQuery(tag => new Profile(tag))

  /** Entity class storing rows of table ProfileParam
   *  @param profileId Database column profile_id SqlType(INT)
   *  @param unitTypeParamId Database column unit_type_param_id SqlType(INT)
   *  @param value Database column value SqlType(VARCHAR), Length(255,true), Default(None) */
  case class ProfileParamRow(profileId: Int, unitTypeParamId: Int, value: Option[String] = None)
  /** GetResult implicit for fetching ProfileParamRow objects using plain SQL queries */
  implicit def GetResultProfileParamRow(implicit e0: GR[Int], e1: GR[Option[String]]): GR[ProfileParamRow] = GR{
    prs => import prs._
    ProfileParamRow.tupled((<<[Int], <<[Int], <<?[String]))
  }
  /** Table description of table profile_param. Objects of this class serve as prototypes for rows in queries. */
  class ProfileParam(_tableTag: Tag) extends profile.api.Table[ProfileParamRow](_tableTag, Some("acs"), "profile_param") {
    def * = (profileId, unitTypeParamId, value) <> (ProfileParamRow.tupled, ProfileParamRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(profileId), Rep.Some(unitTypeParamId), value)).shaped.<>({r=>import r._; _1.map(_=> ProfileParamRow.tupled((_1.get, _2.get, _3)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column profile_id SqlType(INT) */
    val profileId: Rep[Int] = column[Int]("profile_id")
    /** Database column unit_type_param_id SqlType(INT) */
    val unitTypeParamId: Rep[Int] = column[Int]("unit_type_param_id")
    /** Database column value SqlType(VARCHAR), Length(255,true), Default(None) */
    val value: Rep[Option[String]] = column[Option[String]]("value", O.Length(255,varying=true), O.Default(None))

    /** Primary key of ProfileParam (database name profile_param_PK) */
    val pk = primaryKey("profile_param_PK", (profileId, unitTypeParamId))

    /** Foreign key referencing Profile (database name fk_profile_param_profile_id) */
    lazy val profileFk = foreignKey("fk_profile_param_profile_id", profileId, Profile)(r => r.profileId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing UnitTypeParam (database name fk_profile_param_u_t_p_id) */
    lazy val unitTypeParamFk = foreignKey("fk_profile_param_u_t_p_id", unitTypeParamId, UnitTypeParam)(r => r.unitTypeParamId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table ProfileParam */
  lazy val ProfileParam = new TableQuery(tag => new ProfileParam(tag))

  /** Entity class storing rows of table ReportGatewayTr
   *  @param timestamp Database column timestamp_ SqlType(DATETIME)
   *  @param periodType Database column period_type SqlType(INT)
   *  @param unitTypeName Database column unit_type_name SqlType(VARCHAR), Length(64,true)
   *  @param profileName Database column profile_name SqlType(VARCHAR), Length(64,true)
   *  @param softwareVersion Database column software_version SqlType(VARCHAR), Length(64,true)
   *  @param pingSuccessCountAvg Database column ping_success_count_avg SqlType(INT), Default(None)
   *  @param pingFailureCountAvg Database column ping_failure_count_avg SqlType(INT), Default(None)
   *  @param pingResponseTimeAvg Database column ping_response_time_avg SqlType(INT), Default(None)
   *  @param downloadSpeedAvg Database column download_speed_avg SqlType(INT), Default(None)
   *  @param uploadSpeedAvg Database column upload_speed_avg SqlType(INT), Default(None)
   *  @param wanUptimeAvg Database column wan_uptime_avg SqlType(INT), Default(None) */
  case class ReportGatewayTrRow(timestamp: java.sql.Timestamp, periodType: Int, unitTypeName: String, profileName: String, softwareVersion: String, pingSuccessCountAvg: Option[Int] = None, pingFailureCountAvg: Option[Int] = None, pingResponseTimeAvg: Option[Int] = None, downloadSpeedAvg: Option[Int] = None, uploadSpeedAvg: Option[Int] = None, wanUptimeAvg: Option[Int] = None)
  /** GetResult implicit for fetching ReportGatewayTrRow objects using plain SQL queries */
  implicit def GetResultReportGatewayTrRow(implicit e0: GR[java.sql.Timestamp], e1: GR[Int], e2: GR[String], e3: GR[Option[Int]]): GR[ReportGatewayTrRow] = GR{
    prs => import prs._
    ReportGatewayTrRow.tupled((<<[java.sql.Timestamp], <<[Int], <<[String], <<[String], <<[String], <<?[Int], <<?[Int], <<?[Int], <<?[Int], <<?[Int], <<?[Int]))
  }
  /** Table description of table report_gateway_tr. Objects of this class serve as prototypes for rows in queries. */
  class ReportGatewayTr(_tableTag: Tag) extends profile.api.Table[ReportGatewayTrRow](_tableTag, Some("acs"), "report_gateway_tr") {
    def * = (timestamp, periodType, unitTypeName, profileName, softwareVersion, pingSuccessCountAvg, pingFailureCountAvg, pingResponseTimeAvg, downloadSpeedAvg, uploadSpeedAvg, wanUptimeAvg) <> (ReportGatewayTrRow.tupled, ReportGatewayTrRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(timestamp), Rep.Some(periodType), Rep.Some(unitTypeName), Rep.Some(profileName), Rep.Some(softwareVersion), pingSuccessCountAvg, pingFailureCountAvg, pingResponseTimeAvg, downloadSpeedAvg, uploadSpeedAvg, wanUptimeAvg)).shaped.<>({r=>import r._; _1.map(_=> ReportGatewayTrRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6, _7, _8, _9, _10, _11)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column timestamp_ SqlType(DATETIME) */
    val timestamp: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("timestamp_")
    /** Database column period_type SqlType(INT) */
    val periodType: Rep[Int] = column[Int]("period_type")
    /** Database column unit_type_name SqlType(VARCHAR), Length(64,true) */
    val unitTypeName: Rep[String] = column[String]("unit_type_name", O.Length(64,varying=true))
    /** Database column profile_name SqlType(VARCHAR), Length(64,true) */
    val profileName: Rep[String] = column[String]("profile_name", O.Length(64,varying=true))
    /** Database column software_version SqlType(VARCHAR), Length(64,true) */
    val softwareVersion: Rep[String] = column[String]("software_version", O.Length(64,varying=true))
    /** Database column ping_success_count_avg SqlType(INT), Default(None) */
    val pingSuccessCountAvg: Rep[Option[Int]] = column[Option[Int]]("ping_success_count_avg", O.Default(None))
    /** Database column ping_failure_count_avg SqlType(INT), Default(None) */
    val pingFailureCountAvg: Rep[Option[Int]] = column[Option[Int]]("ping_failure_count_avg", O.Default(None))
    /** Database column ping_response_time_avg SqlType(INT), Default(None) */
    val pingResponseTimeAvg: Rep[Option[Int]] = column[Option[Int]]("ping_response_time_avg", O.Default(None))
    /** Database column download_speed_avg SqlType(INT), Default(None) */
    val downloadSpeedAvg: Rep[Option[Int]] = column[Option[Int]]("download_speed_avg", O.Default(None))
    /** Database column upload_speed_avg SqlType(INT), Default(None) */
    val uploadSpeedAvg: Rep[Option[Int]] = column[Option[Int]]("upload_speed_avg", O.Default(None))
    /** Database column wan_uptime_avg SqlType(INT), Default(None) */
    val wanUptimeAvg: Rep[Option[Int]] = column[Option[Int]]("wan_uptime_avg", O.Default(None))

    /** Primary key of ReportGatewayTr (database name report_gateway_tr_PK) */
    val pk = primaryKey("report_gateway_tr_PK", (timestamp, periodType, unitTypeName, profileName, softwareVersion))
  }
  /** Collection-like TableQuery object for table ReportGatewayTr */
  lazy val ReportGatewayTr = new TableQuery(tag => new ReportGatewayTr(tag))

  /** Entity class storing rows of table ReportGroup
   *  @param timestamp Database column timestamp_ SqlType(DATETIME)
   *  @param periodType Database column period_type SqlType(INT)
   *  @param unitTypeName Database column unit_type_name SqlType(VARCHAR), Length(64,true)
   *  @param groupName Database column group_name SqlType(VARCHAR), Length(64,true)
   *  @param unitCount Database column unit_count SqlType(INT) */
  case class ReportGroupRow(timestamp: java.sql.Timestamp, periodType: Int, unitTypeName: String, groupName: String, unitCount: Int)
  /** GetResult implicit for fetching ReportGroupRow objects using plain SQL queries */
  implicit def GetResultReportGroupRow(implicit e0: GR[java.sql.Timestamp], e1: GR[Int], e2: GR[String]): GR[ReportGroupRow] = GR{
    prs => import prs._
    ReportGroupRow.tupled((<<[java.sql.Timestamp], <<[Int], <<[String], <<[String], <<[Int]))
  }
  /** Table description of table report_group. Objects of this class serve as prototypes for rows in queries. */
  class ReportGroup(_tableTag: Tag) extends profile.api.Table[ReportGroupRow](_tableTag, Some("acs"), "report_group") {
    def * = (timestamp, periodType, unitTypeName, groupName, unitCount) <> (ReportGroupRow.tupled, ReportGroupRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(timestamp), Rep.Some(periodType), Rep.Some(unitTypeName), Rep.Some(groupName), Rep.Some(unitCount))).shaped.<>({r=>import r._; _1.map(_=> ReportGroupRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column timestamp_ SqlType(DATETIME) */
    val timestamp: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("timestamp_")
    /** Database column period_type SqlType(INT) */
    val periodType: Rep[Int] = column[Int]("period_type")
    /** Database column unit_type_name SqlType(VARCHAR), Length(64,true) */
    val unitTypeName: Rep[String] = column[String]("unit_type_name", O.Length(64,varying=true))
    /** Database column group_name SqlType(VARCHAR), Length(64,true) */
    val groupName: Rep[String] = column[String]("group_name", O.Length(64,varying=true))
    /** Database column unit_count SqlType(INT) */
    val unitCount: Rep[Int] = column[Int]("unit_count")

    /** Primary key of ReportGroup (database name report_group_PK) */
    val pk = primaryKey("report_group_PK", (timestamp, periodType, unitTypeName, groupName))
  }
  /** Collection-like TableQuery object for table ReportGroup */
  lazy val ReportGroup = new TableQuery(tag => new ReportGroup(tag))

  /** Entity class storing rows of table ReportHw
   *  @param timestamp Database column timestamp_ SqlType(DATETIME)
   *  @param periodType Database column period_type SqlType(INT)
   *  @param unitTypeName Database column unit_type_name SqlType(VARCHAR), Length(64,true)
   *  @param profileName Database column profile_name SqlType(VARCHAR), Length(64,true)
   *  @param softwareVersion Database column software_version SqlType(VARCHAR), Length(64,true)
   *  @param bootCount Database column boot_count SqlType(INT)
   *  @param bootWatchdogCount Database column boot_watchdog_count SqlType(INT)
   *  @param bootMiscCount Database column boot_misc_count SqlType(INT)
   *  @param bootPowerCount Database column boot_power_count SqlType(INT)
   *  @param bootResetCount Database column boot_reset_count SqlType(INT)
   *  @param bootProvCount Database column boot_prov_count SqlType(INT)
   *  @param bootProvSwCount Database column boot_prov_sw_count SqlType(INT)
   *  @param bootProvConfCount Database column boot_prov_conf_count SqlType(INT)
   *  @param bootProvBootCount Database column boot_prov_boot_count SqlType(INT)
   *  @param bootUserCount Database column boot_user_count SqlType(INT)
   *  @param memHeapDdrPoolAvg Database column mem_heap_ddr_pool_avg SqlType(INT), Default(None)
   *  @param memHeapDdrCurrentAvg Database column mem_heap_ddr_current_avg SqlType(INT), Default(None)
   *  @param memHeapDdrLowAvg Database column mem_heap_ddr_low_avg SqlType(INT), Default(None)
   *  @param memHeapOcmPoolAvg Database column mem_heap_ocm_pool_avg SqlType(INT), Default(None)
   *  @param memHeapOcmCurrentAvg Database column mem_heap_ocm_current_avg SqlType(INT), Default(None)
   *  @param memHeapOcmLowAvg Database column mem_heap_ocm_low_avg SqlType(INT), Default(None)
   *  @param memNpDdrPoolAvg Database column mem_np_ddr_pool_avg SqlType(INT), Default(None)
   *  @param memNpDdrCurrentAvg Database column mem_np_ddr_current_avg SqlType(INT), Default(None)
   *  @param memNpDdrLowAvg Database column mem_np_ddr_low_avg SqlType(INT), Default(None)
   *  @param memNpOcmPoolAvg Database column mem_np_ocm_pool_avg SqlType(INT), Default(None)
   *  @param memNpOcmCurrentAvg Database column mem_np_ocm_current_avg SqlType(INT), Default(None)
   *  @param memNpOcmLowAvg Database column mem_np_ocm_low_avg SqlType(INT), Default(None)
   *  @param cpeUptimeAvg Database column cpe_uptime_avg SqlType(INT), Default(None) */
  case class ReportHwRow(timestamp: java.sql.Timestamp, periodType: Int, unitTypeName: String, profileName: String, softwareVersion: String, bootCount: Int, bootWatchdogCount: Int, bootMiscCount: Int, bootPowerCount: Int, bootResetCount: Int, bootProvCount: Int, bootProvSwCount: Int, bootProvConfCount: Int, bootProvBootCount: Int, bootUserCount: Int, memHeapDdrPoolAvg: Option[Int] = None, memHeapDdrCurrentAvg: Option[Int] = None, memHeapDdrLowAvg: Option[Int] = None, memHeapOcmPoolAvg: Option[Int] = None, memHeapOcmCurrentAvg: Option[Int] = None, memHeapOcmLowAvg: Option[Int] = None, memNpDdrPoolAvg: Option[Int] = None, memNpDdrCurrentAvg: Option[Int] = None, memNpDdrLowAvg: Option[Int] = None, memNpOcmPoolAvg: Option[Int] = None, memNpOcmCurrentAvg: Option[Int] = None, memNpOcmLowAvg: Option[Int] = None, cpeUptimeAvg: Option[Int] = None)
  /** GetResult implicit for fetching ReportHwRow objects using plain SQL queries */
  implicit def GetResultReportHwRow(implicit e0: GR[java.sql.Timestamp], e1: GR[Int], e2: GR[String], e3: GR[Option[Int]]): GR[ReportHwRow] = GR{
    prs => import prs._
    ReportHwRow(<<[java.sql.Timestamp], <<[Int], <<[String], <<[String], <<[String], <<[Int], <<[Int], <<[Int], <<[Int], <<[Int], <<[Int], <<[Int], <<[Int], <<[Int], <<[Int], <<?[Int], <<?[Int], <<?[Int], <<?[Int], <<?[Int], <<?[Int], <<?[Int], <<?[Int], <<?[Int], <<?[Int], <<?[Int], <<?[Int], <<?[Int])
  }
  /** Table description of table report_hw. Objects of this class serve as prototypes for rows in queries. */
  class ReportHw(_tableTag: Tag) extends profile.api.Table[ReportHwRow](_tableTag, Some("acs"), "report_hw") {
    def * = (timestamp :: periodType :: unitTypeName :: profileName :: softwareVersion :: bootCount :: bootWatchdogCount :: bootMiscCount :: bootPowerCount :: bootResetCount :: bootProvCount :: bootProvSwCount :: bootProvConfCount :: bootProvBootCount :: bootUserCount :: memHeapDdrPoolAvg :: memHeapDdrCurrentAvg :: memHeapDdrLowAvg :: memHeapOcmPoolAvg :: memHeapOcmCurrentAvg :: memHeapOcmLowAvg :: memNpDdrPoolAvg :: memNpDdrCurrentAvg :: memNpDdrLowAvg :: memNpOcmPoolAvg :: memNpOcmCurrentAvg :: memNpOcmLowAvg :: cpeUptimeAvg :: HNil).mapTo[ReportHwRow]
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(timestamp) :: Rep.Some(periodType) :: Rep.Some(unitTypeName) :: Rep.Some(profileName) :: Rep.Some(softwareVersion) :: Rep.Some(bootCount) :: Rep.Some(bootWatchdogCount) :: Rep.Some(bootMiscCount) :: Rep.Some(bootPowerCount) :: Rep.Some(bootResetCount) :: Rep.Some(bootProvCount) :: Rep.Some(bootProvSwCount) :: Rep.Some(bootProvConfCount) :: Rep.Some(bootProvBootCount) :: Rep.Some(bootUserCount) :: memHeapDdrPoolAvg :: memHeapDdrCurrentAvg :: memHeapDdrLowAvg :: memHeapOcmPoolAvg :: memHeapOcmCurrentAvg :: memHeapOcmLowAvg :: memNpDdrPoolAvg :: memNpDdrCurrentAvg :: memNpDdrLowAvg :: memNpOcmPoolAvg :: memNpOcmCurrentAvg :: memNpOcmLowAvg :: cpeUptimeAvg :: HNil).shaped.<>(r => ReportHwRow(r(0).asInstanceOf[Option[java.sql.Timestamp]].get, r(1).asInstanceOf[Option[Int]].get, r(2).asInstanceOf[Option[String]].get, r(3).asInstanceOf[Option[String]].get, r(4).asInstanceOf[Option[String]].get, r(5).asInstanceOf[Option[Int]].get, r(6).asInstanceOf[Option[Int]].get, r(7).asInstanceOf[Option[Int]].get, r(8).asInstanceOf[Option[Int]].get, r(9).asInstanceOf[Option[Int]].get, r(10).asInstanceOf[Option[Int]].get, r(11).asInstanceOf[Option[Int]].get, r(12).asInstanceOf[Option[Int]].get, r(13).asInstanceOf[Option[Int]].get, r(14).asInstanceOf[Option[Int]].get, r(15).asInstanceOf[Option[Int]], r(16).asInstanceOf[Option[Int]], r(17).asInstanceOf[Option[Int]], r(18).asInstanceOf[Option[Int]], r(19).asInstanceOf[Option[Int]], r(20).asInstanceOf[Option[Int]], r(21).asInstanceOf[Option[Int]], r(22).asInstanceOf[Option[Int]], r(23).asInstanceOf[Option[Int]], r(24).asInstanceOf[Option[Int]], r(25).asInstanceOf[Option[Int]], r(26).asInstanceOf[Option[Int]], r(27).asInstanceOf[Option[Int]]), (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column timestamp_ SqlType(DATETIME) */
    val timestamp: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("timestamp_")
    /** Database column period_type SqlType(INT) */
    val periodType: Rep[Int] = column[Int]("period_type")
    /** Database column unit_type_name SqlType(VARCHAR), Length(64,true) */
    val unitTypeName: Rep[String] = column[String]("unit_type_name", O.Length(64,varying=true))
    /** Database column profile_name SqlType(VARCHAR), Length(64,true) */
    val profileName: Rep[String] = column[String]("profile_name", O.Length(64,varying=true))
    /** Database column software_version SqlType(VARCHAR), Length(64,true) */
    val softwareVersion: Rep[String] = column[String]("software_version", O.Length(64,varying=true))
    /** Database column boot_count SqlType(INT) */
    val bootCount: Rep[Int] = column[Int]("boot_count")
    /** Database column boot_watchdog_count SqlType(INT) */
    val bootWatchdogCount: Rep[Int] = column[Int]("boot_watchdog_count")
    /** Database column boot_misc_count SqlType(INT) */
    val bootMiscCount: Rep[Int] = column[Int]("boot_misc_count")
    /** Database column boot_power_count SqlType(INT) */
    val bootPowerCount: Rep[Int] = column[Int]("boot_power_count")
    /** Database column boot_reset_count SqlType(INT) */
    val bootResetCount: Rep[Int] = column[Int]("boot_reset_count")
    /** Database column boot_prov_count SqlType(INT) */
    val bootProvCount: Rep[Int] = column[Int]("boot_prov_count")
    /** Database column boot_prov_sw_count SqlType(INT) */
    val bootProvSwCount: Rep[Int] = column[Int]("boot_prov_sw_count")
    /** Database column boot_prov_conf_count SqlType(INT) */
    val bootProvConfCount: Rep[Int] = column[Int]("boot_prov_conf_count")
    /** Database column boot_prov_boot_count SqlType(INT) */
    val bootProvBootCount: Rep[Int] = column[Int]("boot_prov_boot_count")
    /** Database column boot_user_count SqlType(INT) */
    val bootUserCount: Rep[Int] = column[Int]("boot_user_count")
    /** Database column mem_heap_ddr_pool_avg SqlType(INT), Default(None) */
    val memHeapDdrPoolAvg: Rep[Option[Int]] = column[Option[Int]]("mem_heap_ddr_pool_avg", O.Default(None))
    /** Database column mem_heap_ddr_current_avg SqlType(INT), Default(None) */
    val memHeapDdrCurrentAvg: Rep[Option[Int]] = column[Option[Int]]("mem_heap_ddr_current_avg", O.Default(None))
    /** Database column mem_heap_ddr_low_avg SqlType(INT), Default(None) */
    val memHeapDdrLowAvg: Rep[Option[Int]] = column[Option[Int]]("mem_heap_ddr_low_avg", O.Default(None))
    /** Database column mem_heap_ocm_pool_avg SqlType(INT), Default(None) */
    val memHeapOcmPoolAvg: Rep[Option[Int]] = column[Option[Int]]("mem_heap_ocm_pool_avg", O.Default(None))
    /** Database column mem_heap_ocm_current_avg SqlType(INT), Default(None) */
    val memHeapOcmCurrentAvg: Rep[Option[Int]] = column[Option[Int]]("mem_heap_ocm_current_avg", O.Default(None))
    /** Database column mem_heap_ocm_low_avg SqlType(INT), Default(None) */
    val memHeapOcmLowAvg: Rep[Option[Int]] = column[Option[Int]]("mem_heap_ocm_low_avg", O.Default(None))
    /** Database column mem_np_ddr_pool_avg SqlType(INT), Default(None) */
    val memNpDdrPoolAvg: Rep[Option[Int]] = column[Option[Int]]("mem_np_ddr_pool_avg", O.Default(None))
    /** Database column mem_np_ddr_current_avg SqlType(INT), Default(None) */
    val memNpDdrCurrentAvg: Rep[Option[Int]] = column[Option[Int]]("mem_np_ddr_current_avg", O.Default(None))
    /** Database column mem_np_ddr_low_avg SqlType(INT), Default(None) */
    val memNpDdrLowAvg: Rep[Option[Int]] = column[Option[Int]]("mem_np_ddr_low_avg", O.Default(None))
    /** Database column mem_np_ocm_pool_avg SqlType(INT), Default(None) */
    val memNpOcmPoolAvg: Rep[Option[Int]] = column[Option[Int]]("mem_np_ocm_pool_avg", O.Default(None))
    /** Database column mem_np_ocm_current_avg SqlType(INT), Default(None) */
    val memNpOcmCurrentAvg: Rep[Option[Int]] = column[Option[Int]]("mem_np_ocm_current_avg", O.Default(None))
    /** Database column mem_np_ocm_low_avg SqlType(INT), Default(None) */
    val memNpOcmLowAvg: Rep[Option[Int]] = column[Option[Int]]("mem_np_ocm_low_avg", O.Default(None))
    /** Database column cpe_uptime_avg SqlType(INT), Default(None) */
    val cpeUptimeAvg: Rep[Option[Int]] = column[Option[Int]]("cpe_uptime_avg", O.Default(None))

    /** Primary key of ReportHw (database name report_hw_PK) */
    val pk = primaryKey("report_hw_PK", timestamp :: periodType :: unitTypeName :: profileName :: softwareVersion :: HNil)
  }
  /** Collection-like TableQuery object for table ReportHw */
  lazy val ReportHw = new TableQuery(tag => new ReportHw(tag))

  /** Entity class storing rows of table ReportHwTr
   *  @param timestamp Database column timestamp_ SqlType(DATETIME)
   *  @param periodType Database column period_type SqlType(INT)
   *  @param unitTypeName Database column unit_type_name SqlType(VARCHAR), Length(64,true)
   *  @param profileName Database column profile_name SqlType(VARCHAR), Length(64,true)
   *  @param softwareVersion Database column software_version SqlType(VARCHAR), Length(64,true)
   *  @param cpeUptimeAvg Database column cpe_uptime_avg SqlType(INT), Default(None)
   *  @param memoryTotalAvg Database column memory_total_avg SqlType(INT), Default(None)
   *  @param memoryFreeAvg Database column memory_free_avg SqlType(INT), Default(None)
   *  @param cpuUsageAvg Database column cpu_usage_avg SqlType(INT), Default(None)
   *  @param processCountAvg Database column process_count_avg SqlType(INT), Default(None)
   *  @param temperatureNowAvg Database column temperature_now_avg SqlType(INT), Default(None)
   *  @param temperatureMaxAvg Database column temperature_max_avg SqlType(INT), Default(None) */
  case class ReportHwTrRow(timestamp: java.sql.Timestamp, periodType: Int, unitTypeName: String, profileName: String, softwareVersion: String, cpeUptimeAvg: Option[Int] = None, memoryTotalAvg: Option[Int] = None, memoryFreeAvg: Option[Int] = None, cpuUsageAvg: Option[Int] = None, processCountAvg: Option[Int] = None, temperatureNowAvg: Option[Int] = None, temperatureMaxAvg: Option[Int] = None)
  /** GetResult implicit for fetching ReportHwTrRow objects using plain SQL queries */
  implicit def GetResultReportHwTrRow(implicit e0: GR[java.sql.Timestamp], e1: GR[Int], e2: GR[String], e3: GR[Option[Int]]): GR[ReportHwTrRow] = GR{
    prs => import prs._
    ReportHwTrRow.tupled((<<[java.sql.Timestamp], <<[Int], <<[String], <<[String], <<[String], <<?[Int], <<?[Int], <<?[Int], <<?[Int], <<?[Int], <<?[Int], <<?[Int]))
  }
  /** Table description of table report_hw_tr. Objects of this class serve as prototypes for rows in queries. */
  class ReportHwTr(_tableTag: Tag) extends profile.api.Table[ReportHwTrRow](_tableTag, Some("acs"), "report_hw_tr") {
    def * = (timestamp, periodType, unitTypeName, profileName, softwareVersion, cpeUptimeAvg, memoryTotalAvg, memoryFreeAvg, cpuUsageAvg, processCountAvg, temperatureNowAvg, temperatureMaxAvg) <> (ReportHwTrRow.tupled, ReportHwTrRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(timestamp), Rep.Some(periodType), Rep.Some(unitTypeName), Rep.Some(profileName), Rep.Some(softwareVersion), cpeUptimeAvg, memoryTotalAvg, memoryFreeAvg, cpuUsageAvg, processCountAvg, temperatureNowAvg, temperatureMaxAvg)).shaped.<>({r=>import r._; _1.map(_=> ReportHwTrRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6, _7, _8, _9, _10, _11, _12)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column timestamp_ SqlType(DATETIME) */
    val timestamp: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("timestamp_")
    /** Database column period_type SqlType(INT) */
    val periodType: Rep[Int] = column[Int]("period_type")
    /** Database column unit_type_name SqlType(VARCHAR), Length(64,true) */
    val unitTypeName: Rep[String] = column[String]("unit_type_name", O.Length(64,varying=true))
    /** Database column profile_name SqlType(VARCHAR), Length(64,true) */
    val profileName: Rep[String] = column[String]("profile_name", O.Length(64,varying=true))
    /** Database column software_version SqlType(VARCHAR), Length(64,true) */
    val softwareVersion: Rep[String] = column[String]("software_version", O.Length(64,varying=true))
    /** Database column cpe_uptime_avg SqlType(INT), Default(None) */
    val cpeUptimeAvg: Rep[Option[Int]] = column[Option[Int]]("cpe_uptime_avg", O.Default(None))
    /** Database column memory_total_avg SqlType(INT), Default(None) */
    val memoryTotalAvg: Rep[Option[Int]] = column[Option[Int]]("memory_total_avg", O.Default(None))
    /** Database column memory_free_avg SqlType(INT), Default(None) */
    val memoryFreeAvg: Rep[Option[Int]] = column[Option[Int]]("memory_free_avg", O.Default(None))
    /** Database column cpu_usage_avg SqlType(INT), Default(None) */
    val cpuUsageAvg: Rep[Option[Int]] = column[Option[Int]]("cpu_usage_avg", O.Default(None))
    /** Database column process_count_avg SqlType(INT), Default(None) */
    val processCountAvg: Rep[Option[Int]] = column[Option[Int]]("process_count_avg", O.Default(None))
    /** Database column temperature_now_avg SqlType(INT), Default(None) */
    val temperatureNowAvg: Rep[Option[Int]] = column[Option[Int]]("temperature_now_avg", O.Default(None))
    /** Database column temperature_max_avg SqlType(INT), Default(None) */
    val temperatureMaxAvg: Rep[Option[Int]] = column[Option[Int]]("temperature_max_avg", O.Default(None))

    /** Primary key of ReportHwTr (database name report_hw_tr_PK) */
    val pk = primaryKey("report_hw_tr_PK", (timestamp, periodType, unitTypeName, profileName, softwareVersion))
  }
  /** Collection-like TableQuery object for table ReportHwTr */
  lazy val ReportHwTr = new TableQuery(tag => new ReportHwTr(tag))

  /** Entity class storing rows of table ReportJob
   *  @param timestamp Database column timestamp_ SqlType(DATETIME)
   *  @param periodType Database column period_type SqlType(INT)
   *  @param unitTypeName Database column unit_type_name SqlType(VARCHAR), Length(64,true)
   *  @param jobName Database column job_name SqlType(VARCHAR), Length(64,true)
   *  @param groupName Database column group_name SqlType(VARCHAR), Length(64,true)
   *  @param groupSize Database column group_size SqlType(INT)
   *  @param completed Database column completed SqlType(INT)
   *  @param confirmedFailed Database column confirmed_failed SqlType(INT)
   *  @param unconfirmedFailed Database column unconfirmed_failed SqlType(INT) */
  case class ReportJobRow(timestamp: java.sql.Timestamp, periodType: Int, unitTypeName: String, jobName: String, groupName: String, groupSize: Int, completed: Int, confirmedFailed: Int, unconfirmedFailed: Int)
  /** GetResult implicit for fetching ReportJobRow objects using plain SQL queries */
  implicit def GetResultReportJobRow(implicit e0: GR[java.sql.Timestamp], e1: GR[Int], e2: GR[String]): GR[ReportJobRow] = GR{
    prs => import prs._
    ReportJobRow.tupled((<<[java.sql.Timestamp], <<[Int], <<[String], <<[String], <<[String], <<[Int], <<[Int], <<[Int], <<[Int]))
  }
  /** Table description of table report_job. Objects of this class serve as prototypes for rows in queries. */
  class ReportJob(_tableTag: Tag) extends profile.api.Table[ReportJobRow](_tableTag, Some("acs"), "report_job") {
    def * = (timestamp, periodType, unitTypeName, jobName, groupName, groupSize, completed, confirmedFailed, unconfirmedFailed) <> (ReportJobRow.tupled, ReportJobRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(timestamp), Rep.Some(periodType), Rep.Some(unitTypeName), Rep.Some(jobName), Rep.Some(groupName), Rep.Some(groupSize), Rep.Some(completed), Rep.Some(confirmedFailed), Rep.Some(unconfirmedFailed))).shaped.<>({r=>import r._; _1.map(_=> ReportJobRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column timestamp_ SqlType(DATETIME) */
    val timestamp: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("timestamp_")
    /** Database column period_type SqlType(INT) */
    val periodType: Rep[Int] = column[Int]("period_type")
    /** Database column unit_type_name SqlType(VARCHAR), Length(64,true) */
    val unitTypeName: Rep[String] = column[String]("unit_type_name", O.Length(64,varying=true))
    /** Database column job_name SqlType(VARCHAR), Length(64,true) */
    val jobName: Rep[String] = column[String]("job_name", O.Length(64,varying=true))
    /** Database column group_name SqlType(VARCHAR), Length(64,true) */
    val groupName: Rep[String] = column[String]("group_name", O.Length(64,varying=true))
    /** Database column group_size SqlType(INT) */
    val groupSize: Rep[Int] = column[Int]("group_size")
    /** Database column completed SqlType(INT) */
    val completed: Rep[Int] = column[Int]("completed")
    /** Database column confirmed_failed SqlType(INT) */
    val confirmedFailed: Rep[Int] = column[Int]("confirmed_failed")
    /** Database column unconfirmed_failed SqlType(INT) */
    val unconfirmedFailed: Rep[Int] = column[Int]("unconfirmed_failed")

    /** Primary key of ReportJob (database name report_job_PK) */
    val pk = primaryKey("report_job_PK", (timestamp, periodType, unitTypeName, jobName))
  }
  /** Collection-like TableQuery object for table ReportJob */
  lazy val ReportJob = new TableQuery(tag => new ReportJob(tag))

  /** Entity class storing rows of table ReportProv
   *  @param timestamp Database column timestamp_ SqlType(DATETIME)
   *  @param periodType Database column period_type SqlType(INT)
   *  @param unitTypeName Database column unit_type_name SqlType(VARCHAR), Length(64,true)
   *  @param profileName Database column profile_name SqlType(VARCHAR), Length(64,true)
   *  @param softwareVersion Database column software_version SqlType(VARCHAR), Length(64,true)
   *  @param provOutput Database column prov_output SqlType(VARCHAR), Length(16,true)
   *  @param okCount Database column ok_count SqlType(INT), Default(None)
   *  @param rescheduledCount Database column rescheduled_count SqlType(INT), Default(None)
   *  @param errorCount Database column error_count SqlType(INT), Default(None)
   *  @param missingCount Database column missing_count SqlType(INT), Default(None)
   *  @param sessionLengthAvg Database column session_length_avg SqlType(INT), Default(None) */
  case class ReportProvRow(timestamp: java.sql.Timestamp, periodType: Int, unitTypeName: String, profileName: String, softwareVersion: String, provOutput: String, okCount: Option[Int] = None, rescheduledCount: Option[Int] = None, errorCount: Option[Int] = None, missingCount: Option[Int] = None, sessionLengthAvg: Option[Int] = None)
  /** GetResult implicit for fetching ReportProvRow objects using plain SQL queries */
  implicit def GetResultReportProvRow(implicit e0: GR[java.sql.Timestamp], e1: GR[Int], e2: GR[String], e3: GR[Option[Int]]): GR[ReportProvRow] = GR{
    prs => import prs._
    ReportProvRow.tupled((<<[java.sql.Timestamp], <<[Int], <<[String], <<[String], <<[String], <<[String], <<?[Int], <<?[Int], <<?[Int], <<?[Int], <<?[Int]))
  }
  /** Table description of table report_prov. Objects of this class serve as prototypes for rows in queries. */
  class ReportProv(_tableTag: Tag) extends profile.api.Table[ReportProvRow](_tableTag, Some("acs"), "report_prov") {
    def * = (timestamp, periodType, unitTypeName, profileName, softwareVersion, provOutput, okCount, rescheduledCount, errorCount, missingCount, sessionLengthAvg) <> (ReportProvRow.tupled, ReportProvRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(timestamp), Rep.Some(periodType), Rep.Some(unitTypeName), Rep.Some(profileName), Rep.Some(softwareVersion), Rep.Some(provOutput), okCount, rescheduledCount, errorCount, missingCount, sessionLengthAvg)).shaped.<>({r=>import r._; _1.map(_=> ReportProvRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7, _8, _9, _10, _11)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column timestamp_ SqlType(DATETIME) */
    val timestamp: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("timestamp_")
    /** Database column period_type SqlType(INT) */
    val periodType: Rep[Int] = column[Int]("period_type")
    /** Database column unit_type_name SqlType(VARCHAR), Length(64,true) */
    val unitTypeName: Rep[String] = column[String]("unit_type_name", O.Length(64,varying=true))
    /** Database column profile_name SqlType(VARCHAR), Length(64,true) */
    val profileName: Rep[String] = column[String]("profile_name", O.Length(64,varying=true))
    /** Database column software_version SqlType(VARCHAR), Length(64,true) */
    val softwareVersion: Rep[String] = column[String]("software_version", O.Length(64,varying=true))
    /** Database column prov_output SqlType(VARCHAR), Length(16,true) */
    val provOutput: Rep[String] = column[String]("prov_output", O.Length(16,varying=true))
    /** Database column ok_count SqlType(INT), Default(None) */
    val okCount: Rep[Option[Int]] = column[Option[Int]]("ok_count", O.Default(None))
    /** Database column rescheduled_count SqlType(INT), Default(None) */
    val rescheduledCount: Rep[Option[Int]] = column[Option[Int]]("rescheduled_count", O.Default(None))
    /** Database column error_count SqlType(INT), Default(None) */
    val errorCount: Rep[Option[Int]] = column[Option[Int]]("error_count", O.Default(None))
    /** Database column missing_count SqlType(INT), Default(None) */
    val missingCount: Rep[Option[Int]] = column[Option[Int]]("missing_count", O.Default(None))
    /** Database column session_length_avg SqlType(INT), Default(None) */
    val sessionLengthAvg: Rep[Option[Int]] = column[Option[Int]]("session_length_avg", O.Default(None))

    /** Primary key of ReportProv (database name report_prov_PK) */
    val pk = primaryKey("report_prov_PK", (timestamp, periodType, unitTypeName, profileName, softwareVersion, provOutput))
  }
  /** Collection-like TableQuery object for table ReportProv */
  lazy val ReportProv = new TableQuery(tag => new ReportProv(tag))

  /** Entity class storing rows of table ReportSyslog
   *  @param timestamp Database column timestamp_ SqlType(DATETIME)
   *  @param periodType Database column period_type SqlType(INT)
   *  @param unitTypeName Database column unit_type_name SqlType(VARCHAR), Length(64,true)
   *  @param profileName Database column profile_name SqlType(VARCHAR), Length(64,true)
   *  @param severity Database column severity SqlType(VARCHAR), Length(16,true)
   *  @param syslogEventId Database column syslog_event_id SqlType(INT)
   *  @param facility Database column facility SqlType(VARCHAR), Length(32,true)
   *  @param unitCount Database column unit_count SqlType(INT) */
  case class ReportSyslogRow(timestamp: java.sql.Timestamp, periodType: Int, unitTypeName: String, profileName: String, severity: String, syslogEventId: Int, facility: String, unitCount: Int)
  /** GetResult implicit for fetching ReportSyslogRow objects using plain SQL queries */
  implicit def GetResultReportSyslogRow(implicit e0: GR[java.sql.Timestamp], e1: GR[Int], e2: GR[String]): GR[ReportSyslogRow] = GR{
    prs => import prs._
    ReportSyslogRow.tupled((<<[java.sql.Timestamp], <<[Int], <<[String], <<[String], <<[String], <<[Int], <<[String], <<[Int]))
  }
  /** Table description of table report_syslog. Objects of this class serve as prototypes for rows in queries. */
  class ReportSyslog(_tableTag: Tag) extends profile.api.Table[ReportSyslogRow](_tableTag, Some("acs"), "report_syslog") {
    def * = (timestamp, periodType, unitTypeName, profileName, severity, syslogEventId, facility, unitCount) <> (ReportSyslogRow.tupled, ReportSyslogRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(timestamp), Rep.Some(periodType), Rep.Some(unitTypeName), Rep.Some(profileName), Rep.Some(severity), Rep.Some(syslogEventId), Rep.Some(facility), Rep.Some(unitCount))).shaped.<>({r=>import r._; _1.map(_=> ReportSyslogRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column timestamp_ SqlType(DATETIME) */
    val timestamp: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("timestamp_")
    /** Database column period_type SqlType(INT) */
    val periodType: Rep[Int] = column[Int]("period_type")
    /** Database column unit_type_name SqlType(VARCHAR), Length(64,true) */
    val unitTypeName: Rep[String] = column[String]("unit_type_name", O.Length(64,varying=true))
    /** Database column profile_name SqlType(VARCHAR), Length(64,true) */
    val profileName: Rep[String] = column[String]("profile_name", O.Length(64,varying=true))
    /** Database column severity SqlType(VARCHAR), Length(16,true) */
    val severity: Rep[String] = column[String]("severity", O.Length(16,varying=true))
    /** Database column syslog_event_id SqlType(INT) */
    val syslogEventId: Rep[Int] = column[Int]("syslog_event_id")
    /** Database column facility SqlType(VARCHAR), Length(32,true) */
    val facility: Rep[String] = column[String]("facility", O.Length(32,varying=true))
    /** Database column unit_count SqlType(INT) */
    val unitCount: Rep[Int] = column[Int]("unit_count")

    /** Primary key of ReportSyslog (database name report_syslog_PK) */
    val pk = primaryKey("report_syslog_PK", (timestamp, periodType, unitTypeName, profileName, severity, syslogEventId, facility))
  }
  /** Collection-like TableQuery object for table ReportSyslog */
  lazy val ReportSyslog = new TableQuery(tag => new ReportSyslog(tag))

  /** Entity class storing rows of table ReportUnit
   *  @param timestamp Database column timestamp_ SqlType(DATETIME)
   *  @param periodType Database column period_type SqlType(INT)
   *  @param unitTypeName Database column unit_type_name SqlType(VARCHAR), Length(64,true)
   *  @param profileName Database column profile_name SqlType(VARCHAR), Length(64,true)
   *  @param softwareVersion Database column software_version SqlType(VARCHAR), Length(64,true)
   *  @param status Database column status SqlType(VARCHAR), Length(32,true)
   *  @param unitCount Database column unit_count SqlType(INT) */
  case class ReportUnitRow(timestamp: java.sql.Timestamp, periodType: Int, unitTypeName: String, profileName: String, softwareVersion: String, status: String, unitCount: Int)
  /** GetResult implicit for fetching ReportUnitRow objects using plain SQL queries */
  implicit def GetResultReportUnitRow(implicit e0: GR[java.sql.Timestamp], e1: GR[Int], e2: GR[String]): GR[ReportUnitRow] = GR{
    prs => import prs._
    ReportUnitRow.tupled((<<[java.sql.Timestamp], <<[Int], <<[String], <<[String], <<[String], <<[String], <<[Int]))
  }
  /** Table description of table report_unit. Objects of this class serve as prototypes for rows in queries. */
  class ReportUnit(_tableTag: Tag) extends profile.api.Table[ReportUnitRow](_tableTag, Some("acs"), "report_unit") {
    def * = (timestamp, periodType, unitTypeName, profileName, softwareVersion, status, unitCount) <> (ReportUnitRow.tupled, ReportUnitRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(timestamp), Rep.Some(periodType), Rep.Some(unitTypeName), Rep.Some(profileName), Rep.Some(softwareVersion), Rep.Some(status), Rep.Some(unitCount))).shaped.<>({r=>import r._; _1.map(_=> ReportUnitRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column timestamp_ SqlType(DATETIME) */
    val timestamp: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("timestamp_")
    /** Database column period_type SqlType(INT) */
    val periodType: Rep[Int] = column[Int]("period_type")
    /** Database column unit_type_name SqlType(VARCHAR), Length(64,true) */
    val unitTypeName: Rep[String] = column[String]("unit_type_name", O.Length(64,varying=true))
    /** Database column profile_name SqlType(VARCHAR), Length(64,true) */
    val profileName: Rep[String] = column[String]("profile_name", O.Length(64,varying=true))
    /** Database column software_version SqlType(VARCHAR), Length(64,true) */
    val softwareVersion: Rep[String] = column[String]("software_version", O.Length(64,varying=true))
    /** Database column status SqlType(VARCHAR), Length(32,true) */
    val status: Rep[String] = column[String]("status", O.Length(32,varying=true))
    /** Database column unit_count SqlType(INT) */
    val unitCount: Rep[Int] = column[Int]("unit_count")

    /** Primary key of ReportUnit (database name report_unit_PK) */
    val pk = primaryKey("report_unit_PK", (timestamp, periodType, unitTypeName, profileName, softwareVersion, status))
  }
  /** Collection-like TableQuery object for table ReportUnit */
  lazy val ReportUnit = new TableQuery(tag => new ReportUnit(tag))

  /** Entity class storing rows of table ReportVoip
   *  @param timestamp Database column timestamp_ SqlType(DATETIME)
   *  @param periodType Database column period_type SqlType(INT)
   *  @param unitTypeName Database column unit_type_name SqlType(VARCHAR), Length(64,true)
   *  @param profileName Database column profile_name SqlType(VARCHAR), Length(64,true)
   *  @param softwareVersion Database column software_version SqlType(VARCHAR), Length(64,true)
   *  @param line Database column line SqlType(INT)
   *  @param mosAvg Database column mos_avg SqlType(INT), Default(None)
   *  @param jitterAvg Database column jitter_avg SqlType(INT), Default(None)
   *  @param jitterMax Database column jitter_max SqlType(INT), Default(None)
   *  @param percentLossAvg Database column percent_loss_avg SqlType(INT), Default(None)
   *  @param callLengthAvg Database column call_length_avg SqlType(INT), Default(None)
   *  @param callLengthTotal Database column call_length_total SqlType(INT)
   *  @param incomingCallCount Database column incoming_call_count SqlType(INT)
   *  @param outgoingCallCount Database column outgoing_call_count SqlType(INT)
   *  @param outgoingCallFailedCount Database column outgoing_call_failed_count SqlType(INT)
   *  @param abortedCallCount Database column aborted_call_count SqlType(INT)
   *  @param noSipServiceTime Database column no_sip_service_time SqlType(INT) */
  case class ReportVoipRow(timestamp: java.sql.Timestamp, periodType: Int, unitTypeName: String, profileName: String, softwareVersion: String, line: Int, mosAvg: Option[Int] = None, jitterAvg: Option[Int] = None, jitterMax: Option[Int] = None, percentLossAvg: Option[Int] = None, callLengthAvg: Option[Int] = None, callLengthTotal: Int, incomingCallCount: Int, outgoingCallCount: Int, outgoingCallFailedCount: Int, abortedCallCount: Int, noSipServiceTime: Int)
  /** GetResult implicit for fetching ReportVoipRow objects using plain SQL queries */
  implicit def GetResultReportVoipRow(implicit e0: GR[java.sql.Timestamp], e1: GR[Int], e2: GR[String], e3: GR[Option[Int]]): GR[ReportVoipRow] = GR{
    prs => import prs._
    ReportVoipRow.tupled((<<[java.sql.Timestamp], <<[Int], <<[String], <<[String], <<[String], <<[Int], <<?[Int], <<?[Int], <<?[Int], <<?[Int], <<?[Int], <<[Int], <<[Int], <<[Int], <<[Int], <<[Int], <<[Int]))
  }
  /** Table description of table report_voip. Objects of this class serve as prototypes for rows in queries. */
  class ReportVoip(_tableTag: Tag) extends profile.api.Table[ReportVoipRow](_tableTag, Some("acs"), "report_voip") {
    def * = (timestamp, periodType, unitTypeName, profileName, softwareVersion, line, mosAvg, jitterAvg, jitterMax, percentLossAvg, callLengthAvg, callLengthTotal, incomingCallCount, outgoingCallCount, outgoingCallFailedCount, abortedCallCount, noSipServiceTime) <> (ReportVoipRow.tupled, ReportVoipRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(timestamp), Rep.Some(periodType), Rep.Some(unitTypeName), Rep.Some(profileName), Rep.Some(softwareVersion), Rep.Some(line), mosAvg, jitterAvg, jitterMax, percentLossAvg, callLengthAvg, Rep.Some(callLengthTotal), Rep.Some(incomingCallCount), Rep.Some(outgoingCallCount), Rep.Some(outgoingCallFailedCount), Rep.Some(abortedCallCount), Rep.Some(noSipServiceTime))).shaped.<>({r=>import r._; _1.map(_=> ReportVoipRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7, _8, _9, _10, _11, _12.get, _13.get, _14.get, _15.get, _16.get, _17.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column timestamp_ SqlType(DATETIME) */
    val timestamp: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("timestamp_")
    /** Database column period_type SqlType(INT) */
    val periodType: Rep[Int] = column[Int]("period_type")
    /** Database column unit_type_name SqlType(VARCHAR), Length(64,true) */
    val unitTypeName: Rep[String] = column[String]("unit_type_name", O.Length(64,varying=true))
    /** Database column profile_name SqlType(VARCHAR), Length(64,true) */
    val profileName: Rep[String] = column[String]("profile_name", O.Length(64,varying=true))
    /** Database column software_version SqlType(VARCHAR), Length(64,true) */
    val softwareVersion: Rep[String] = column[String]("software_version", O.Length(64,varying=true))
    /** Database column line SqlType(INT) */
    val line: Rep[Int] = column[Int]("line")
    /** Database column mos_avg SqlType(INT), Default(None) */
    val mosAvg: Rep[Option[Int]] = column[Option[Int]]("mos_avg", O.Default(None))
    /** Database column jitter_avg SqlType(INT), Default(None) */
    val jitterAvg: Rep[Option[Int]] = column[Option[Int]]("jitter_avg", O.Default(None))
    /** Database column jitter_max SqlType(INT), Default(None) */
    val jitterMax: Rep[Option[Int]] = column[Option[Int]]("jitter_max", O.Default(None))
    /** Database column percent_loss_avg SqlType(INT), Default(None) */
    val percentLossAvg: Rep[Option[Int]] = column[Option[Int]]("percent_loss_avg", O.Default(None))
    /** Database column call_length_avg SqlType(INT), Default(None) */
    val callLengthAvg: Rep[Option[Int]] = column[Option[Int]]("call_length_avg", O.Default(None))
    /** Database column call_length_total SqlType(INT) */
    val callLengthTotal: Rep[Int] = column[Int]("call_length_total")
    /** Database column incoming_call_count SqlType(INT) */
    val incomingCallCount: Rep[Int] = column[Int]("incoming_call_count")
    /** Database column outgoing_call_count SqlType(INT) */
    val outgoingCallCount: Rep[Int] = column[Int]("outgoing_call_count")
    /** Database column outgoing_call_failed_count SqlType(INT) */
    val outgoingCallFailedCount: Rep[Int] = column[Int]("outgoing_call_failed_count")
    /** Database column aborted_call_count SqlType(INT) */
    val abortedCallCount: Rep[Int] = column[Int]("aborted_call_count")
    /** Database column no_sip_service_time SqlType(INT) */
    val noSipServiceTime: Rep[Int] = column[Int]("no_sip_service_time")

    /** Primary key of ReportVoip (database name report_voip_PK) */
    val pk = primaryKey("report_voip_PK", (timestamp, periodType, unitTypeName, profileName, softwareVersion, line))
  }
  /** Collection-like TableQuery object for table ReportVoip */
  lazy val ReportVoip = new TableQuery(tag => new ReportVoip(tag))

  /** Entity class storing rows of table ReportVoipTr
   *  @param timestamp Database column timestamp_ SqlType(DATETIME)
   *  @param periodType Database column period_type SqlType(INT)
   *  @param unitTypeName Database column unit_type_name SqlType(VARCHAR), Length(64,true)
   *  @param profileName Database column profile_name SqlType(VARCHAR), Length(64,true)
   *  @param softwareVersion Database column software_version SqlType(VARCHAR), Length(64,true)
   *  @param line Database column line SqlType(VARCHAR), Length(16,true)
   *  @param lineStatus Database column line_status SqlType(VARCHAR), Length(64,true)
   *  @param overrunsCount Database column overruns_count SqlType(INT)
   *  @param underrunsCount Database column underruns_count SqlType(INT)
   *  @param percentLossAvg Database column percent_loss_avg SqlType(INT), Default(None)
   *  @param callLengthAvg Database column call_length_avg SqlType(INT), Default(None)
   *  @param callLengthTotal Database column call_length_total SqlType(INT)
   *  @param incomingCallCount Database column incoming_call_count SqlType(INT)
   *  @param outgoingCallCount Database column outgoing_call_count SqlType(INT)
   *  @param outgoingCallFailedCount Database column outgoing_call_failed_count SqlType(INT)
   *  @param abortedCallCount Database column aborted_call_count SqlType(INT)
   *  @param noSipServiceTime Database column no_sip_service_time SqlType(INT) */
  case class ReportVoipTrRow(timestamp: java.sql.Timestamp, periodType: Int, unitTypeName: String, profileName: String, softwareVersion: String, line: String, lineStatus: String, overrunsCount: Int, underrunsCount: Int, percentLossAvg: Option[Int] = None, callLengthAvg: Option[Int] = None, callLengthTotal: Int, incomingCallCount: Int, outgoingCallCount: Int, outgoingCallFailedCount: Int, abortedCallCount: Int, noSipServiceTime: Int)
  /** GetResult implicit for fetching ReportVoipTrRow objects using plain SQL queries */
  implicit def GetResultReportVoipTrRow(implicit e0: GR[java.sql.Timestamp], e1: GR[Int], e2: GR[String], e3: GR[Option[Int]]): GR[ReportVoipTrRow] = GR{
    prs => import prs._
    ReportVoipTrRow.tupled((<<[java.sql.Timestamp], <<[Int], <<[String], <<[String], <<[String], <<[String], <<[String], <<[Int], <<[Int], <<?[Int], <<?[Int], <<[Int], <<[Int], <<[Int], <<[Int], <<[Int], <<[Int]))
  }
  /** Table description of table report_voip_tr. Objects of this class serve as prototypes for rows in queries. */
  class ReportVoipTr(_tableTag: Tag) extends profile.api.Table[ReportVoipTrRow](_tableTag, Some("acs"), "report_voip_tr") {
    def * = (timestamp, periodType, unitTypeName, profileName, softwareVersion, line, lineStatus, overrunsCount, underrunsCount, percentLossAvg, callLengthAvg, callLengthTotal, incomingCallCount, outgoingCallCount, outgoingCallFailedCount, abortedCallCount, noSipServiceTime) <> (ReportVoipTrRow.tupled, ReportVoipTrRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(timestamp), Rep.Some(periodType), Rep.Some(unitTypeName), Rep.Some(profileName), Rep.Some(softwareVersion), Rep.Some(line), Rep.Some(lineStatus), Rep.Some(overrunsCount), Rep.Some(underrunsCount), percentLossAvg, callLengthAvg, Rep.Some(callLengthTotal), Rep.Some(incomingCallCount), Rep.Some(outgoingCallCount), Rep.Some(outgoingCallFailedCount), Rep.Some(abortedCallCount), Rep.Some(noSipServiceTime))).shaped.<>({r=>import r._; _1.map(_=> ReportVoipTrRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10, _11, _12.get, _13.get, _14.get, _15.get, _16.get, _17.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column timestamp_ SqlType(DATETIME) */
    val timestamp: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("timestamp_")
    /** Database column period_type SqlType(INT) */
    val periodType: Rep[Int] = column[Int]("period_type")
    /** Database column unit_type_name SqlType(VARCHAR), Length(64,true) */
    val unitTypeName: Rep[String] = column[String]("unit_type_name", O.Length(64,varying=true))
    /** Database column profile_name SqlType(VARCHAR), Length(64,true) */
    val profileName: Rep[String] = column[String]("profile_name", O.Length(64,varying=true))
    /** Database column software_version SqlType(VARCHAR), Length(64,true) */
    val softwareVersion: Rep[String] = column[String]("software_version", O.Length(64,varying=true))
    /** Database column line SqlType(VARCHAR), Length(16,true) */
    val line: Rep[String] = column[String]("line", O.Length(16,varying=true))
    /** Database column line_status SqlType(VARCHAR), Length(64,true) */
    val lineStatus: Rep[String] = column[String]("line_status", O.Length(64,varying=true))
    /** Database column overruns_count SqlType(INT) */
    val overrunsCount: Rep[Int] = column[Int]("overruns_count")
    /** Database column underruns_count SqlType(INT) */
    val underrunsCount: Rep[Int] = column[Int]("underruns_count")
    /** Database column percent_loss_avg SqlType(INT), Default(None) */
    val percentLossAvg: Rep[Option[Int]] = column[Option[Int]]("percent_loss_avg", O.Default(None))
    /** Database column call_length_avg SqlType(INT), Default(None) */
    val callLengthAvg: Rep[Option[Int]] = column[Option[Int]]("call_length_avg", O.Default(None))
    /** Database column call_length_total SqlType(INT) */
    val callLengthTotal: Rep[Int] = column[Int]("call_length_total")
    /** Database column incoming_call_count SqlType(INT) */
    val incomingCallCount: Rep[Int] = column[Int]("incoming_call_count")
    /** Database column outgoing_call_count SqlType(INT) */
    val outgoingCallCount: Rep[Int] = column[Int]("outgoing_call_count")
    /** Database column outgoing_call_failed_count SqlType(INT) */
    val outgoingCallFailedCount: Rep[Int] = column[Int]("outgoing_call_failed_count")
    /** Database column aborted_call_count SqlType(INT) */
    val abortedCallCount: Rep[Int] = column[Int]("aborted_call_count")
    /** Database column no_sip_service_time SqlType(INT) */
    val noSipServiceTime: Rep[Int] = column[Int]("no_sip_service_time")

    /** Primary key of ReportVoipTr (database name report_voip_tr_PK) */
    val pk = primaryKey("report_voip_tr_PK", (timestamp, periodType, unitTypeName, profileName, softwareVersion, line, lineStatus))
  }
  /** Collection-like TableQuery object for table ReportVoipTr */
  lazy val ReportVoipTr = new TableQuery(tag => new ReportVoipTr(tag))

  /** Entity class storing rows of table ScriptExecution
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param unitTypeId Database column unit_type_id SqlType(INT)
   *  @param filestoreId Database column filestore_id SqlType(INT)
   *  @param arguments Database column arguments SqlType(VARCHAR), Length(1024,true), Default(None)
   *  @param requestTimestamp Database column request_timestamp SqlType(DATETIME)
   *  @param requestId Database column request_id SqlType(VARCHAR), Length(32,true), Default(None)
   *  @param startTimestamp Database column start_timestamp SqlType(DATETIME), Default(None)
   *  @param endTimestamp Database column end_timestamp SqlType(DATETIME), Default(None)
   *  @param exitStatus Database column exit_status SqlType(INT), Default(None)
   *  @param errorMessage Database column error_message SqlType(VARCHAR), Length(1024,true), Default(None) */
  case class ScriptExecutionRow(id: Int, unitTypeId: Int, filestoreId: Int, arguments: Option[String] = None, requestTimestamp: java.sql.Timestamp, requestId: Option[String] = None, startTimestamp: Option[java.sql.Timestamp] = None, endTimestamp: Option[java.sql.Timestamp] = None, exitStatus: Option[Int] = None, errorMessage: Option[String] = None)
  /** GetResult implicit for fetching ScriptExecutionRow objects using plain SQL queries */
  implicit def GetResultScriptExecutionRow(implicit e0: GR[Int], e1: GR[Option[String]], e2: GR[java.sql.Timestamp], e3: GR[Option[java.sql.Timestamp]], e4: GR[Option[Int]]): GR[ScriptExecutionRow] = GR{
    prs => import prs._
    ScriptExecutionRow.tupled((<<[Int], <<[Int], <<[Int], <<?[String], <<[java.sql.Timestamp], <<?[String], <<?[java.sql.Timestamp], <<?[java.sql.Timestamp], <<?[Int], <<?[String]))
  }
  /** Table description of table script_execution. Objects of this class serve as prototypes for rows in queries. */
  class ScriptExecution(_tableTag: Tag) extends profile.api.Table[ScriptExecutionRow](_tableTag, Some("acs"), "script_execution") {
    def * = (id, unitTypeId, filestoreId, arguments, requestTimestamp, requestId, startTimestamp, endTimestamp, exitStatus, errorMessage) <> (ScriptExecutionRow.tupled, ScriptExecutionRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(unitTypeId), Rep.Some(filestoreId), arguments, Rep.Some(requestTimestamp), requestId, startTimestamp, endTimestamp, exitStatus, errorMessage)).shaped.<>({r=>import r._; _1.map(_=> ScriptExecutionRow.tupled((_1.get, _2.get, _3.get, _4, _5.get, _6, _7, _8, _9, _10)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column unit_type_id SqlType(INT) */
    val unitTypeId: Rep[Int] = column[Int]("unit_type_id")
    /** Database column filestore_id SqlType(INT) */
    val filestoreId: Rep[Int] = column[Int]("filestore_id")
    /** Database column arguments SqlType(VARCHAR), Length(1024,true), Default(None) */
    val arguments: Rep[Option[String]] = column[Option[String]]("arguments", O.Length(1024,varying=true), O.Default(None))
    /** Database column request_timestamp SqlType(DATETIME) */
    val requestTimestamp: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("request_timestamp")
    /** Database column request_id SqlType(VARCHAR), Length(32,true), Default(None) */
    val requestId: Rep[Option[String]] = column[Option[String]]("request_id", O.Length(32,varying=true), O.Default(None))
    /** Database column start_timestamp SqlType(DATETIME), Default(None) */
    val startTimestamp: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("start_timestamp", O.Default(None))
    /** Database column end_timestamp SqlType(DATETIME), Default(None) */
    val endTimestamp: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("end_timestamp", O.Default(None))
    /** Database column exit_status SqlType(INT), Default(None) */
    val exitStatus: Rep[Option[Int]] = column[Option[Int]]("exit_status", O.Default(None))
    /** Database column error_message SqlType(VARCHAR), Length(1024,true), Default(None) */
    val errorMessage: Rep[Option[String]] = column[Option[String]]("error_message", O.Length(1024,varying=true), O.Default(None))
  }
  /** Collection-like TableQuery object for table ScriptExecution */
  lazy val ScriptExecution = new TableQuery(tag => new ScriptExecution(tag))

  /** Entity class storing rows of table Syslog
   *  @param syslogId Database column syslog_id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param collectorTimestamp Database column collector_timestamp SqlType(DATETIME)
   *  @param syslogEventId Database column syslog_event_id SqlType(INT)
   *  @param facility Database column facility SqlType(INT)
   *  @param facilityVersion Database column facility_version SqlType(VARCHAR), Length(48,true), Default(None)
   *  @param severity Database column severity SqlType(INT)
   *  @param deviceTimestamp Database column device_timestamp SqlType(VARCHAR), Length(32,true), Default(None)
   *  @param hostname Database column hostname SqlType(VARCHAR), Length(32,true), Default(None)
   *  @param tag Database column tag SqlType(VARCHAR), Length(32,true), Default(None)
   *  @param content Database column content SqlType(VARCHAR), Length(1024,true)
   *  @param flags Database column flags SqlType(VARCHAR), Length(32,true), Default(None)
   *  @param ipaddress Database column ipaddress SqlType(VARCHAR), Length(32,true), Default(None)
   *  @param unitId Database column unit_id SqlType(VARCHAR), Length(64,true), Default(None)
   *  @param profileName Database column profile_name SqlType(VARCHAR), Length(64,true), Default(None)
   *  @param unitTypeName Database column unit_type_name SqlType(VARCHAR), Length(64,true), Default(None)
   *  @param userId Database column user_id SqlType(VARCHAR), Length(32,true), Default(None) */
  case class SyslogRow(syslogId: Long, collectorTimestamp: java.sql.Timestamp, syslogEventId: Int, facility: Int, facilityVersion: Option[String] = None, severity: Int, deviceTimestamp: Option[String] = None, hostname: Option[String] = None, tag: Option[String] = None, content: String, flags: Option[String] = None, ipaddress: Option[String] = None, unitId: Option[String] = None, profileName: Option[String] = None, unitTypeName: Option[String] = None, userId: Option[String] = None)
  /** GetResult implicit for fetching SyslogRow objects using plain SQL queries */
  implicit def GetResultSyslogRow(implicit e0: GR[Long], e1: GR[java.sql.Timestamp], e2: GR[Int], e3: GR[Option[String]], e4: GR[String]): GR[SyslogRow] = GR{
    prs => import prs._
    SyslogRow.tupled((<<[Long], <<[java.sql.Timestamp], <<[Int], <<[Int], <<?[String], <<[Int], <<?[String], <<?[String], <<?[String], <<[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String]))
  }
  /** Table description of table syslog. Objects of this class serve as prototypes for rows in queries. */
  class Syslog(_tableTag: Tag) extends profile.api.Table[SyslogRow](_tableTag, Some("acs"), "syslog") {
    def * = (syslogId, collectorTimestamp, syslogEventId, facility, facilityVersion, severity, deviceTimestamp, hostname, tag, content, flags, ipaddress, unitId, profileName, unitTypeName, userId) <> (SyslogRow.tupled, SyslogRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(syslogId), Rep.Some(collectorTimestamp), Rep.Some(syslogEventId), Rep.Some(facility), facilityVersion, Rep.Some(severity), deviceTimestamp, hostname, tag, Rep.Some(content), flags, ipaddress, unitId, profileName, unitTypeName, userId)).shaped.<>({r=>import r._; _1.map(_=> SyslogRow.tupled((_1.get, _2.get, _3.get, _4.get, _5, _6.get, _7, _8, _9, _10.get, _11, _12, _13, _14, _15, _16)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column syslog_id SqlType(BIGINT), AutoInc, PrimaryKey */
    val syslogId: Rep[Long] = column[Long]("syslog_id", O.AutoInc, O.PrimaryKey)
    /** Database column collector_timestamp SqlType(DATETIME) */
    val collectorTimestamp: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("collector_timestamp")
    /** Database column syslog_event_id SqlType(INT) */
    val syslogEventId: Rep[Int] = column[Int]("syslog_event_id")
    /** Database column facility SqlType(INT) */
    val facility: Rep[Int] = column[Int]("facility")
    /** Database column facility_version SqlType(VARCHAR), Length(48,true), Default(None) */
    val facilityVersion: Rep[Option[String]] = column[Option[String]]("facility_version", O.Length(48,varying=true), O.Default(None))
    /** Database column severity SqlType(INT) */
    val severity: Rep[Int] = column[Int]("severity")
    /** Database column device_timestamp SqlType(VARCHAR), Length(32,true), Default(None) */
    val deviceTimestamp: Rep[Option[String]] = column[Option[String]]("device_timestamp", O.Length(32,varying=true), O.Default(None))
    /** Database column hostname SqlType(VARCHAR), Length(32,true), Default(None) */
    val hostname: Rep[Option[String]] = column[Option[String]]("hostname", O.Length(32,varying=true), O.Default(None))
    /** Database column tag SqlType(VARCHAR), Length(32,true), Default(None) */
    val tag: Rep[Option[String]] = column[Option[String]]("tag", O.Length(32,varying=true), O.Default(None))
    /** Database column content SqlType(VARCHAR), Length(1024,true) */
    val content: Rep[String] = column[String]("content", O.Length(1024,varying=true))
    /** Database column flags SqlType(VARCHAR), Length(32,true), Default(None) */
    val flags: Rep[Option[String]] = column[Option[String]]("flags", O.Length(32,varying=true), O.Default(None))
    /** Database column ipaddress SqlType(VARCHAR), Length(32,true), Default(None) */
    val ipaddress: Rep[Option[String]] = column[Option[String]]("ipaddress", O.Length(32,varying=true), O.Default(None))
    /** Database column unit_id SqlType(VARCHAR), Length(64,true), Default(None) */
    val unitId: Rep[Option[String]] = column[Option[String]]("unit_id", O.Length(64,varying=true), O.Default(None))
    /** Database column profile_name SqlType(VARCHAR), Length(64,true), Default(None) */
    val profileName: Rep[Option[String]] = column[Option[String]]("profile_name", O.Length(64,varying=true), O.Default(None))
    /** Database column unit_type_name SqlType(VARCHAR), Length(64,true), Default(None) */
    val unitTypeName: Rep[Option[String]] = column[Option[String]]("unit_type_name", O.Length(64,varying=true), O.Default(None))
    /** Database column user_id SqlType(VARCHAR), Length(32,true), Default(None) */
    val userId: Rep[Option[String]] = column[Option[String]]("user_id", O.Length(32,varying=true), O.Default(None))

    /** Index over (collectorTimestamp,severity,syslogEventId) (database name idx_syslog_coll_tms) */
    val index1 = index("idx_syslog_coll_tms", (collectorTimestamp, severity, syslogEventId))
    /** Index over (unitId,collectorTimestamp) (database name idx_syslog_unit_id_coll_tms) */
    val index2 = index("idx_syslog_unit_id_coll_tms", (unitId, collectorTimestamp))
  }
  /** Collection-like TableQuery object for table Syslog */
  lazy val Syslog = new TableQuery(tag => new Syslog(tag))

  /** Entity class storing rows of table SyslogEvent
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param syslogEventId Database column syslog_event_id SqlType(INT)
   *  @param syslogEventName Database column syslog_event_name SqlType(VARCHAR), Length(64,true)
   *  @param unitTypeId Database column unit_type_id SqlType(INT)
   *  @param groupId Database column group_id SqlType(INT), Default(None)
   *  @param expression Database column expression SqlType(VARCHAR), Length(64,true), Default(Specify an expression)
   *  @param storePolicy Database column store_policy SqlType(VARCHAR), Length(16,true), Default(STORE)
   *  @param filestoreId Database column filestore_id SqlType(INT), Default(None)
   *  @param description Database column description SqlType(VARCHAR), Length(1024,true), Default(None)
   *  @param deleteLimit Database column delete_limit SqlType(INT), Default(None) */
  case class SyslogEventRow(id: Int, syslogEventId: Int, syslogEventName: String, unitTypeId: Int, groupId: Option[Int] = None, expression: String = "Specify an expression", storePolicy: String = "STORE", filestoreId: Option[Int] = None, description: Option[String] = None, deleteLimit: Option[Int] = None)
  /** GetResult implicit for fetching SyslogEventRow objects using plain SQL queries */
  implicit def GetResultSyslogEventRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[Int]], e3: GR[Option[String]]): GR[SyslogEventRow] = GR{
    prs => import prs._
    SyslogEventRow.tupled((<<[Int], <<[Int], <<[String], <<[Int], <<?[Int], <<[String], <<[String], <<?[Int], <<?[String], <<?[Int]))
  }
  /** Table description of table syslog_event. Objects of this class serve as prototypes for rows in queries. */
  class SyslogEvent(_tableTag: Tag) extends profile.api.Table[SyslogEventRow](_tableTag, Some("acs"), "syslog_event") {
    def * = (id, syslogEventId, syslogEventName, unitTypeId, groupId, expression, storePolicy, filestoreId, description, deleteLimit) <> (SyslogEventRow.tupled, SyslogEventRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(syslogEventId), Rep.Some(syslogEventName), Rep.Some(unitTypeId), groupId, Rep.Some(expression), Rep.Some(storePolicy), filestoreId, description, deleteLimit)).shaped.<>({r=>import r._; _1.map(_=> SyslogEventRow.tupled((_1.get, _2.get, _3.get, _4.get, _5, _6.get, _7.get, _8, _9, _10)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column syslog_event_id SqlType(INT) */
    val syslogEventId: Rep[Int] = column[Int]("syslog_event_id")
    /** Database column syslog_event_name SqlType(VARCHAR), Length(64,true) */
    val syslogEventName: Rep[String] = column[String]("syslog_event_name", O.Length(64,varying=true))
    /** Database column unit_type_id SqlType(INT) */
    val unitTypeId: Rep[Int] = column[Int]("unit_type_id")
    /** Database column group_id SqlType(INT), Default(None) */
    val groupId: Rep[Option[Int]] = column[Option[Int]]("group_id", O.Default(None))
    /** Database column expression SqlType(VARCHAR), Length(64,true), Default(Specify an expression) */
    val expression: Rep[String] = column[String]("expression", O.Length(64,varying=true), O.Default("Specify an expression"))
    /** Database column store_policy SqlType(VARCHAR), Length(16,true), Default(STORE) */
    val storePolicy: Rep[String] = column[String]("store_policy", O.Length(16,varying=true), O.Default("STORE"))
    /** Database column filestore_id SqlType(INT), Default(None) */
    val filestoreId: Rep[Option[Int]] = column[Option[Int]]("filestore_id", O.Default(None))
    /** Database column description SqlType(VARCHAR), Length(1024,true), Default(None) */
    val description: Rep[Option[String]] = column[Option[String]]("description", O.Length(1024,varying=true), O.Default(None))
    /** Database column delete_limit SqlType(INT), Default(None) */
    val deleteLimit: Rep[Option[Int]] = column[Option[Int]]("delete_limit", O.Default(None))

    /** Foreign key referencing Filestore (database name fk_syslogevent_filestore_id) */
    lazy val filestoreFk = foreignKey("fk_syslogevent_filestore_id", filestoreId, Filestore)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Group (database name fk_syslogevent__group_id) */
    lazy val groupFk = foreignKey("fk_syslogevent__group_id", groupId, Group)(r => Rep.Some(r.groupId), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing UnitType (database name fk_syslogevent__unit_type_id) */
    lazy val unitTypeFk = foreignKey("fk_syslogevent__unit_type_id", unitTypeId, UnitType)(r => r.unitTypeId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

    /** Uniqueness Index over (syslogEventId,unitTypeId) (database name idx_syslog_event_id_unit_type_name) */
    val index1 = index("idx_syslog_event_id_unit_type_name", (syslogEventId, unitTypeId), unique=true)
  }
  /** Collection-like TableQuery object for table SyslogEvent */
  lazy val SyslogEvent = new TableQuery(tag => new SyslogEvent(tag))

  /** Entity class storing rows of table TestCase
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param unitTypeId Database column unit_type_id SqlType(INT)
   *  @param method Database column method SqlType(VARCHAR), Length(16,true)
   *  @param tag Database column tag SqlType(VARCHAR), Length(128,true), Default(None)
   *  @param expectError Database column expect_error SqlType(INT), Default(None) */
  case class TestCaseRow(id: Int, unitTypeId: Int, method: String, tag: Option[String] = None, expectError: Option[Int] = None)
  /** GetResult implicit for fetching TestCaseRow objects using plain SQL queries */
  implicit def GetResultTestCaseRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[String]], e3: GR[Option[Int]]): GR[TestCaseRow] = GR{
    prs => import prs._
    TestCaseRow.tupled((<<[Int], <<[Int], <<[String], <<?[String], <<?[Int]))
  }
  /** Table description of table test_case. Objects of this class serve as prototypes for rows in queries. */
  class TestCase(_tableTag: Tag) extends profile.api.Table[TestCaseRow](_tableTag, Some("acs"), "test_case") {
    def * = (id, unitTypeId, method, tag, expectError) <> (TestCaseRow.tupled, TestCaseRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(unitTypeId), Rep.Some(method), tag, expectError)).shaped.<>({r=>import r._; _1.map(_=> TestCaseRow.tupled((_1.get, _2.get, _3.get, _4, _5)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column unit_type_id SqlType(INT) */
    val unitTypeId: Rep[Int] = column[Int]("unit_type_id")
    /** Database column method SqlType(VARCHAR), Length(16,true) */
    val method: Rep[String] = column[String]("method", O.Length(16,varying=true))
    /** Database column tag SqlType(VARCHAR), Length(128,true), Default(None) */
    val tag: Rep[Option[String]] = column[Option[String]]("tag", O.Length(128,varying=true), O.Default(None))
    /** Database column expect_error SqlType(INT), Default(None) */
    val expectError: Rep[Option[Int]] = column[Option[Int]]("expect_error", O.Default(None))

    /** Foreign key referencing UnitType (database name fk_test_case_unit_type_id) */
    lazy val unitTypeFk = foreignKey("fk_test_case_unit_type_id", unitTypeId, UnitType)(r => r.unitTypeId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table TestCase */
  lazy val TestCase = new TableQuery(tag => new TestCase(tag))

  /** Entity class storing rows of table TestCaseFiles
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param caseId Database column case_id SqlType(INT)
   *  @param inputFileId Database column input_file_id SqlType(INT)
   *  @param outputFileId Database column output_file_id SqlType(INT), Default(None) */
  case class TestCaseFilesRow(id: Int, caseId: Int, inputFileId: Int, outputFileId: Option[Int] = None)
  /** GetResult implicit for fetching TestCaseFilesRow objects using plain SQL queries */
  implicit def GetResultTestCaseFilesRow(implicit e0: GR[Int], e1: GR[Option[Int]]): GR[TestCaseFilesRow] = GR{
    prs => import prs._
    TestCaseFilesRow.tupled((<<[Int], <<[Int], <<[Int], <<?[Int]))
  }
  /** Table description of table test_case_files. Objects of this class serve as prototypes for rows in queries. */
  class TestCaseFiles(_tableTag: Tag) extends profile.api.Table[TestCaseFilesRow](_tableTag, Some("acs"), "test_case_files") {
    def * = (id, caseId, inputFileId, outputFileId) <> (TestCaseFilesRow.tupled, TestCaseFilesRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(caseId), Rep.Some(inputFileId), outputFileId)).shaped.<>({r=>import r._; _1.map(_=> TestCaseFilesRow.tupled((_1.get, _2.get, _3.get, _4)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column case_id SqlType(INT) */
    val caseId: Rep[Int] = column[Int]("case_id")
    /** Database column input_file_id SqlType(INT) */
    val inputFileId: Rep[Int] = column[Int]("input_file_id")
    /** Database column output_file_id SqlType(INT), Default(None) */
    val outputFileId: Rep[Option[Int]] = column[Option[Int]]("output_file_id", O.Default(None))

    /** Foreign key referencing Filestore (database name fk_test_case_files_input_filestore_id) */
    lazy val filestoreFk1 = foreignKey("fk_test_case_files_input_filestore_id", inputFileId, Filestore)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Filestore (database name fk_test_case_files_output_filestore_id) */
    lazy val filestoreFk2 = foreignKey("fk_test_case_files_output_filestore_id", outputFileId, Filestore)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing TestCase (database name fk_test_case_files_case_id) */
    lazy val testCaseFk = foreignKey("fk_test_case_files_case_id", caseId, TestCase)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table TestCaseFiles */
  lazy val TestCaseFiles = new TableQuery(tag => new TestCaseFiles(tag))

  /** Entity class storing rows of table TestCaseParam
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param `type` Database column type SqlType(VARCHAR), Length(16,true)
   *  @param caseId Database column case_id SqlType(INT)
   *  @param unitTypeParamId Database column unit_type_param_id SqlType(INT)
   *  @param value Database column value SqlType(VARCHAR), Length(512,true), Default(None)
   *  @param notification Database column notification SqlType(INT), Default(None) */
  case class TestCaseParamRow(id: Int, `type`: String, caseId: Int, unitTypeParamId: Int, value: Option[String] = None, notification: Option[Int] = None)
  /** GetResult implicit for fetching TestCaseParamRow objects using plain SQL queries */
  implicit def GetResultTestCaseParamRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[String]], e3: GR[Option[Int]]): GR[TestCaseParamRow] = GR{
    prs => import prs._
    TestCaseParamRow.tupled((<<[Int], <<[String], <<[Int], <<[Int], <<?[String], <<?[Int]))
  }
  /** Table description of table test_case_param. Objects of this class serve as prototypes for rows in queries.
   *  NOTE: The following names collided with Scala keywords and were escaped: type */
  class TestCaseParam(_tableTag: Tag) extends profile.api.Table[TestCaseParamRow](_tableTag, Some("acs"), "test_case_param") {
    def * = (id, `type`, caseId, unitTypeParamId, value, notification) <> (TestCaseParamRow.tupled, TestCaseParamRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(`type`), Rep.Some(caseId), Rep.Some(unitTypeParamId), value, notification)).shaped.<>({r=>import r._; _1.map(_=> TestCaseParamRow.tupled((_1.get, _2.get, _3.get, _4.get, _5, _6)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column type SqlType(VARCHAR), Length(16,true)
     *  NOTE: The name was escaped because it collided with a Scala keyword. */
    val `type`: Rep[String] = column[String]("type", O.Length(16,varying=true))
    /** Database column case_id SqlType(INT) */
    val caseId: Rep[Int] = column[Int]("case_id")
    /** Database column unit_type_param_id SqlType(INT) */
    val unitTypeParamId: Rep[Int] = column[Int]("unit_type_param_id")
    /** Database column value SqlType(VARCHAR), Length(512,true), Default(None) */
    val value: Rep[Option[String]] = column[Option[String]]("value", O.Length(512,varying=true), O.Default(None))
    /** Database column notification SqlType(INT), Default(None) */
    val notification: Rep[Option[Int]] = column[Option[Int]]("notification", O.Default(None))

    /** Foreign key referencing TestCase (database name fk_test_case_param_input_case_id) */
    lazy val testCaseFk = foreignKey("fk_test_case_param_input_case_id", caseId, TestCase)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing UnitTypeParam (database name fk_test_case_param_input_u_t_p_id) */
    lazy val unitTypeParamFk = foreignKey("fk_test_case_param_input_u_t_p_id", unitTypeParamId, UnitTypeParam)(r => r.unitTypeParamId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table TestCaseParam */
  lazy val TestCaseParam = new TableQuery(tag => new TestCaseParam(tag))

  /** Entity class storing rows of table TestHistory
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param unitTypeId Database column unit_type_id SqlType(INT)
   *  @param unitId Database column unit_id SqlType(VARCHAR), Length(64,true)
   *  @param caseId Database column case_id SqlType(INT)
   *  @param startTimestamp Database column start_timestamp SqlType(DATETIME)
   *  @param endTimestamp Database column end_timestamp SqlType(DATETIME), Default(None)
   *  @param failed Database column failed SqlType(INT), Default(0)
   *  @param expectError Database column expect_error SqlType(INT), Default(0)
   *  @param result Database column result SqlType(VARCHAR), Length(4096,true), Default(None) */
  case class TestHistoryRow(id: Int, unitTypeId: Int, unitId: String, caseId: Int, startTimestamp: java.sql.Timestamp, endTimestamp: Option[java.sql.Timestamp] = None, failed: Int = 0, expectError: Int = 0, result: Option[String] = None)
  /** GetResult implicit for fetching TestHistoryRow objects using plain SQL queries */
  implicit def GetResultTestHistoryRow(implicit e0: GR[Int], e1: GR[String], e2: GR[java.sql.Timestamp], e3: GR[Option[java.sql.Timestamp]], e4: GR[Option[String]]): GR[TestHistoryRow] = GR{
    prs => import prs._
    TestHistoryRow.tupled((<<[Int], <<[Int], <<[String], <<[Int], <<[java.sql.Timestamp], <<?[java.sql.Timestamp], <<[Int], <<[Int], <<?[String]))
  }
  /** Table description of table test_history. Objects of this class serve as prototypes for rows in queries. */
  class TestHistory(_tableTag: Tag) extends profile.api.Table[TestHistoryRow](_tableTag, Some("acs"), "test_history") {
    def * = (id, unitTypeId, unitId, caseId, startTimestamp, endTimestamp, failed, expectError, result) <> (TestHistoryRow.tupled, TestHistoryRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(unitTypeId), Rep.Some(unitId), Rep.Some(caseId), Rep.Some(startTimestamp), endTimestamp, Rep.Some(failed), Rep.Some(expectError), result)).shaped.<>({r=>import r._; _1.map(_=> TestHistoryRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6, _7.get, _8.get, _9)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column unit_type_id SqlType(INT) */
    val unitTypeId: Rep[Int] = column[Int]("unit_type_id")
    /** Database column unit_id SqlType(VARCHAR), Length(64,true) */
    val unitId: Rep[String] = column[String]("unit_id", O.Length(64,varying=true))
    /** Database column case_id SqlType(INT) */
    val caseId: Rep[Int] = column[Int]("case_id")
    /** Database column start_timestamp SqlType(DATETIME) */
    val startTimestamp: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("start_timestamp")
    /** Database column end_timestamp SqlType(DATETIME), Default(None) */
    val endTimestamp: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("end_timestamp", O.Default(None))
    /** Database column failed SqlType(INT), Default(0) */
    val failed: Rep[Int] = column[Int]("failed", O.Default(0))
    /** Database column expect_error SqlType(INT), Default(0) */
    val expectError: Rep[Int] = column[Int]("expect_error", O.Default(0))
    /** Database column result SqlType(VARCHAR), Length(4096,true), Default(None) */
    val result: Rep[Option[String]] = column[Option[String]]("result", O.Length(4096,varying=true), O.Default(None))
  }
  /** Collection-like TableQuery object for table TestHistory */
  lazy val TestHistory = new TableQuery(tag => new TestHistory(tag))

  /** Entity class storing rows of table Trigger
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(VARCHAR), Length(255,true)
   *  @param description Database column description SqlType(VARCHAR), Length(1024,true), Default(None)
   *  @param triggerType Database column trigger_type SqlType(INT), Default(0)
   *  @param notifyType Database column notify_type SqlType(INT), Default(0)
   *  @param active Database column active SqlType(INT), Default(0)
   *  @param unitTypeId Database column unit_type_id SqlType(INT)
   *  @param groupId Database column group_id SqlType(INT), Default(None)
   *  @param evalPeriodMinutes Database column eval_period_minutes SqlType(INT)
   *  @param notifyIntervalHours Database column notify_interval_hours SqlType(INT), Default(None)
   *  @param filestoreId Database column filestore_id SqlType(INT), Default(None)
   *  @param parentTriggerId Database column parent_trigger_id SqlType(INT), Default(None)
   *  @param toList Database column to_list SqlType(VARCHAR), Length(512,true), Default(None)
   *  @param syslogEventId Database column syslog_event_id SqlType(INT), Default(None)
   *  @param noEvents Database column no_events SqlType(INT), Default(None)
   *  @param noEventsPrUnit Database column no_events_pr_unit SqlType(INT), Default(None)
   *  @param noUnits Database column no_units SqlType(INT), Default(None) */
  case class TriggerRow(id: Int, name: String, description: Option[String] = None, triggerType: Int = 0, notifyType: Int = 0, active: Int = 0, unitTypeId: Int, groupId: Option[Int] = None, evalPeriodMinutes: Int, notifyIntervalHours: Option[Int] = None, filestoreId: Option[Int] = None, parentTriggerId: Option[Int] = None, toList: Option[String] = None, syslogEventId: Option[Int] = None, noEvents: Option[Int] = None, noEventsPrUnit: Option[Int] = None, noUnits: Option[Int] = None)
  /** GetResult implicit for fetching TriggerRow objects using plain SQL queries */
  implicit def GetResultTriggerRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[String]], e3: GR[Option[Int]]): GR[TriggerRow] = GR{
    prs => import prs._
    TriggerRow.tupled((<<[Int], <<[String], <<?[String], <<[Int], <<[Int], <<[Int], <<[Int], <<?[Int], <<[Int], <<?[Int], <<?[Int], <<?[Int], <<?[String], <<?[Int], <<?[Int], <<?[Int], <<?[Int]))
  }
  /** Table description of table trigger_. Objects of this class serve as prototypes for rows in queries. */
  class Trigger(_tableTag: Tag) extends profile.api.Table[TriggerRow](_tableTag, Some("acs"), "trigger_") {
    def * = (id, name, description, triggerType, notifyType, active, unitTypeId, groupId, evalPeriodMinutes, notifyIntervalHours, filestoreId, parentTriggerId, toList, syslogEventId, noEvents, noEventsPrUnit, noUnits) <> (TriggerRow.tupled, TriggerRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(name), description, Rep.Some(triggerType), Rep.Some(notifyType), Rep.Some(active), Rep.Some(unitTypeId), groupId, Rep.Some(evalPeriodMinutes), notifyIntervalHours, filestoreId, parentTriggerId, toList, syslogEventId, noEvents, noEventsPrUnit, noUnits)).shaped.<>({r=>import r._; _1.map(_=> TriggerRow.tupled((_1.get, _2.get, _3, _4.get, _5.get, _6.get, _7.get, _8, _9.get, _10, _11, _12, _13, _14, _15, _16, _17)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(VARCHAR), Length(255,true) */
    val name: Rep[String] = column[String]("name", O.Length(255,varying=true))
    /** Database column description SqlType(VARCHAR), Length(1024,true), Default(None) */
    val description: Rep[Option[String]] = column[Option[String]]("description", O.Length(1024,varying=true), O.Default(None))
    /** Database column trigger_type SqlType(INT), Default(0) */
    val triggerType: Rep[Int] = column[Int]("trigger_type", O.Default(0))
    /** Database column notify_type SqlType(INT), Default(0) */
    val notifyType: Rep[Int] = column[Int]("notify_type", O.Default(0))
    /** Database column active SqlType(INT), Default(0) */
    val active: Rep[Int] = column[Int]("active", O.Default(0))
    /** Database column unit_type_id SqlType(INT) */
    val unitTypeId: Rep[Int] = column[Int]("unit_type_id")
    /** Database column group_id SqlType(INT), Default(None) */
    val groupId: Rep[Option[Int]] = column[Option[Int]]("group_id", O.Default(None))
    /** Database column eval_period_minutes SqlType(INT) */
    val evalPeriodMinutes: Rep[Int] = column[Int]("eval_period_minutes")
    /** Database column notify_interval_hours SqlType(INT), Default(None) */
    val notifyIntervalHours: Rep[Option[Int]] = column[Option[Int]]("notify_interval_hours", O.Default(None))
    /** Database column filestore_id SqlType(INT), Default(None) */
    val filestoreId: Rep[Option[Int]] = column[Option[Int]]("filestore_id", O.Default(None))
    /** Database column parent_trigger_id SqlType(INT), Default(None) */
    val parentTriggerId: Rep[Option[Int]] = column[Option[Int]]("parent_trigger_id", O.Default(None))
    /** Database column to_list SqlType(VARCHAR), Length(512,true), Default(None) */
    val toList: Rep[Option[String]] = column[Option[String]]("to_list", O.Length(512,varying=true), O.Default(None))
    /** Database column syslog_event_id SqlType(INT), Default(None) */
    val syslogEventId: Rep[Option[Int]] = column[Option[Int]]("syslog_event_id", O.Default(None))
    /** Database column no_events SqlType(INT), Default(None) */
    val noEvents: Rep[Option[Int]] = column[Option[Int]]("no_events", O.Default(None))
    /** Database column no_events_pr_unit SqlType(INT), Default(None) */
    val noEventsPrUnit: Rep[Option[Int]] = column[Option[Int]]("no_events_pr_unit", O.Default(None))
    /** Database column no_units SqlType(INT), Default(None) */
    val noUnits: Rep[Option[Int]] = column[Option[Int]]("no_units", O.Default(None))

    /** Foreign key referencing Filestore (database name fk_trigger_filestore_id) */
    lazy val filestoreFk = foreignKey("fk_trigger_filestore_id", filestoreId, Filestore)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Group (database name fk_trigger_group_id) */
    lazy val groupFk = foreignKey("fk_trigger_group_id", groupId, Group)(r => Rep.Some(r.groupId), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing SyslogEvent (database name fk_trigger_syslog_event_id) */
    lazy val syslogEventFk = foreignKey("fk_trigger_syslog_event_id", syslogEventId, SyslogEvent)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Trigger (database name fk_trigger_parent_id) */
    lazy val triggerFk = foreignKey("fk_trigger_parent_id", parentTriggerId, Trigger)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing UnitType (database name fk_trigger_unit_type_id) */
    lazy val unitTypeFk = foreignKey("fk_trigger_unit_type_id", unitTypeId, UnitType)(r => r.unitTypeId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

    /** Uniqueness Index over (unitTypeId,name) (database name idx_trigger_unit_type_id_name) */
    val index1 = index("idx_trigger_unit_type_id_name", (unitTypeId, name), unique=true)
  }
  /** Collection-like TableQuery object for table Trigger */
  lazy val Trigger = new TableQuery(tag => new Trigger(tag))

  /** Entity class storing rows of table TriggerEvent
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param timestamp Database column timestamp_ SqlType(DATETIME)
   *  @param triggerId Database column trigger_id SqlType(INT)
   *  @param unitId Database column unit_id SqlType(VARCHAR), Length(64,true) */
  case class TriggerEventRow(id: Int, timestamp: java.sql.Timestamp, triggerId: Int, unitId: String)
  /** GetResult implicit for fetching TriggerEventRow objects using plain SQL queries */
  implicit def GetResultTriggerEventRow(implicit e0: GR[Int], e1: GR[java.sql.Timestamp], e2: GR[String]): GR[TriggerEventRow] = GR{
    prs => import prs._
    TriggerEventRow.tupled((<<[Int], <<[java.sql.Timestamp], <<[Int], <<[String]))
  }
  /** Table description of table trigger_event. Objects of this class serve as prototypes for rows in queries. */
  class TriggerEvent(_tableTag: Tag) extends profile.api.Table[TriggerEventRow](_tableTag, Some("acs"), "trigger_event") {
    def * = (id, timestamp, triggerId, unitId) <> (TriggerEventRow.tupled, TriggerEventRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(timestamp), Rep.Some(triggerId), Rep.Some(unitId))).shaped.<>({r=>import r._; _1.map(_=> TriggerEventRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column timestamp_ SqlType(DATETIME) */
    val timestamp: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("timestamp_")
    /** Database column trigger_id SqlType(INT) */
    val triggerId: Rep[Int] = column[Int]("trigger_id")
    /** Database column unit_id SqlType(VARCHAR), Length(64,true) */
    val unitId: Rep[String] = column[String]("unit_id", O.Length(64,varying=true))

    /** Foreign key referencing Trigger (database name fk_trigger_event_trigger_id) */
    lazy val triggerFk = foreignKey("fk_trigger_event_trigger_id", triggerId, Trigger)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
  }
  /** Collection-like TableQuery object for table TriggerEvent */
  lazy val TriggerEvent = new TableQuery(tag => new TriggerEvent(tag))

  /** Entity class storing rows of table TriggerRelease
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param triggerId Database column trigger_id SqlType(INT)
   *  @param noEvents Database column no_events SqlType(INT), Default(None)
   *  @param noEventsPrUnit Database column no_events_pr_unit SqlType(INT), Default(None)
   *  @param noUnits Database column no_units SqlType(INT), Default(None)
   *  @param firstEventTimestamp Database column first_event_timestamp SqlType(DATETIME)
   *  @param releaseTimestamp Database column release_timestamp SqlType(DATETIME)
   *  @param sentTimestamp Database column sent_timestamp SqlType(DATETIME), Default(None) */
  case class TriggerReleaseRow(id: Int, triggerId: Int, noEvents: Option[Int] = None, noEventsPrUnit: Option[Int] = None, noUnits: Option[Int] = None, firstEventTimestamp: java.sql.Timestamp, releaseTimestamp: java.sql.Timestamp, sentTimestamp: Option[java.sql.Timestamp] = None)
  /** GetResult implicit for fetching TriggerReleaseRow objects using plain SQL queries */
  implicit def GetResultTriggerReleaseRow(implicit e0: GR[Int], e1: GR[Option[Int]], e2: GR[java.sql.Timestamp], e3: GR[Option[java.sql.Timestamp]]): GR[TriggerReleaseRow] = GR{
    prs => import prs._
    TriggerReleaseRow.tupled((<<[Int], <<[Int], <<?[Int], <<?[Int], <<?[Int], <<[java.sql.Timestamp], <<[java.sql.Timestamp], <<?[java.sql.Timestamp]))
  }
  /** Table description of table trigger_release. Objects of this class serve as prototypes for rows in queries. */
  class TriggerRelease(_tableTag: Tag) extends profile.api.Table[TriggerReleaseRow](_tableTag, Some("acs"), "trigger_release") {
    def * = (id, triggerId, noEvents, noEventsPrUnit, noUnits, firstEventTimestamp, releaseTimestamp, sentTimestamp) <> (TriggerReleaseRow.tupled, TriggerReleaseRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(triggerId), noEvents, noEventsPrUnit, noUnits, Rep.Some(firstEventTimestamp), Rep.Some(releaseTimestamp), sentTimestamp)).shaped.<>({r=>import r._; _1.map(_=> TriggerReleaseRow.tupled((_1.get, _2.get, _3, _4, _5, _6.get, _7.get, _8)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column trigger_id SqlType(INT) */
    val triggerId: Rep[Int] = column[Int]("trigger_id")
    /** Database column no_events SqlType(INT), Default(None) */
    val noEvents: Rep[Option[Int]] = column[Option[Int]]("no_events", O.Default(None))
    /** Database column no_events_pr_unit SqlType(INT), Default(None) */
    val noEventsPrUnit: Rep[Option[Int]] = column[Option[Int]]("no_events_pr_unit", O.Default(None))
    /** Database column no_units SqlType(INT), Default(None) */
    val noUnits: Rep[Option[Int]] = column[Option[Int]]("no_units", O.Default(None))
    /** Database column first_event_timestamp SqlType(DATETIME) */
    val firstEventTimestamp: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("first_event_timestamp")
    /** Database column release_timestamp SqlType(DATETIME) */
    val releaseTimestamp: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("release_timestamp")
    /** Database column sent_timestamp SqlType(DATETIME), Default(None) */
    val sentTimestamp: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("sent_timestamp", O.Default(None))

    /** Foreign key referencing Trigger (database name fk_trigger_release_trigger_id) */
    lazy val triggerFk = foreignKey("fk_trigger_release_trigger_id", triggerId, Trigger)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table TriggerRelease */
  lazy val TriggerRelease = new TableQuery(tag => new TriggerRelease(tag))

  /** Entity class storing rows of table Unit
   *  @param unitId Database column unit_id SqlType(VARCHAR), PrimaryKey, Length(64,true)
   *  @param unitTypeId Database column unit_type_id SqlType(INT)
   *  @param profileId Database column profile_id SqlType(INT) */
  case class UnitRow(unitId: String, unitTypeId: Int, profileId: Int)
  /** GetResult implicit for fetching UnitRow objects using plain SQL queries */
  implicit def GetResultUnitRow(implicit e0: GR[String], e1: GR[Int]): GR[UnitRow] = GR{
    prs => import prs._
    UnitRow.tupled((<<[String], <<[Int], <<[Int]))
  }
  /** Table description of table unit. Objects of this class serve as prototypes for rows in queries. */
  class Unit(_tableTag: Tag) extends profile.api.Table[UnitRow](_tableTag, Some("acs"), "unit") {
    def * = (unitId, unitTypeId, profileId) <> (UnitRow.tupled, UnitRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(unitId), Rep.Some(unitTypeId), Rep.Some(profileId))).shaped.<>({r=>import r._; _1.map(_=> UnitRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column unit_id SqlType(VARCHAR), PrimaryKey, Length(64,true) */
    val unitId: Rep[String] = column[String]("unit_id", O.PrimaryKey, O.Length(64,varying=true))
    /** Database column unit_type_id SqlType(INT) */
    val unitTypeId: Rep[Int] = column[Int]("unit_type_id")
    /** Database column profile_id SqlType(INT) */
    val profileId: Rep[Int] = column[Int]("profile_id")

    /** Foreign key referencing Profile (database name fk_unit_profile_id) */
    lazy val profileFk = foreignKey("fk_unit_profile_id", profileId, Profile)(r => r.profileId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing UnitType (database name fk_unit_unit_type_id) */
    lazy val unitTypeFk = foreignKey("fk_unit_unit_type_id", unitTypeId, UnitType)(r => r.unitTypeId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

    /** Index over (profileId,unitTypeId,unitId) (database name idx_unit_profile_unit_type) */
    val index1 = index("idx_unit_profile_unit_type", (profileId, unitTypeId, unitId))
    /** Index over (unitTypeId,profileId,unitId) (database name idx_unit_unit_type_profile) */
    val index2 = index("idx_unit_unit_type_profile", (unitTypeId, profileId, unitId))
  }
  /** Collection-like TableQuery object for table Unit */
  lazy val Unit = new TableQuery(tag => new Unit(tag))

  /** Entity class storing rows of table UnitJob
   *  @param unitId Database column unit_id SqlType(VARCHAR), Length(64,true)
   *  @param jobId Database column job_id SqlType(INT)
   *  @param startTimestamp Database column start_timestamp SqlType(DATETIME)
   *  @param endTimestamp Database column end_timestamp SqlType(DATETIME), Default(None)
   *  @param status Database column status SqlType(VARCHAR), Length(32,true)
   *  @param processed Database column processed SqlType(INT), Default(Some(0))
   *  @param confirmed Database column confirmed SqlType(INT), Default(Some(0))
   *  @param unconfirmed Database column unconfirmed SqlType(INT), Default(Some(0)) */
  case class UnitJobRow(unitId: String, jobId: Int, startTimestamp: java.sql.Timestamp, endTimestamp: Option[java.sql.Timestamp] = None, status: String, processed: Option[Int] = Some(0), confirmed: Option[Int] = Some(0), unconfirmed: Option[Int] = Some(0))
  /** GetResult implicit for fetching UnitJobRow objects using plain SQL queries */
  implicit def GetResultUnitJobRow(implicit e0: GR[String], e1: GR[Int], e2: GR[java.sql.Timestamp], e3: GR[Option[java.sql.Timestamp]], e4: GR[Option[Int]]): GR[UnitJobRow] = GR{
    prs => import prs._
    UnitJobRow.tupled((<<[String], <<[Int], <<[java.sql.Timestamp], <<?[java.sql.Timestamp], <<[String], <<?[Int], <<?[Int], <<?[Int]))
  }
  /** Table description of table unit_job. Objects of this class serve as prototypes for rows in queries. */
  class UnitJob(_tableTag: Tag) extends profile.api.Table[UnitJobRow](_tableTag, Some("acs"), "unit_job") {
    def * = (unitId, jobId, startTimestamp, endTimestamp, status, processed, confirmed, unconfirmed) <> (UnitJobRow.tupled, UnitJobRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(unitId), Rep.Some(jobId), Rep.Some(startTimestamp), endTimestamp, Rep.Some(status), processed, confirmed, unconfirmed)).shaped.<>({r=>import r._; _1.map(_=> UnitJobRow.tupled((_1.get, _2.get, _3.get, _4, _5.get, _6, _7, _8)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column unit_id SqlType(VARCHAR), Length(64,true) */
    val unitId: Rep[String] = column[String]("unit_id", O.Length(64,varying=true))
    /** Database column job_id SqlType(INT) */
    val jobId: Rep[Int] = column[Int]("job_id")
    /** Database column start_timestamp SqlType(DATETIME) */
    val startTimestamp: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("start_timestamp")
    /** Database column end_timestamp SqlType(DATETIME), Default(None) */
    val endTimestamp: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("end_timestamp", O.Default(None))
    /** Database column status SqlType(VARCHAR), Length(32,true) */
    val status: Rep[String] = column[String]("status", O.Length(32,varying=true))
    /** Database column processed SqlType(INT), Default(Some(0)) */
    val processed: Rep[Option[Int]] = column[Option[Int]]("processed", O.Default(Some(0)))
    /** Database column confirmed SqlType(INT), Default(Some(0)) */
    val confirmed: Rep[Option[Int]] = column[Option[Int]]("confirmed", O.Default(Some(0)))
    /** Database column unconfirmed SqlType(INT), Default(Some(0)) */
    val unconfirmed: Rep[Option[Int]] = column[Option[Int]]("unconfirmed", O.Default(Some(0)))

    /** Primary key of UnitJob (database name unit_job_PK) */
    val pk = primaryKey("unit_job_PK", (unitId, jobId))

    /** Foreign key referencing Job (database name fk_unit_job_job_id) */
    lazy val jobFk = foreignKey("fk_unit_job_job_id", jobId, Job)(r => r.jobId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Unit (database name fk_unit_job_unit_id) */
    lazy val unitFk = foreignKey("fk_unit_job_unit_id", unitId, Unit)(r => r.unitId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

    /** Index over (status,startTimestamp) (database name idx_unit_job_1) */
    val index1 = index("idx_unit_job_1", (status, startTimestamp))
    /** Index over (processed) (database name idx_unit_job_2) */
    val index2 = index("idx_unit_job_2", processed)
  }
  /** Collection-like TableQuery object for table UnitJob */
  lazy val UnitJob = new TableQuery(tag => new UnitJob(tag))

  /** Entity class storing rows of table UnitParam
   *  @param unitId Database column unit_id SqlType(VARCHAR), Length(64,true)
   *  @param unitTypeParamId Database column unit_type_param_id SqlType(INT)
   *  @param value Database column value SqlType(VARCHAR), Length(512,true), Default(None) */
  case class UnitParamRow(unitId: String, unitTypeParamId: Int, value: Option[String] = None)
  /** GetResult implicit for fetching UnitParamRow objects using plain SQL queries */
  implicit def GetResultUnitParamRow(implicit e0: GR[String], e1: GR[Int], e2: GR[Option[String]]): GR[UnitParamRow] = GR{
    prs => import prs._
    UnitParamRow.tupled((<<[String], <<[Int], <<?[String]))
  }
  /** Table description of table unit_param. Objects of this class serve as prototypes for rows in queries. */
  class UnitParam(_tableTag: Tag) extends profile.api.Table[UnitParamRow](_tableTag, Some("acs"), "unit_param") {
    def * = (unitId, unitTypeParamId, value) <> (UnitParamRow.tupled, UnitParamRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(unitId), Rep.Some(unitTypeParamId), value)).shaped.<>({r=>import r._; _1.map(_=> UnitParamRow.tupled((_1.get, _2.get, _3)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column unit_id SqlType(VARCHAR), Length(64,true) */
    val unitId: Rep[String] = column[String]("unit_id", O.Length(64,varying=true))
    /** Database column unit_type_param_id SqlType(INT) */
    val unitTypeParamId: Rep[Int] = column[Int]("unit_type_param_id")
    /** Database column value SqlType(VARCHAR), Length(512,true), Default(None) */
    val value: Rep[Option[String]] = column[Option[String]]("value", O.Length(512,varying=true), O.Default(None))

    /** Primary key of UnitParam (database name unit_param_PK) */
    val pk = primaryKey("unit_param_PK", (unitId, unitTypeParamId))

    /** Foreign key referencing Unit (database name fk_unit_param_unit_id) */
    lazy val unitFk = foreignKey("fk_unit_param_unit_id", unitId, Unit)(r => r.unitId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing UnitTypeParam (database name fk_unit_param_u_t_p_id) */
    lazy val unitTypeParamFk = foreignKey("fk_unit_param_u_t_p_id", unitTypeParamId, UnitTypeParam)(r => r.unitTypeParamId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

    /** Index over (unitTypeParamId,value) (database name idx_unit_param_type_id2) */
    val index1 = index("idx_unit_param_type_id2", (unitTypeParamId, value))
    /** Index over (value) (database name idx_unit_param_value) */
    val index2 = index("idx_unit_param_value", value)
  }
  /** Collection-like TableQuery object for table UnitParam */
  lazy val UnitParam = new TableQuery(tag => new UnitParam(tag))

  /** Entity class storing rows of table UnitParamSession
   *  @param unitId Database column unit_id SqlType(VARCHAR), Length(64,true)
   *  @param unitTypeParamId Database column unit_type_param_id SqlType(INT)
   *  @param value Database column value SqlType(VARCHAR), Length(512,true), Default(None) */
  case class UnitParamSessionRow(unitId: String, unitTypeParamId: Int, value: Option[String] = None)
  /** GetResult implicit for fetching UnitParamSessionRow objects using plain SQL queries */
  implicit def GetResultUnitParamSessionRow(implicit e0: GR[String], e1: GR[Int], e2: GR[Option[String]]): GR[UnitParamSessionRow] = GR{
    prs => import prs._
    UnitParamSessionRow.tupled((<<[String], <<[Int], <<?[String]))
  }
  /** Table description of table unit_param_session. Objects of this class serve as prototypes for rows in queries. */
  class UnitParamSession(_tableTag: Tag) extends profile.api.Table[UnitParamSessionRow](_tableTag, Some("acs"), "unit_param_session") {
    def * = (unitId, unitTypeParamId, value) <> (UnitParamSessionRow.tupled, UnitParamSessionRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(unitId), Rep.Some(unitTypeParamId), value)).shaped.<>({r=>import r._; _1.map(_=> UnitParamSessionRow.tupled((_1.get, _2.get, _3)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column unit_id SqlType(VARCHAR), Length(64,true) */
    val unitId: Rep[String] = column[String]("unit_id", O.Length(64,varying=true))
    /** Database column unit_type_param_id SqlType(INT) */
    val unitTypeParamId: Rep[Int] = column[Int]("unit_type_param_id")
    /** Database column value SqlType(VARCHAR), Length(512,true), Default(None) */
    val value: Rep[Option[String]] = column[Option[String]]("value", O.Length(512,varying=true), O.Default(None))

    /** Primary key of UnitParamSession (database name unit_param_session_PK) */
    val pk = primaryKey("unit_param_session_PK", (unitId, unitTypeParamId))

    /** Foreign key referencing Unit (database name fk_unit_param_session_unit_id) */
    lazy val unitFk = foreignKey("fk_unit_param_session_unit_id", unitId, Unit)(r => r.unitId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
    /** Foreign key referencing UnitTypeParam (database name fk_unit_param_session_u_t_p_id) */
    lazy val unitTypeParamFk = foreignKey("fk_unit_param_session_u_t_p_id", unitTypeParamId, UnitTypeParam)(r => r.unitTypeParamId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table UnitParamSession */
  lazy val UnitParamSession = new TableQuery(tag => new UnitParamSession(tag))

  /** Entity class storing rows of table UnitType
   *  @param unitTypeId Database column unit_type_id SqlType(INT), AutoInc, PrimaryKey
   *  @param matcherId Database column matcher_id SqlType(VARCHAR), Length(32,true), Default(None)
   *  @param unitTypeName Database column unit_type_name SqlType(VARCHAR), Length(64,true)
   *  @param vendorName Database column vendor_name SqlType(VARCHAR), Length(64,true), Default(None)
   *  @param description Database column description SqlType(VARCHAR), Length(2000,true), Default(None)
   *  @param protocol Database column protocol SqlType(VARCHAR), Length(16,true) */
  case class UnitTypeRow(unitTypeId: Int, matcherId: Option[String] = None, unitTypeName: String, vendorName: Option[String] = None, description: Option[String] = None, protocol: String)
  /** GetResult implicit for fetching UnitTypeRow objects using plain SQL queries */
  implicit def GetResultUnitTypeRow(implicit e0: GR[Int], e1: GR[Option[String]], e2: GR[String]): GR[UnitTypeRow] = GR{
    prs => import prs._
    UnitTypeRow.tupled((<<[Int], <<?[String], <<[String], <<?[String], <<?[String], <<[String]))
  }
  /** Table description of table unit_type. Objects of this class serve as prototypes for rows in queries. */
  class UnitType(_tableTag: Tag) extends profile.api.Table[UnitTypeRow](_tableTag, Some("acs"), "unit_type") {
    def * = (unitTypeId, matcherId, unitTypeName, vendorName, description, protocol) <> (UnitTypeRow.tupled, UnitTypeRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(unitTypeId), matcherId, Rep.Some(unitTypeName), vendorName, description, Rep.Some(protocol))).shaped.<>({r=>import r._; _1.map(_=> UnitTypeRow.tupled((_1.get, _2, _3.get, _4, _5, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column unit_type_id SqlType(INT), AutoInc, PrimaryKey */
    val unitTypeId: Rep[Int] = column[Int]("unit_type_id", O.AutoInc, O.PrimaryKey)
    /** Database column matcher_id SqlType(VARCHAR), Length(32,true), Default(None) */
    val matcherId: Rep[Option[String]] = column[Option[String]]("matcher_id", O.Length(32,varying=true), O.Default(None))
    /** Database column unit_type_name SqlType(VARCHAR), Length(64,true) */
    val unitTypeName: Rep[String] = column[String]("unit_type_name", O.Length(64,varying=true))
    /** Database column vendor_name SqlType(VARCHAR), Length(64,true), Default(None) */
    val vendorName: Rep[Option[String]] = column[Option[String]]("vendor_name", O.Length(64,varying=true), O.Default(None))
    /** Database column description SqlType(VARCHAR), Length(2000,true), Default(None) */
    val description: Rep[Option[String]] = column[Option[String]]("description", O.Length(2000,varying=true), O.Default(None))
    /** Database column protocol SqlType(VARCHAR), Length(16,true) */
    val protocol: Rep[String] = column[String]("protocol", O.Length(16,varying=true))

    /** Uniqueness Index over (unitTypeName) (database name uq_unit_type_name) */
    val index1 = index("uq_unit_type_name", unitTypeName, unique=true)
  }
  /** Collection-like TableQuery object for table UnitType */
  lazy val UnitType = new TableQuery(tag => new UnitType(tag))

  /** Entity class storing rows of table UnitTypeParam
   *  @param unitTypeParamId Database column unit_type_param_id SqlType(INT), AutoInc, PrimaryKey
   *  @param unitTypeId Database column unit_type_id SqlType(INT)
   *  @param name Database column name SqlType(VARCHAR), Length(255,true)
   *  @param flags Database column flags SqlType(VARCHAR), Length(32,true) */
  case class UnitTypeParamRow(unitTypeParamId: Int, unitTypeId: Int, name: String, flags: String)
  /** GetResult implicit for fetching UnitTypeParamRow objects using plain SQL queries */
  implicit def GetResultUnitTypeParamRow(implicit e0: GR[Int], e1: GR[String]): GR[UnitTypeParamRow] = GR{
    prs => import prs._
    UnitTypeParamRow.tupled((<<[Int], <<[Int], <<[String], <<[String]))
  }
  /** Table description of table unit_type_param. Objects of this class serve as prototypes for rows in queries. */
  class UnitTypeParam(_tableTag: Tag) extends profile.api.Table[UnitTypeParamRow](_tableTag, Some("acs"), "unit_type_param") {
    def * = (unitTypeParamId, unitTypeId, name, flags) <> (UnitTypeParamRow.tupled, UnitTypeParamRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(unitTypeParamId), Rep.Some(unitTypeId), Rep.Some(name), Rep.Some(flags))).shaped.<>({r=>import r._; _1.map(_=> UnitTypeParamRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column unit_type_param_id SqlType(INT), AutoInc, PrimaryKey */
    val unitTypeParamId: Rep[Int] = column[Int]("unit_type_param_id", O.AutoInc, O.PrimaryKey)
    /** Database column unit_type_id SqlType(INT) */
    val unitTypeId: Rep[Int] = column[Int]("unit_type_id")
    /** Database column name SqlType(VARCHAR), Length(255,true) */
    val name: Rep[String] = column[String]("name", O.Length(255,varying=true))
    /** Database column flags SqlType(VARCHAR), Length(32,true) */
    val flags: Rep[String] = column[String]("flags", O.Length(32,varying=true))

    /** Foreign key referencing UnitType (database name fk_u_t_p_unit_type_id) */
    lazy val unitTypeFk = foreignKey("fk_u_t_p_unit_type_id", unitTypeId, UnitType)(r => r.unitTypeId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

    /** Uniqueness Index over (unitTypeId,name) (database name idx_u_t_p_unit_type_id_name) */
    val index1 = index("idx_u_t_p_unit_type_id_name", (unitTypeId, name), unique=true)
  }
  /** Collection-like TableQuery object for table UnitTypeParam */
  lazy val UnitTypeParam = new TableQuery(tag => new UnitTypeParam(tag))

  /** Entity class storing rows of table UnitTypeParamValue
   *  @param unitTypeParamId Database column unit_type_param_id SqlType(INT)
   *  @param value Database column value SqlType(VARCHAR), Length(255,true)
   *  @param priority Database column priority SqlType(INT)
   *  @param `type` Database column type SqlType(VARCHAR), Length(32,true), Default(enum) */
  case class UnitTypeParamValueRow(unitTypeParamId: Int, value: String, priority: Int, `type`: String = "enum")
  /** GetResult implicit for fetching UnitTypeParamValueRow objects using plain SQL queries */
  implicit def GetResultUnitTypeParamValueRow(implicit e0: GR[Int], e1: GR[String]): GR[UnitTypeParamValueRow] = GR{
    prs => import prs._
    UnitTypeParamValueRow.tupled((<<[Int], <<[String], <<[Int], <<[String]))
  }
  /** Table description of table unit_type_param_value. Objects of this class serve as prototypes for rows in queries.
   *  NOTE: The following names collided with Scala keywords and were escaped: type */
  class UnitTypeParamValue(_tableTag: Tag) extends profile.api.Table[UnitTypeParamValueRow](_tableTag, Some("acs"), "unit_type_param_value") {
    def * = (unitTypeParamId, value, priority, `type`) <> (UnitTypeParamValueRow.tupled, UnitTypeParamValueRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(unitTypeParamId), Rep.Some(value), Rep.Some(priority), Rep.Some(`type`))).shaped.<>({r=>import r._; _1.map(_=> UnitTypeParamValueRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column unit_type_param_id SqlType(INT) */
    val unitTypeParamId: Rep[Int] = column[Int]("unit_type_param_id")
    /** Database column value SqlType(VARCHAR), Length(255,true) */
    val value: Rep[String] = column[String]("value", O.Length(255,varying=true))
    /** Database column priority SqlType(INT) */
    val priority: Rep[Int] = column[Int]("priority")
    /** Database column type SqlType(VARCHAR), Length(32,true), Default(enum)
     *  NOTE: The name was escaped because it collided with a Scala keyword. */
    val `type`: Rep[String] = column[String]("type", O.Length(32,varying=true), O.Default("enum"))

    /** Primary key of UnitTypeParamValue (database name unit_type_param_value_PK) */
    val pk = primaryKey("unit_type_param_value_PK", (unitTypeParamId, value))

    /** Foreign key referencing UnitTypeParam (database name fk_unit_param_value_utpid) */
    lazy val unitTypeParamFk = foreignKey("fk_unit_param_value_utpid", unitTypeParamId, UnitTypeParam)(r => r.unitTypeParamId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table UnitTypeParamValue */
  lazy val UnitTypeParamValue = new TableQuery(tag => new UnitTypeParamValue(tag))

  /** Entity class storing rows of table User
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param username Database column username SqlType(VARCHAR), Length(64,true)
   *  @param secret Database column secret SqlType(VARCHAR), Length(64,true)
   *  @param fullname Database column fullname SqlType(VARCHAR), Length(64,true)
   *  @param accesslist Database column accesslist SqlType(VARCHAR), Length(256,true)
   *  @param isAdmin Database column is_admin SqlType(INT), Default(0) */
  case class UserRow(id: Int, username: String, secret: String, fullname: String, accesslist: String, isAdmin: Int = 0)
  /** GetResult implicit for fetching UserRow objects using plain SQL queries */
  implicit def GetResultUserRow(implicit e0: GR[Int], e1: GR[String]): GR[UserRow] = GR{
    prs => import prs._
    UserRow.tupled((<<[Int], <<[String], <<[String], <<[String], <<[String], <<[Int]))
  }
  /** Table description of table user_. Objects of this class serve as prototypes for rows in queries. */
  class User(_tableTag: Tag) extends profile.api.Table[UserRow](_tableTag, Some("acs"), "user_") {
    def * = (id, username, secret, fullname, accesslist, isAdmin) <> (UserRow.tupled, UserRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(username), Rep.Some(secret), Rep.Some(fullname), Rep.Some(accesslist), Rep.Some(isAdmin))).shaped.<>({r=>import r._; _1.map(_=> UserRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column username SqlType(VARCHAR), Length(64,true) */
    val username: Rep[String] = column[String]("username", O.Length(64,varying=true))
    /** Database column secret SqlType(VARCHAR), Length(64,true) */
    val secret: Rep[String] = column[String]("secret", O.Length(64,varying=true))
    /** Database column fullname SqlType(VARCHAR), Length(64,true) */
    val fullname: Rep[String] = column[String]("fullname", O.Length(64,varying=true))
    /** Database column accesslist SqlType(VARCHAR), Length(256,true) */
    val accesslist: Rep[String] = column[String]("accesslist", O.Length(256,varying=true))
    /** Database column is_admin SqlType(INT), Default(0) */
    val isAdmin: Rep[Int] = column[Int]("is_admin", O.Default(0))

    /** Uniqueness Index over (username) (database name idx_username) */
    val index1 = index("idx_username", username, unique=true)
  }
  /** Collection-like TableQuery object for table User */
  lazy val User = new TableQuery(tag => new User(tag))
}
