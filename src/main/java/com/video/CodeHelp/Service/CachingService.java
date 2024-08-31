package com.video.CodeHelp.Service;

import com.github.benmanes.caffeine.cache.Cache;
import com.video.CodeHelp.Caffine.CaffineCacheFactory;
import com.video.CodeHelp.Enums.CacheTypeEnums;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class CachingService {

  Cache<String, Object> oneDayCommonCacheCache = CacheTypeEnums.ONE_DAY_COMMON_CACHE.getCache();

  public Object getFromCache(String key, CacheTypeEnums cacheType) {
    switch (cacheType) {
      case ONE_DAY_COMMON_CACHE -> {
        return oneDayCommonCacheCache.getIfPresent(key);
      }
      default -> {
        log.info("Unknown cache type");
      }
    }
    return null;
  }

  public boolean populateInCache(String key, Object value, CacheTypeEnums cacheType) {
    if(StringUtils.isEmpty(key) || value == null){
      log.info("Key or value is null. Unable to populate cache.");
      return false;
    }
    try {
      switch (cacheType) {
        case ONE_DAY_COMMON_CACHE -> {
          CaffineCacheFactory.saveInCache(key, value, oneDayCommonCacheCache);
        }
        default -> {
          log.info("Cache type not supported");
          return false;
        }
      }
      return true;
    } catch (Exception e) {
      log.error("Exception occurred while saving data in cache");
      return false;
    }
  }
}
