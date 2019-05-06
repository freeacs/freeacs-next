package services
import freeacs.dbi.{ProvisioningProtocol, Unittype}
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class UnitTypeService(dbConfig: DatabaseConfig[JdbcProfile])(implicit ec: ExecutionContext) {

  import daos.Tables.{UnitType => UnitTypeDao, UnitTypeRow}
  import dbConfig._
  import dbConfig.profile.api._

  def create(unitType: Unittype): Future[Int] = {
    db.run(
      UnitTypeDao += UnitTypeRow(
        unitType.getId,
        None,
        unitType.getName,
        Option(unitType.getVendor),
        Option(unitType.getDescription),
        unitType.getProtocol.name()
      )
    )
  }

  def list: Future[Seq[Unittype]] = {
    db.run(for {
      unitTypeRows <- UnitTypeDao.result
      unitTypes = unitTypeRows.map(
        row =>
          new Unittype(
            row.unitTypeId,
            row.unitTypeName,
            row.vendorName.orNull,
            row.description.orNull,
            ProvisioningProtocol.valueOf(row.protocol)
        )
      )
    } yield unitTypes)
  }

}
