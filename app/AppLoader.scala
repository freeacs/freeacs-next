import config._
import controllers._
import play.api.ApplicationLoader.Context
import play.api.routing.Router
import play.api.routing.sird._
import play.api.{Application, ApplicationLoader, BuiltInComponentsFromContext, LoggerConfigurator}
import views.Dashboard

class AppComponents(context: Context)
    extends BuiltInComponentsFromContext(context)
    with AssetsComponents
    with AppCacheConfig
    with AppConfig
    with AppDatabaseConfig
    with AppServicesConfig
    with AppControllersConfig {

  override val httpFilters = Nil

  override val router: Router = Router.from {
    case GET(p"/")                               => dashboardController.index
    case GET(p"/ok")                             => healthController.ok
    case GET(p"/unittype/create")                => unitTypeController.viewCreate
    case POST(p"/unittype/create")               => unitTypeController.postCreate
    case GET(p"/unittype/overview")              => unitTypeController.overview
    case GET(p"/unittype/details/$unitTypeName") => unitTypeController.viewUnitType(unitTypeName)
    case GET(p"/profile/create")                 => profileController.viewCreate
    case POST(p"/profile/create")                => profileController.postCreate
    case GET(p"/profile/overview")               => profileController.overview
    case GET(p"/profile/details/$profileId")     => profileController.viewProfile(profileId.toInt)
    case GET(p"/unit/create")                    => unitController.viewCreate
    case POST(p"/unit/create")                   => unitController.postCreate
    case GET(p"/unit/overview")                  => unitController.overview
    case GET(p"/unit/details/$unitId")           => unitController.viewUnit(unitId)
    case POST(p"/tr069")                         => tr069Controller.provision
    case POST(p"/tr069/prov")                    => tr069Controller.provision
    case GET(p"/assets/$file*")                  => assets.versioned("/public", file)
    case GET(p"/$path<.*>")                      => redirectController.index(path, Dashboard)
  }
}

class AppLoader extends ApplicationLoader {
  def load(context: Context): Application = {
    LoggerConfigurator(context.environment.classLoader).foreach {
      _.configure(context.environment, context.initialConfiguration, Map.empty)
    }
    new AppComponents(context).application
  }
}
