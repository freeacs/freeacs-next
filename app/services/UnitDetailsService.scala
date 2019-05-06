package services

import freeacs.dbi.DBIHolder
import freeacs.dbi.util.SystemParameters

import scala.util.Try

case class UnitDetails(user: String, pass: String)

class UnitDetailsService(val dbiHolder: DBIHolder) {

  // This method is used by legacy java code and cannot return Future.
  def loadByUsername(username: String): Try[UnitDetails] = {
    Try(dbiHolder.dbi.getACSUnit.getUnitById(username))
      .filter(_ != null)
      .map(_.getUnitParameters.get(SystemParameters.SECRET))
      .filter(_ != null)
      .map(param => UnitDetails(username, param.getValue))
  }
}
