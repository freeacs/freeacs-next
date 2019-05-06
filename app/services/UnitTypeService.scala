package services
import freeacs.dbi.{ProvisioningProtocol, Unittype}
import play.api.mvc.{AbstractController, ControllerComponents}
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class UnitTypeService (protected val dbConfig: DatabaseConfig[JdbcProfile],
                       cc: ControllerComponents
                      )(implicit ec: ExecutionContext) extends AbstractController(cc)  {

  import dbConfig.profile.api._
  import dbConfig._
  import dao.Tables._

  def list: Future[Seq[Unittype]] = {
    for {
      unittypes <- db.run(UnitType.result).map(_.map(row => {
        val unitType = new Unittype(
          row.unitTypeName,
          row.vendorName.orNull,
          row.description.orNull,
          ProvisioningProtocol.valueOf(row.protocol)
        )
        unitType.setId(row.unitTypeId)
        unitType
      }))
    } yield unittypes
  }

}
