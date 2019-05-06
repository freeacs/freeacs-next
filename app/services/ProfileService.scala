package services

import daos.Tables.ProfileRow
import freeacs.dbi.{Profile, ProvisioningProtocol, Unittype}
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class ProfileService(dbConfig: DatabaseConfig[JdbcProfile])(implicit ec: ExecutionContext) {

  import daos.Tables.{Profile => ProfileDao, UnitType => UnitTypeDao}
  import dbConfig._
  import dbConfig.profile.api._

  def create(profile: Profile): Future[Int] = {
    db.run(for {
      numsInserted <- ProfileDao += ProfileRow(
                       profile.getId,
                       profile.getUnittype.getId,
                       profile.getName
                     )
    } yield numsInserted)
  }

  def list: Future[Seq[Profile]] = {
    db.run(for {
      unitTypeRows <- ProfileDao.join(UnitTypeDao).on(_.unitTypeId === _.unitTypeId).result
      unitTypes = unitTypeRows.map(
        row =>
          new Profile(
            row._1.profileId,
            row._1.profileName,
            new Unittype(
              row._1.unitTypeId,
              row._2.unitTypeName,
              row._2.vendorName.orNull,
              row._2.description.orNull,
              ProvisioningProtocol.valueOf(row._2.protocol)
            )
        )
      )
    } yield unitTypes)
  }

}
