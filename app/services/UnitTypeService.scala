package services
import freeacs.dbi.{ProvisioningProtocol, Unittype}
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class UnitTypeService(dbConfig: DatabaseConfig[JdbcProfile]) {

  import daos.Tables.{UnitType => UnitTypeDao, UnitTypeRow}
  import dbConfig._
  import dbConfig.profile.api._

  def create(name: String, vendor: String, desc: String, protocol: ProvisioningProtocol)(
      implicit ec: ExecutionContext
  ): Future[Int] =
    db.run(UnitTypeDao += UnitTypeRow(-1, None, name, Option(vendor), Option(desc), protocol.name()))

  def list(implicit ec: ExecutionContext): Future[Seq[Unittype]] = {
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
