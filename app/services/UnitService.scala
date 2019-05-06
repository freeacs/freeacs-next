package services

import freeacs.dbi.util.SystemParameters
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class UnitService(dbConfig: DatabaseConfig[JdbcProfile])(implicit ec: ExecutionContext) {

  import daos.Tables.{Unit => UnitDao, UnitRow, UnitParam => UnitParamDao, UnitTypeParam => UnitTypeParamDao}
  import dbConfig._
  import dbConfig.profile.api._

  def create(unitId: String, unitTypeId: Int, profileId: Int): Future[Int] =
    db.run(UnitDao += UnitRow(unitId, unitTypeId, profileId))

  def getSecret(unitId: String): Future[Option[String]] = {
    db.run(for {
      result <- UnitParamDao
                 .join(UnitTypeParamDao)
                 .on(
                   (up, utp) =>
                     up.unitTypeParamId === utp.unitTypeParamId
                       && up.unitId === unitId
                       && utp.name === SystemParameters.SECRET
                 )
                 .map(_._1.value)
                 .result
    } yield if (result.isEmpty) None else result.head)
  }
}
