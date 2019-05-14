package services

import models._
import daos.Tables.ProfileRow
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class ProfileService(val dbConfig: DatabaseConfig[JdbcProfile]) {

  import daos.Tables.{Profile => ProfileDao, UnitType => UnitTypeDao}
  import dbConfig._
  import dbConfig.profile.api._

  def createOrFail(name: String, unitTypeId: Int)(
      implicit ec: ExecutionContext
  ): Future[Either[String, Int]] =
    db.run(createProfileQuery(name, unitTypeId)).map(Right.apply).recoverWith {
      case e => Future.successful(Left("Failed to create profile: " + e.getLocalizedMessage))
    }

  def list(implicit ec: ExecutionContext): Future[Either[String, Seq[AcsProfile]]] = {
    db.run(
        for {
          unitTypeRows <- ProfileDao.join(UnitTypeDao).on(_.unitTypeId === _.unitTypeId).result
        } yield
          unitTypeRows.map(
            row =>
              AcsProfile(
                Some(row._1.profileId),
                row._1.profileName,
                AcsUnitType(
                  unitTypeId = Some(row._1.unitTypeId),
                  name = row._2.unitTypeName,
                  vendor = row._2.vendorName.orNull,
                  description = row._2.description.orNull,
                  protocol = AcsProtocol.unsafeFromString(row._2.protocol)
                )
            )
          )
      )
      .map(Right.apply)
      .recoverWith {
        case e: Exception => Future.successful(Left(s"Failed get profiles: ${e.getLocalizedMessage}"))
      }
  }

  def createProfileQuery(name: String, unitTypeId: Int)(implicit ec: ExecutionContext): DBIO[Int] =
    ProfileDao returning ProfileDao.map(_.profileId) += ProfileRow(-1, unitTypeId, name)

}
