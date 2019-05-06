package services

import freeacs.dbi.{Profile, ProvisioningProtocol, Unittype}
import freeacs.dbi.util.SystemParameters
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class UnitService(dbConfig: DatabaseConfig[JdbcProfile])(implicit ec: ExecutionContext) {

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

  def create(unitId: String, unitTypeId: Int, profileId: Int): Future[Int] =
    db.run(UnitDao += UnitRow(unitId, unitTypeId, profileId))

  def list: Future[Seq[freeacs.dbi.Unit]] = {
    db.run(
      for {
        result <- UnitDao
                   .join(ProfileDao)
                   .on(_.profileId === _.profileId)
                   .join(UnitTypeDao)
                   .on(_._1.unitTypeId === _.unitTypeId)
                   .result
      } yield
        result.map(
          t =>
            new freeacs.dbi.Unit(
              t._1._1.unitId,
              unitTypeRowToUnittype(t._2),
              new Profile(
                t._1._2.profileId,
                t._1._2.profileName,
                unitTypeRowToUnittype(t._2)
              )
          )
        )
    )
  }

  private def unitTypeRowToUnittype(unitTypeRow: _root_.daos.Tables.UnitTypeRow) = {
    new Unittype(
      unitTypeRow.unitTypeId,
      unitTypeRow.unitTypeName,
      unitTypeRow.vendorName.orNull,
      unitTypeRow.description.orNull,
      ProvisioningProtocol.valueOf(unitTypeRow.protocol)
    )
  }

  def getSecret(unitId: String): Future[Option[String]] = {
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
  }
}
