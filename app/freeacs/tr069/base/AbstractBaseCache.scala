package freeacs.tr069.base

import freeacs.common.util.CacheValue
import play.api.cache.SyncCacheApi

abstract class AbstractBaseCache(cache: SyncCacheApi) {

  def get(k: String)                = cache.get[CacheValue](k).orNull
  def set(k: String, v: CacheValue) = cache.set(k, v)
  def remove(k: String)             = cache.remove(k)

}
