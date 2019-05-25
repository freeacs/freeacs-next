package models

case class AcsProfile(
    id: Int = -1,
    name: String,
    unitType: AcsUnitType,
    params: Seq[AcsProfileParameter] = Seq.empty
)
