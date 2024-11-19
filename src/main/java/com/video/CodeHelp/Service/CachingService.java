package com.video.CodeHelp.Service;

import com.github.benmanes.caffeine.cache.Cache;
import com.video.CodeHelp.Caffine.CaffineCacheFactory;
import com.video.CodeHelp.Enums.CacheTypeEnums;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class CachingService {

  Cache<String, Object> oneDayCommonCacheCache = CacheTypeEnums.ONE_DAY_COMMON_CACHE.getCache();
  Cache<String, Object> twoHunderedDayCache = CacheTypeEnums.TWO_HUNDERED_CACHE.getCache();

  public Object getFromCache(String key, CacheTypeEnums cacheType) {
    switch (cacheType) {
      case ONE_DAY_COMMON_CACHE -> {
        return oneDayCommonCacheCache.getIfPresent(key);
      }
      case TWO_HUNDERED_CACHE -> {
        return twoHunderedDayCache.getIfPresent(key);
      }
      default -> {
        log.info("Unknown cache type");
      }
    }
    return null;
  }

  public boolean populateInCache(String key, Object value, CacheTypeEnums cacheType) {
    if(StringUtils.isEmpty(key) || value == null){
      log.error("Key or value is null. Unable to populate cache.");
      return false;
    }
    try {
      switch (cacheType) {
        case ONE_DAY_COMMON_CACHE -> {
          CaffineCacheFactory.saveInCache(key, value, oneDayCommonCacheCache);
        }
        case TWO_HUNDERED_CACHE -> {
          CaffineCacheFactory.saveInCache(key, value,twoHunderedDayCache );
        }
        default -> {
          log.info("Cache type not supported");
          return false;
        }
      }
      log.info("Saved data to cache for key:{},value:{}",key,value);
      return true;
    } catch (Exception e) {
      log.error("Exception occurred while saving data in cache");
      return false;
    }
  }
}
