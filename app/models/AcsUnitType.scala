package models

case class AcsUnitType(
    unitTypeId: Option[Int],
    name: String,
    vendor: String,
    description: String,
    protocol: AcsProtocol,
    params: Seq[AcsUnitTypeParameter] = Seq.empty
)
