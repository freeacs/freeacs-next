package tr069.base;

import common.util.Cache;
import common.util.CacheValue;
import play.cache.SyncCacheApi;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BaseCache {
  /** 2 minutes. */
  private static final int SESSIONDATA_CACHE_TIMEOUT = 3 * 60 * 1000;

  /** 10 minutes. */
  private static final int FIRMWAREIMAGE_CACHE_TIMEOUT = 10 * 60 * 1000;

  private static final String SESSION_KEY = "SESSION";

  private static final String FIRMWAREIMAGE_KEY = "FIRMWARE";

  @Inject
  SyncCacheApi cache;

  /**
   * Retrieves the current session data from the cache based on a key that identifies the client.
   *
   * @param unitKey Can be either session id or unit id
   * @return SessionDataI
   */
  public SessionDataI getSessionData(String unitKey) {
    String key = unitKey + SESSION_KEY;
    CacheValue cv = cache.get(key);
    if (cv != null) {
      return (SessionDataI) cv.getObject();
    } else {
      throw new BaseCacheException(key);
    }
  }

  /**
   * Puts the given session data into the cache with a key that identifies the client.
   *
   * @param unitKey Can be either session id or unit id
   * @param sessionData The session data to be stored in cache
   */
  public void putSessionData(String unitKey, SessionDataI sessionData) {
    if (sessionData != null) {
      String key = unitKey + SESSION_KEY;
      CacheValue cv = new CacheValue(sessionData, Cache.SESSION, SESSIONDATA_CACHE_TIMEOUT);
      cv.setCleanupNotifier(new SessionDataCacheCleanup(unitKey, sessionData));
      cache.set(key, cv);
    }
  }

  public void removeSessionData(String unitKey) {
    String key = unitKey + SESSION_KEY;
    cache.remove(key);
  }

  public dbi.File getFirmware(String firmwareName, String unittypeName) {
    String key = firmwareName + unittypeName + FIRMWAREIMAGE_KEY;
    CacheValue cv = cache.get(key);
    if (cv != null) {
      return (dbi.File) cv.getObject();
    } else {
      return null;
    }
  }

  public void putFirmware(String firmwareName, String unittypeName, dbi.File firmware) {
    String key = firmwareName + unittypeName + FIRMWAREIMAGE_KEY;
    if (firmware != null) {
      CacheValue cv = new CacheValue(firmware, Cache.ABSOLUTE, FIRMWAREIMAGE_CACHE_TIMEOUT);
      cache.set(key, cv);
    }
  }
}
