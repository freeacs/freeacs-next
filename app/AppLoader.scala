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

  override val httpFilters     = Nil
  override val Action          = defaultActionBuilder
  lazy val config              = ConfigFactory.load()
  lazy val dbConf              = slickApi.dbConfig[JdbcProfile](DbName("default"))
  lazy val cc                  = controllerComponents
  lazy val baseCache           = new BaseCache(defaultCacheApi.sync)
  lazy val unitTypeService     = new UnitTypeService(dbConf)
  lazy val profileService      = new ProfileService(dbConf)
  lazy val unitService         = new UnitService(dbConf)
  lazy val unitController      = new UnitController(cc, unitService, profileService, unitTypeService)
  lazy val unitTypeController  = new UnitTypeController(cc, unitTypeService)
  lazy val profileController   = new ProfileController(cc, profileService, unitTypeService)
  lazy val dashboardController = new DashboardController(cc, unitService)
  lazy val healthController    = new HealthController(cc)
  lazy val properties          = new Properties(config)
  lazy val dbiHolder           = new DBIHolder(config, dbApi.database("default"))
  lazy val tr069Controller     = new Tr069Controller(cc, properties, baseCache, config, dbiHolder, unitService)

  override val router: Router = Router.from {
    case GET(p"/")                  => dashboardController.index
    case GET(p"/ok")                => healthController.ok
    case GET(p"/unittype/create")   => unitTypeController.viewCreate
    case POST(p"/unittype/create")  => unitTypeController.postCreate
    case GET(p"/unittype/overview") => unitTypeController.overview
    case GET(p"/profile/create")    => profileController.viewCreate
    case POST(p"/profile/create")   => profileController.postCreate
    case GET(p"/profile/overview")  => profileController.overview
    case GET(p"/unit/create")       => unitController.viewCreate
    case POST(p"/unit/create")      => unitController.postCreate
    case GET(p"/unit/overview")     => unitController.overview
    case POST(p"/tr069")            => tr069Controller.provision
    case POST(p"/tr069/prov")       => tr069Controller.provision
    case GET(p"/assets/$file*")     => assets.versioned("/public", file)
  }
}

class AppLoader extends ApplicationLoader {
  def load(context: Context): Application =
    new AppComponents(context).application
}
