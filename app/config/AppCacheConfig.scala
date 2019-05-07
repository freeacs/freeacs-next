package config
import freeacs.tr069.base.BaseCache
import play.api.cache.ehcache.EhCacheComponents

trait AppCacheConfig extends EhCacheComponents {
  lazy val cache     = defaultCacheApi
  lazy val syncCache = defaultCacheApi.sync
  lazy val baseCache = new BaseCache(syncCache)
}
