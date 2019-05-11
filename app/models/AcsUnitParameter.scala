package models

case class AcsUnitParameter(
    unitId: String,
    unitTypeParamId: Int,
    unitTypeParamName: String,
    value: Option[String]
)
