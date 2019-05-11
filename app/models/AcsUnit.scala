package models

case class AcsUnit(unitId: String, profile: AcsProfile, params: Seq[AcsUnitParameter] = Seq.empty) {
  lazy val unitTypeParams: Seq[AcsUnitTypeParameter] = profile.unitType.params
}
