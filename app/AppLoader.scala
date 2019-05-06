import controllers._
import services.{ProfileService, UnitService, UnitTypeService}
import freeacs.dbi.DBIHolder
import freeacs.tr069.Properties
import freeacs.tr069.base.BaseCache
import com.typesafe.config.ConfigFactory
import play.api.ApplicationLoader.Context
import play.api.cache.ehcache.EhCacheComponents
import play.api.db.{DBComponents, HikariCPComponents}
import play.api.routing.Router
import play.api.routing.sird._
import play.api.{Application, ApplicationLoader, BuiltInComponentsFromContext}
import play.api.db.evolutions.EvolutionsComponents
import play.api.db.slick.{DbName, SlickComponents}
import slick.jdbc.JdbcProfile

class AppComponents(context: Context)
    extends BuiltInComponentsFromContext(context)
    with EhCacheComponents
    with AssetsComponents
    with DBComponents
    with EvolutionsComponents
    with HikariCPComponents
    with SlickComponents {

  applicationEvolutions

  override val httpFilters = Nil
  override val Action      = defaultActionBuilder

  lazy val dbConf = slickApi.dbConfig[JdbcProfile](DbName("default"))

  val config              = ConfigFactory.load()
  val properties          = new Properties(config)
  val dbiHolder           = new DBIHolder(config, dbApi.database("default"))
  val baseCache           = new BaseCache(defaultCacheApi.sync)
  val unitTypeService     = new UnitTypeService(dbConf)
  val profileService      = new ProfileService(dbConf)
  val unitDetailsService  = new UnitService(dbConf)
  val unitTypeController  = new UnitTypeController(controllerComponents, unitTypeService)
  val dashboardController = new DashboardController(controllerComponents, dbiHolder)
  val healthController    = new HealthController(controllerComponents, dbiHolder)
  val tr069Controller =
    new Tr069Controller(controllerComponents, properties, baseCache, config, dbiHolder, unitDetailsService)

  override val router: Router = Router.from {
    case GET(p"/")                  => dashboardController.index
    case GET(p"/ok")                => healthController.ok
    case GET(p"/unittype/create")   => unitTypeController.viewCreate
    case POST(p"/unittype/create")  => unitTypeController.postCreate
    case GET(p"/unittype/overview") => unitTypeController.overview
    case POST(p"/tr069")            => tr069Controller.provision
    case POST(p"/tr069/prov")       => tr069Controller.provision
    case GET(p"/assets/$file*")     => assets.versioned("/public", file)
  }
}

class AppLoader extends ApplicationLoader {
  def load(context: Context): Application =
    new AppComponents(context).application
}
