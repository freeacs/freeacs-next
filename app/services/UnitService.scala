package services

import com.google.inject.{Inject, Singleton}
import models._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UnitService @Inject()(
    unitTypeService: UnitTypeService,
    profileService: ProfileService,
    protected val dbConfigProvider: DatabaseConfigProvider
) extends HasDatabaseConfigProvider[JdbcProfile] {

  import daos.Tables._

  import profile.api._

  def find(unitId: String)(implicit ec: ExecutionContext): Future[Option[AcsUnit]] =
    db.run(findUnitQuery(unitId)).flatMap {
      case Some(unit) => db.run(getUnitTypeParamsQuery(unit)).map(Some.apply)
      case _          => Future.successful(None)
    }

  def count: Future[Int] =
    db.run(Unit.length.result)

  def creatOrFail(unitId: String, unitTypeId: Int, profileId: Int)(
      implicit ec: ExecutionContext
  ): Future[Either[String, AcsUnit]] =
    db.run(createUnitQuery(unitId, unitTypeId, profileId)).map(Right.apply).recoverWith {
      case e => Future.successful(Left("Failed to create unit: " + e.getLocalizedMessage))
    }

  def createAndReturnUnit(unitId: String, profileName: String, unitTypeName: String)(
      implicit ec: ExecutionContext
  ): Future[AcsUnit] =
    db.run((for {
      unitTypeId <- unitTypeService.createUnitTypeQuery(unitTypeName, AcsProtocol.TR069)
      profileId  <- profileService.createProfileQuery(profileName, unitTypeId)
      acsUnit    <- createUnitQuery(unitId, profileId, unitTypeId)
      withParams <- getUnitTypeParamsQuery(acsUnit)
    } yield withParams).transactionally)

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
      result <- UnitParam
                 .join(UnitTypeParam)
                 .on(
                   (up, utp) =>
                     up.unitTypeParamId === utp.unitTypeParamId
                       && up.unitId === unitId
                       && utp.name === SystemParameters.SECRET.name
                 )
                 .map(_._1.value)
                 .result
    } yield if (result.isEmpty) None else result.head)

  def upsertParameters(params: Seq[AcsUnitParameter]): Future[Seq[Int]] =
    db.run(
      DBIO
        .sequence(
          params.map(p => UnitParam.insertOrUpdate(UnitParamRow(p.unitId, p.unitTypeParamId, p.value)))
        )
        .transactionally
    )

  private def getUnitQuery =
    Unit.join(Profile).on(_.profileId === _.profileId).join(UnitType).on(_._1.unitTypeId === _.unitTypeId)

  private def getUnitTypeParamsQuery(unit: AcsUnit)(implicit ec: ExecutionContext): DBIO[AcsUnit] =
    unitTypeService
      .getParamsQuery(unit.profile.unitType.unitTypeId.get)
      .map(
        params =>
          unit.copy(profile = unit.profile.copy(unitType = unit.profile.unitType.copy(params = params)))
      )

  private def findUnitQuery(unitId: String)(implicit ec: ExecutionContext): DBIO[Option[AcsUnit]] =
    for {
      result <- getUnitQuery.filter(_._1._1.unitId === unitId).result.headOption
      params <- UnitParam
                 .join(UnitTypeParam)
                 .on(_.unitTypeParamId === _.unitTypeParamId)
                 .filter(_._1.unitId === unitId)
                 .result
    } yield {
      result.map(mapToUnit).map { unit =>
        unit.copy(params = params.map(mapToUnitParam))
      }
    }

  private def createUnitQuery(unitId: String, unitTypeId: Int, profileId: Int)(
      implicit ec: ExecutionContext
  ): DBIO[AcsUnit] =
    for {
      _ <- Unit += UnitRow(
            unitId,
            unitTypeId,
            profileId
          )
      unitRow <- getUnitQuery.filter(_._1._1.unitId === unitId).result.head
    } yield mapToUnit(unitRow)

  private def mapToUnitParam(t: (daos.Tables.UnitParamRow, daos.Tables.UnitTypeParamRow)) =
    AcsUnitParameter(
      unitId = t._1.unitId,
      unitTypeParamId = t._1.unitTypeParamId,
      unitTypeParamName = t._2.name,
      value = t._1.value
    )

  private def mapToUnit(t: ((UnitRow, ProfileRow), UnitTypeRow)) =
    AcsUnit(
      t._1._1.unitId,
      AcsProfile(
        t._1._2.profileId,
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
