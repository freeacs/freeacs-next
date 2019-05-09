package services

import models._
import daos.Tables.ProfileRow
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class ProfileService(dbConfig: DatabaseConfig[JdbcProfile]) {

  import daos.Tables.{Profile => ProfileDao, UnitType => UnitTypeDao}
  import dbConfig._
  import dbConfig.profile.api._

  def create(name: String, unitTypeId: Int)(implicit ec: ExecutionContext): Future[Either[String, Int]] =
    db.run(ProfileDao += ProfileRow(-1, unitTypeId, name)).map(Right.apply).recoverWith {
      case e: Exception => Future.successful(Left(s"Failed to create profile $name: ${e.getLocalizedMessage}"))
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
                  Some(row._1.unitTypeId),
                  row._2.unitTypeName,
                  row._2.vendorName.orNull,
                  row._2.description.orNull,
                  AcsProtocol.unsafeFromString(row._2.protocol)
                )
            )
          )
      )
      .map(Right.apply)
      .recoverWith {
        case e: Exception => Future.successful(Left(s"Failed get profiles: ${e.getLocalizedMessage}"))
      }
  }

}
