package services

import models._
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class UnitService(dbConfig: DatabaseConfig[JdbcProfile], unitTypeService: UnitTypeService) {

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

  def find(unitId: String)(implicit ec: ExecutionContext): Future[Option[AcsUnit]] =
    db.run(
        for {
          result <- getUnitQuery.filter(_._1._1.unitId === unitId).result.headOption
          params <- UnitParamDao
                     .join(UnitTypeParamDao)
                     .on(_.unitTypeParamId === _.unitTypeParamId)
                     .filter(_._1.unitId === unitId)
                     .result
        } yield {
          result.map(mapToUnit).map { unit =>
            unit.copy(params = params.map(mapToUnitParam))
          }
        }
      )
      .flatMap {
        case Some(unit) =>
          unitTypeService
            .params(unit.profile.unitType.unitTypeId.get)
            .map(
              params =>
                Some(
                  unit
                    .copy(profile = unit.profile.copy(unitType = unit.profile.unitType.copy(params = params)))
              )
            )
        case _ =>
          Future.successful(None)
      }

  def count(implicit ec: ExecutionContext): Future[Int] =
    db.run(UnitDao.length.result)

  def create(unitId: String, unitTypeId: Int, profileId: Int)(
      implicit ec: ExecutionContext
  ): Future[Either[String, Int]] =
    db.run(UnitDao += UnitRow(unitId, unitTypeId, profileId)).map(Right.apply).recoverWith {
      case e: Exception => Future.successful(Left(s"Failed to create unit $unitId: ${e.getLocalizedMessage}"))
    }

  def list(implicit ec: ExecutionContext): Future[Either[String, Seq[AcsUnit]]] =
    db.run(
        for {
          result <- getUnitQuery.result
        } yield result.map(mapToUnit)
      )
      .map(Right.apply)
      .recoverWith {
        case e: Exception => Future.successful(Left(s"Failed get units: ${e.getLocalizedMessage}"))
      }

  def getSecret(unitId: String)(implicit ec: ExecutionContext): Future[Option[String]] =
    db.run(for {
      result <- UnitParamDao
                 .join(UnitTypeParamDao)
                 .on(
                   (up, utp) =>
                     up.unitTypeParamId === utp.unitTypeParamId
                       && up.unitId === unitId
                       && utp.name === AcsParameter.secret
                 )
                 .map(_._1.value)
                 .result
    } yield if (result.isEmpty) None else result.head)

  private def getUnitQuery =
    UnitDao
      .join(ProfileDao)
      .on(_.profileId === _.profileId)
      .join(UnitTypeDao)
      .on(_._1.unitTypeId === _.unitTypeId)

  private def mapToUnitParam(t: (daos.Tables.UnitParamRow, daos.Tables.UnitTypeParamRow)) =
    AcsUnitParameter(
      unitId = t._1.unitId,
      unitTypeParamId = t._1.unitTypeParamId,
      unitTypeParamName = t._2.name,
      value = t._1.value
    )

  private def mapToUnit(t: ((daos.Tables.UnitRow, daos.Tables.ProfileRow), daos.Tables.UnitTypeRow)) =
    AcsUnit(
      t._1._1.unitId,
      AcsProfile(
        Some(t._1._2.profileId),
        t._1._2.profileName,
        AcsUnitType(
          Some(t._2.unitTypeId),
          t._2.unitTypeName,
          t._2.vendorName.orNull,
          t._2.description.orNull,
          AcsProtocol.unsafeFromString(t._2.protocol)
        )
      )
    )
}
