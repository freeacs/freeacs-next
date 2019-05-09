package config
import play.api.cache.ehcache.EhCacheComponents

trait AppCacheConfig extends EhCacheComponents {
  lazy val cache     = defaultCacheApi
  lazy val syncCache = defaultCacheApi.sync
}
