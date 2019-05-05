import controllers._
import services.UnitDetailsService
import freeacs.dbi.DBIHolder
import freeacs.tr069.Properties
import freeacs.tr069.base.BaseCache
import com.typesafe.config.ConfigFactory
import play.api.ApplicationLoader.Context
import play.api.cache.ehcache.EhCacheComponents
import play.api.routing.Router
import play.api.routing.sird._
import play.api.{Application, ApplicationLoader, BuiltInComponentsFromContext}

class AppComponents(context: Context) extends BuiltInComponentsFromContext(context)
  with EhCacheComponents
  with AssetsComponents {

  override val httpFilters = Nil
  override val Action = defaultActionBuilder

  val config = ConfigFactory.load()
  val properties = new Properties(config)
  val dbiHolder = new DBIHolder(config)
  val baseCache = new BaseCache(defaultCacheApi.sync)
  val unitDetailsService = new UnitDetailsService(dbiHolder)
  val unitTypeController = new UnitTypeController(controllerComponents, dbiHolder)
  val dashboardController = new DashboardController(controllerComponents, dbiHolder)
  val healthController = new HealthController(controllerComponents, dbiHolder)
  val tr069Controller = new Tr069Controller(controllerComponents, properties, baseCache, config, dbiHolder, unitDetailsService)

  override val router: Router = Router.from {
    case GET(p"/")                      =>  dashboardController.index
    case GET(p"/ok")                    =>  healthController.ok
    case GET(p"/unittype/create")       =>  unitTypeController.viewCreate
    case POST(p"/unittype/create")      =>  unitTypeController.postCreate
    case GET(p"/unittype/overview")     =>  unitTypeController.overview
    case POST(p"/tr069")                =>  tr069Controller.provision
    case POST(p"/tr069/prov")           =>  tr069Controller.provision
    case GET(p"/assets/$file*")         =>  assets.versioned("/public", file)
  }
}

class AppLoader extends ApplicationLoader {
  def load(context: Context): Application = new AppComponents(context).application
}
