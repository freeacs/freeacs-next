package models

case class AcsUnit(unitId: String, profile: AcsProfile, params: Seq[AcsUnitParameter] = Seq.empty) {
  lazy val unitTypeParams: Seq[AcsUnitTypeParameter] = profile.unitType.params
  lazy val hasConnectionRequestUrl: Boolean =
    params.exists(
      p => p.unitTypeParamName.endsWith("ConnectionRequestURL") && p.value.exists(!_.isBlank)
    )
  lazy val hasConnectionRequestUsername: Boolean =
    params.exists(
      p => p.unitTypeParamName.endsWith("ConnectionRequestUsername") && p.value.exists(!_.isBlank)
    )
  lazy val hasConnectionRequestPassword: Boolean =
    params.exists(
      p => p.unitTypeParamName.endsWith("ConnectionRequestPassword") && p.value.exists(!_.isBlank)
    )
  lazy val canKick: Boolean =
    hasConnectionRequestUrl && hasConnectionRequestUsername && hasConnectionRequestPassword
}
