package services
import freeacs.dbi.{ProvisioningProtocol, Unittype}
import play.api.mvc.{AbstractController, ControllerComponents}
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class UnitTypeService(protected val dbConfig: DatabaseConfig[JdbcProfile], cc: ControllerComponents)(
    implicit ec: ExecutionContext
) extends AbstractController(cc) {

  import dbConfig.profile.api._
  import dbConfig._
  import daos.Tables._

  def create(unitType: Unittype): Future[_] = {
    for {
      numsInserted <- db.run(
                       UnitType += UnitTypeRow(
                         unitType.getId,
                         None,
                         unitType.getName,
                         Option(unitType.getVendor),
                         Option(unitType.getDescription),
                         unitType.getProtocol.name()
                       )
                     )
    } yield Future.successful(numsInserted)
  }

  def list: Future[Seq[Unittype]] = {
    for {
      unitTypeRows <- db.run(UnitType.result)
      unitTypes = unitTypeRows.map(row => {
        val unitType = new Unittype(
          row.unitTypeName,
          row.vendorName.orNull,
          row.description.orNull,
          ProvisioningProtocol.valueOf(row.protocol)
        )
        unitType.setId(row.unitTypeId)
        unitType
      })
    } yield unitTypes
  }

}
