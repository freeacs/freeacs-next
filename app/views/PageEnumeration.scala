package views

sealed abstract class PageEnumeration(val name: String, val fontAwesome: String, val url: String)

case object Dashboard extends PageEnumeration("Dashboard", "fa fa-dashboard", "/")
case object UnitTypeOverview extends PageEnumeration("Unit Type Overview", "fa fa-desktop", "/unittype/overview")
case object CreateUnitType extends PageEnumeration("Create Unit Type", "fa fa-desktop", "/unittype/create")
case object ProfileOverview extends PageEnumeration("Profile Overview", "fa fa-desktop", "/")
case object CreateProfile extends PageEnumeration("Create Profile", "fa fa-desktop", "/")
case object UnitSearch extends PageEnumeration("Unit Search", "fa fa-desktop", "/")
case object CreateUnit extends PageEnumeration("Create Unit", "fa fa-desktop", "/")
case object GroupOverview extends PageEnumeration("Group Overview", "fa fa-desktop", "/")
case object CreateGroup extends PageEnumeration("Create Group", "fa fa-desktop", "/")
case object JobOverview extends PageEnumeration("Job Overview", "fa fa-desktop", "/")
case object CreateJob extends PageEnumeration("Create Job", "fa fa-desktop", "/")
