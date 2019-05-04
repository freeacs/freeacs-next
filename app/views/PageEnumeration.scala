package views

import play.api.mvc.Call

sealed abstract class PageEnumeration {
  val name: String
  val url: Call
  val fontAwesome: String
}

case object Dashboard extends PageEnumeration {
  val name: String = "Dashboard"
  val url: Call = controllers.routes.WebController.index()
  val fontAwesome: String = "fa fa-dashboard"
}

case object UnitTypeOverview extends PageEnumeration {
  val name: String = "UnitType Overview"
  val url: Call = controllers.routes.WebController.index()
  val fontAwesome: String = "fa fa-desktop"
}

case object CreateUnitType extends PageEnumeration {
  val name: String = "Create UnitType"
  val url: Call = controllers.routes.WebController.index()
  val fontAwesome: String = "fa fa-desktop"
}

case object ProfileOverview extends PageEnumeration {
  val name: String = "Profile Overview"
  val url: Call = controllers.routes.WebController.index()
  val fontAwesome: String = "fa fa-desktop"
}

case object CreateProfile extends PageEnumeration {
  val name: String = "Create Profile"
  val url: Call = controllers.routes.WebController.index()
  val fontAwesome: String = "fa fa-desktop"
}

case object UnitSearch extends PageEnumeration {
  val name: String = "Unit Search"
  val url: Call = controllers.routes.WebController.index()
  val fontAwesome: String = "fa fa-desktop"
}

case object CreateUnit extends PageEnumeration {
  val name: String = "Create Unit"
  val url: Call = controllers.routes.WebController.index()
  val fontAwesome: String = "fa fa-desktop"
}

case object GroupOverview extends PageEnumeration {
  val name: String = "Group Overview"
  val url: Call = controllers.routes.WebController.index()
  val fontAwesome: String = "fa fa-desktop"
}

case object CreateGroup extends PageEnumeration {
  val name: String = "Create Group"
  val url: Call = controllers.routes.WebController.index()
  val fontAwesome: String = "fa fa-desktop"
}

case object JobOverview extends PageEnumeration {
  val name: String = "Job Overview"
  val url: Call = controllers.routes.WebController.index()
  val fontAwesome: String = "fa fa-desktop"
}

case object CreateJob extends PageEnumeration {
  val name: String = "Create Job"
  val url: Call = controllers.routes.WebController.index()
  val fontAwesome: String = "fa fa-desktop"
}
