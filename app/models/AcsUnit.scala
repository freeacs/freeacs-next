package models

case class AcsUnit(unitId: String, profile: AcsProfile, params: Seq[AcsUnitParameter] = Seq.empty)
