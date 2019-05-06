package services

import freeacs.dbi.util.SystemParameters
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class UnitService(dbConfig: DatabaseConfig[JdbcProfile])(implicit ec: ExecutionContext) {

  import daos.Tables.{UnitParam => UnitParamDao, UnitTypeParam => UnitTypeParamDao}
  import dbConfig._
  import dbConfig.profile.api._

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
