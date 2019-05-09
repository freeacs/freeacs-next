package services

import freeacs.dbi.{Profile, ProvisioningProtocol, Unittype}
import freeacs.dbi.util.SystemParameters
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class UnitService(dbConfig: DatabaseConfig[JdbcProfile]) {

  import daos.Tables.{
    Unit => UnitDao,
    UnitRow,
    Profile => ProfileDao,
    UnitType => UnitTypeDao,
    UnitParam => UnitParamDao,
    UnitTypeParam => UnitTypeParamDao
  }

  import dbConfig._
  import dbConfig.profile.api._

  def find(unitId: String)(implicit ec: ExecutionContext): Future[Option[freeacs.dbi.Unit]] =
    db.run(
      for {
        result <- getUnitQuery.filter(_._1._1.unitId === unitId).result.headOption
      } yield result.map(mapToUnit)
    )

  def count(implicit ec: ExecutionContext): Future[Int] =
    db.run(UnitDao.length.result)

  def create(unitId: String, unitTypeId: Int, profileId: Int)(implicit ec: ExecutionContext): Future[Int] =
    db.run(UnitDao += UnitRow(unitId, unitTypeId, profileId))

  def list(implicit ec: ExecutionContext): Future[Seq[freeacs.dbi.Unit]] =
    db.run(
      for {
        result <- getUnitQuery.result
      } yield result.map(mapToUnit)
    )

  def getSecret(unitId: String)(implicit ec: ExecutionContext): Future[Option[String]] =
    db.run(for {
      result <- UnitParamDao
                 .join(UnitTypeParamDao)
                 .on(
                   (up, utp) =>
                     up.unitTypeParamId === utp.unitTypeParamId
                       && up.unitId === unitId
                       && utp.name === SystemParameters.SECRET
                 )
                 .map(_._1.value)
                 .result
    } yield if (result.isEmpty) None else result.head)

  private def getUnitQuery =
    UnitDao.join(ProfileDao).on(_.profileId === _.profileId).join(UnitTypeDao).on(_._1.unitTypeId === _.unitTypeId)

  private def mapToUnit(t: ((daos.Tables.UnitRow, daos.Tables.ProfileRow), daos.Tables.UnitTypeRow)) =
    new freeacs.dbi.Unit(
      t._1._1.unitId,
      new Unittype(
        t._2.unitTypeId,
        t._2.unitTypeName,
        t._2.vendorName.orNull,
        t._2.description.orNull,
        ProvisioningProtocol.valueOf(t._2.protocol)
      ),
      new Profile(
        t._1._2.profileId,
        t._1._2.profileName,
        new Unittype(
          t._2.unitTypeId,
          t._2.unitTypeName,
          t._2.vendorName.orNull,
          t._2.description.orNull,
          ProvisioningProtocol.valueOf(t._2.protocol)
        )
      )
    )
}
