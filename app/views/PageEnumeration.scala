package views
import controllers.routes.WebController.index
import play.api.mvc.Call

sealed abstract class PageEnumeration(val name: String, val url: Call, val fontAwesome: String)

case object Dashboard extends PageEnumeration("Dashboard", index(), "fa fa-dashboard")
case object UnitTypeOverview extends PageEnumeration("UnitType Overview", index(), "fa fa-desktop")
case object CreateUnitType extends PageEnumeration("Create UnitType", index(), "fa fa-desktop")
case object ProfileOverview extends PageEnumeration("Profile Overview", index(), "fa fa-desktop")
case object CreateProfile extends PageEnumeration("Create Profile", index(), "fa fa-desktop")
case object UnitSearch extends PageEnumeration("Unit Search", index(), "fa fa-desktop")
case object CreateUnit extends PageEnumeration ("Create Unit", index(), "fa fa-desktop")
case object GroupOverview extends PageEnumeration("Group Overview", index(), "fa fa-desktop")
case object CreateGroup extends PageEnumeration("Create Group", index(), "fa fa-desktop")
case object JobOverview extends PageEnumeration("Job Overview", index(), "fa fa-desktop")
case object CreateJob extends PageEnumeration("Create Job", index(), "fa fa-desktop")
