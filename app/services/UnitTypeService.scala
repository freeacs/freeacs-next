package services

import models._
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class UnitTypeService(dbConfig: DatabaseConfig[JdbcProfile]) {

  import daos.Tables.{UnitType => UnitTypeDao, UnitTypeRow}
  import dbConfig._
  import dbConfig.profile.api._

  def create(name: String, vendor: String, desc: String, protocol: AcsProtocol)(
      implicit ec: ExecutionContext
  ): Future[Either[String, Int]] =
    db.run(UnitTypeDao += UnitTypeRow(-1, None, name, Option(vendor), Option(desc), protocol.name))
      .map(Right.apply)
      .recoverWith {
        case e: Exception =>
          Future.successful(Left(s"Failed to create unit type $name: ${e.getLocalizedMessage}"))
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

}
