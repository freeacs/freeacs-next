package services

import daos.Tables.UnitTypeParamRow
import models._
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class UnitTypeService(val dbConfig: DatabaseConfig[JdbcProfile]) {

  import daos.Tables.{UnitType => UnitTypeDao, UnitTypeRow, UnitTypeParam => UnitTypeParamDao}
  import dbConfig._
  import dbConfig.profile.api._

  def createUnitTypeParameter(unitTypeId: Int, name: String, flag: String)(implicit ec: ExecutionContext) =
    db.run(
      for {
        unitTypeParamId <- UnitTypeParamDao returning UnitTypeParamDao
                            .map(_.unitTypeParamId) += UnitTypeParamRow(-1, unitTypeId, name, flag)
        unitTypeParameter <- UnitTypeParamDao.filter(_.unitTypeParamId === unitTypeParamId).result.headOption
      } yield
        unitTypeParameter
          .map(utp => AcsUnitTypeParameter(utp.unitTypeParamId, utp.unitTypeId, utp.name, utp.flags))
          .get
    )

  def createOrFail(name: String, vendor: String, desc: String, protocol: AcsProtocol)(
      implicit ec: ExecutionContext
  ): Future[Either[String, Int]] =
    db.run(createUnitTypeQuery(name, vendor, desc, protocol)).map(Right.apply).recoverWith {
      case e => Future.successful(Left("Failed to create unit type " + e.getLocalizedMessage))
    }

  def list(implicit ec: ExecutionContext): Future[Either[String, Seq[AcsUnitType]]] =
    db.run(for {
        unitTypeRows <- UnitTypeDao.result
        unitTypes = unitTypeRows.map(
          row => mapToUnitType(row)
        )
      } yield unitTypes)
      .map(Right.apply)
      .recoverWith {
        case e: Exception => Future.successful(Left(s"Failed get unit types: ${e.getLocalizedMessage}"))
      }

  def getParamsQuery(unitTypeId: Int)(implicit ec: ExecutionContext): DBIO[Seq[AcsUnitTypeParameter]] =
    for {
      params <- UnitTypeParamDao.filter(_.unitTypeId === unitTypeId).result
    } yield params.map(mapToUnitTypeParameter)

  def createUnitTypeQuery(name: String, protocol: AcsProtocol)(
      implicit ec: ExecutionContext
  ): DBIO[Int] =
    for {
      unitType <- UnitTypeDao.filter(_.unitTypeName === name).result.headOption
      unitTypeId <- if (unitType.isEmpty)
                     createUnitTypeQuery(name, null, null, protocol)
                   else
                     DBIO.successful(unitType.map(_.unitTypeId).get)
    } yield unitTypeId

  def createUnitTypeQuery(name: String, vendor: String, desc: String, protocol: AcsProtocol)(
      implicit ec: ExecutionContext
  ): DBIO[Int] =
    (for {
      unitTypeId <- UnitTypeDao returning UnitTypeDao.map(_.unitTypeId) += UnitTypeRow(
                     -1,
                     None,
                     name,
                     Option(vendor),
                     Option(desc),
                     protocol.name
                   )
      _ <- UnitTypeParamDao ++= SystemParameters.values.map { cp =>
            UnitTypeParamRow(-1, unitTypeId, cp.name, cp.flag)
          }
    } yield unitTypeId).transactionally

  private def mapToUnitType(row: UnitTypeRow) =
    AcsUnitType(
      Some(row.unitTypeId),
      row.unitTypeName,
      row.vendorName.orNull,
      row.description.orNull,
      AcsProtocol.unsafeFromString(row.protocol)
    )

  private def mapToUnitTypeParameter(param: UnitTypeParamRow) =
    AcsUnitTypeParameter(param.unitTypeParamId, param.unitTypeId, param.name, param.flags)
}
