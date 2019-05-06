package views

sealed abstract class Page(
    val name: String,
    val fontAwesome: String,
    val url: String
)

case object Dashboard        extends Page("Dashboard", "fa fa-dashboard", "/")
case object UnitTypeOverview extends Page("Unit Type Overview", "fa fa-desktop", "/unittype/overview")
case object CreateUnitType   extends Page("Create Unit Type", "fa fa-desktop", "/unittype/create")
case object ProfileOverview  extends Page("Profile Overview", "fa fa-desktop", "/profile/overview")
case object CreateProfile    extends Page("Create Profile", "fa fa-desktop", "/profile/create")
case object UnitOverview     extends Page("Unit Search", "fa fa-desktop", "/unit/overview")
case object CreateUnit       extends Page("Create Unit", "fa fa-desktop", "/unit/create")
case object GroupOverview    extends Page("Group Overview", "fa fa-desktop", "/group/overview")
case object CreateGroup      extends Page("Create Group", "fa fa-desktop", "/group/create")
case object JobOverview      extends Page("Job Overview", "fa fa-desktop", "/job/overview")
case object CreateJob        extends Page("Create Job", "fa fa-desktop", "/job/create")
