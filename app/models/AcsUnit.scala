package models

case class AcsUnit(unitId: String, profile: AcsProfile, params: Seq[AcsUnitParameter] = Seq.empty) {
  import SessionData._
  lazy val unitTypeParams: Seq[AcsUnitTypeParameter] = profile.unitType.params
  lazy val hasConnectionRequestUrl: Boolean =
    params.exists(
      p => p.unitTypeParamName.endsWith(connectionRequestUrlSuffix) && p.value.exists(!_.isEmpty)
    )
  lazy val hasConnectionRequestUsername: Boolean =
    params.exists(
      p => p.unitTypeParamName.endsWith(connectionRequestUsernameSuffix) && p.value.exists(!_.isEmpty)
    )
  lazy val hasConnectionRequestPassword: Boolean =
    params.exists(
      p => p.unitTypeParamName.endsWith(connectionRequestPasswordSuffix) && p.value.exists(!_.isEmpty)
    )
  lazy val canKick: Boolean =
    hasConnectionRequestUrl && hasConnectionRequestUsername && hasConnectionRequestPassword
}
