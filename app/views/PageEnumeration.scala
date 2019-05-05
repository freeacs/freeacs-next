package views
import play.api.mvc.Call

sealed abstract class PageEnumeration(val name: String, val url: Call, val fontAwesome: String)

case object Dashboard extends PageEnumeration("Dashboard", controllers.routes.DashboardController.index(), "fa fa-dashboard")
case object UnitTypeOverview extends PageEnumeration("Unit Type Overview", controllers.routes.UnitTypeController.overview(), "fa fa-desktop")
case object CreateUnitType extends PageEnumeration("Create Unit Type", controllers.routes.UnitTypeController.viewCreate(), "fa fa-desktop")
case object ProfileOverview extends PageEnumeration("Profile Overview", controllers.routes.DashboardController.index(), "fa fa-desktop")
case object CreateProfile extends PageEnumeration("Create Profile", controllers.routes.DashboardController.index(), "fa fa-desktop")
case object UnitSearch extends PageEnumeration("Unit Search", controllers.routes.DashboardController.index(), "fa fa-desktop")
case object CreateUnit extends PageEnumeration ("Create Unit", controllers.routes.DashboardController.index(), "fa fa-desktop")
case object GroupOverview extends PageEnumeration("Group Overview", controllers.routes.DashboardController.index(), "fa fa-desktop")
case object CreateGroup extends PageEnumeration("Create Group", controllers.routes.DashboardController.index(), "fa fa-desktop")
case object JobOverview extends PageEnumeration("Job Overview", controllers.routes.DashboardController.index(), "fa fa-desktop")
case object CreateJob extends PageEnumeration("Create Job", controllers.routes.DashboardController.index(), "fa fa-desktop")
