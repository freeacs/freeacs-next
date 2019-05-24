package views

sealed abstract class Page(
    val name: String,
    val url: String,
    val link: Boolean = true
)

case object Dashboard        extends Page("Dashboard", "/")
case object UnitTypeOverview extends Page("Unit Type Overview", "/unittype/overview")
case object UnitTypeDetails  extends Page("Unit Type Details", "/unittype/details", false)
case object CreateUnitType   extends Page("Create Unit Type", "/unittype/create")
case object ProfileOverview  extends Page("Profile Overview", "/profile/overview")
case object ProfileDetails   extends Page("Profile Details", "/profile/details", false)
case object CreateProfile    extends Page("Create Profile", "/profile/create")
case object UnitOverview     extends Page("Unit Search", "/unit/overview")
case object UnitDetails      extends Page("Unit Details", "/unit/details", false)
case object UpdateUnitParam  extends Page("Update Unit Param", "/unit/details/updateparam", false)
case object UnitKick         extends Page("Kick Unit", "/unit/kick", false)
case object CreateUnit       extends Page("Create Unit", "/unit/create")
case object AddUnitParam     extends Page("Add Unit Param", "/unit/details/addparam", false)
case object GroupOverview    extends Page("Group Overview", "/group/overview")
case object CreateGroup      extends Page("Create Group", "/group/create")
case object JobOverview      extends Page("Job Overview", "/job/overview")
case object CreateJob        extends Page("Create Job", "/job/create")
