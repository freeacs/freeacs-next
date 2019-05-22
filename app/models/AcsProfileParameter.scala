package models

case class AcsProfileParameter(
    profileId: Int,
    unitTypeParamId: Int,
    unitTypeParamName: String,
    value: Option[String]
)
