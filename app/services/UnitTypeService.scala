package services

import models._
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class UnitTypeService(val dbConfig: DatabaseConfig[JdbcProfile]) {

  import daos.Tables.{UnitType => UnitTypeDao, UnitTypeRow, UnitTypeParam => UnitTypeParamDao}
  import dbConfig._
  import dbConfig.profile.api._

  def createOrFail(name: String, vendor: String, desc: String, protocol: AcsProtocol)(
      implicit ec: ExecutionContext
  ): Future[Either[String, Int]] =
    db.run(createUnitTypeQuery(name, vendor, desc, protocol)).map(Right.apply).recoverWith {
      case e => Future.successful(Left("Failed to create unit type " + e.getLocalizedMessage))
    }

  def list(implicit ec: ExecutionContext): Future[Either[String, Seq[AcsUnitType]]] = {
    db.run(for {
        unitTypeRows <- UnitTypeDao.result
        unitTypes = unitTypeRows.map(
          row =>
            AcsUnitType(
              Some(row.unitTypeId),
              row.unitTypeName,
              row.vendorName.orNull,
              row.description.orNull,
              AcsProtocol.unsafeFromString(row.protocol)
          )
        )
      } yield unitTypes)
      .map(Right.apply)
      .recoverWith {
        case e: Exception => Future.successful(Left(s"Failed get unit types: ${e.getLocalizedMessage}"))
      }
  }

  def getParamsQuery(unitTypeId: Int)(implicit ec: ExecutionContext): DBIO[Seq[AcsUnitTypeParameter]] =
    for {
      params <- UnitTypeParamDao.filter(_.unitTypeId === unitTypeId).result
    } yield
      params.map { param =>
        AcsUnitTypeParameter(param.unitTypeParamId, param.unitTypeId, param.name, param.flags)
      }

  def createUnitTypeQuery(name: String, protocol: AcsProtocol)(
      implicit ec: ExecutionContext
  ): DBIO[Int] =
    createUnitTypeQuery(name, null, null, protocol)

  def createUnitTypeQuery(name: String, vendor: String, desc: String, protocol: AcsProtocol)(
      implicit ec: ExecutionContext
  ): DBIO[Int] =
    UnitTypeDao returning UnitTypeDao.map(_.unitTypeId) += UnitTypeRow(
      -1,
      None,
      name,
      Option(vendor),
      Option(desc),
      protocol.name
    )
}
