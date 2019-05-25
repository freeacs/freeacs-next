package services

import com.google.inject.{Inject, Singleton}
import daos.Tables.ProfileRow
import models._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ProfileService @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
    extends HasDatabaseConfigProvider[JdbcProfile] {

  import daos.Tables.{UnitTypeRow, Profile => ProfileDao, UnitType => UnitTypeDao}
  import profile.api._

  def createOrFail(name: String, unitTypeId: Int)(
      implicit ec: ExecutionContext
  ): Future[Either[String, Int]] =
    db.run(createProfileQuery(name, unitTypeId)).map(Right.apply).recoverWith {
      case e => Future.successful(Left("Failed to create profile: " + e.getLocalizedMessage))
    }

  def list(implicit ec: ExecutionContext): Future[Either[String, Seq[AcsProfile]]] =
    db.run(
        for {
          profileRows <- ProfileDao.join(UnitTypeDao).on(_.unitTypeId === _.unitTypeId).result
        } yield profileRows.map(mapToProfile)
      )
      .map(Right.apply)
      .recoverWith {
        case e: Exception => Future.successful(Left(s"Failed get profiles: ${e.getLocalizedMessage}"))
      }

  def createProfileQuery(name: String, unitTypeId: Int)(implicit ec: ExecutionContext): DBIO[Int] =
    for {
      profile <- ProfileDao
                  .filter(_.profileName === name)
                  .filter(_.unitTypeId === unitTypeId)
                  .result
                  .headOption
      profileId <- if (profile.isEmpty)
                    ProfileDao returning ProfileDao.map(_.profileId) += ProfileRow(-1, unitTypeId, name)
                  else
                    DBIO.successful(profile.map(_.profileId).get)
    } yield profileId

  private def mapToProfile(row: (ProfileRow, UnitTypeRow)) =
    AcsProfile(
      row._1.profileId,
      row._1.profileName,
      AcsUnitType(
        unitTypeId = Some(row._1.unitTypeId),
        name = row._2.unitTypeName,
        vendor = row._2.vendorName.orNull,
        description = row._2.description.orNull,
        protocol = AcsProtocol.unsafeFromString(row._2.protocol)
      )
    )

}
