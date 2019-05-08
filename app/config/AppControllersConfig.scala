package config
import controllers._
import freeacs.dbi.DBIHolder
import play.api.BuiltInComponentsFromContext

trait AppControllersConfig {
  this: AppServicesConfig
    with BuiltInComponentsFromContext
    with AppConfig
    with AppCacheConfig
    with AppDatabaseConfig =>

  lazy val redirectController =
    new RedirectController(controllerComponents)

  lazy val unitController =
    new UnitController(controllerComponents, unitService, profileService, unitTypeService)

  lazy val tr069Controller =
    new Tr069Controller(controllerComponents, unitService, profileService, unitTypeService, cache)

  lazy val unitTypeController =
    new UnitTypeController(controllerComponents, unitTypeService)

  lazy val profileController =
    new ProfileController(controllerComponents, profileService, unitTypeService)

  lazy val dashboardController =
    new DashboardController(controllerComponents, unitService)

  lazy val healthController =
    new HealthController(controllerComponents)

  lazy val oldTr069Ctrl =
    new OldTr069Controller(
      controllerComponents,
      properties,
      baseCache,
      config,
      new DBIHolder(config, playDb),
      unitService
    )
}
