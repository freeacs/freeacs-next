package config
import controllers._
import play.api.BuiltInComponentsFromContext
import play.api.libs.ws.ahc.AhcWSComponents

trait AppControllersConfig {
  this: AppServicesConfig
    with BuiltInComponentsFromContext
    with AppConfig
    with AppCacheConfig
    with AppDatabaseConfig
    with AhcWSComponents =>

  lazy val redirectController =
    new RedirectController(controllerComponents)

  lazy val unitController =
    new UnitController(controllerComponents, unitService, profileService, unitTypeService, wsClient)

  lazy val userAction =
    new SecureAction(unitService, config, environment, controllerComponents.parsers.defaultBodyParser)

  lazy val tr069Controller =
    new Tr069Controller(
      controllerComponents,
      unitService,
      unitTypeService,
      cache,
      userAction,
      settings
    )

  lazy val unitTypeController =
    new UnitTypeController(controllerComponents, unitTypeService)

  lazy val profileController =
    new ProfileController(controllerComponents, profileService, unitTypeService)

  lazy val dashboardController =
    new DashboardController(controllerComponents, unitService)

  lazy val healthController =
    new HealthController(controllerComponents)
}
