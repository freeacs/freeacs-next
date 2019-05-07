package config
import services.{ProfileService, UnitService, UnitTypeService}

trait AppServicesConfig {
  this: AppDatabaseConfig with AppCacheConfig =>

  lazy val unitTypeService = new UnitTypeService(slickDbConf)
  lazy val profileService  = new ProfileService(slickDbConf)
  lazy val unitService     = new UnitService(slickDbConf)
}
