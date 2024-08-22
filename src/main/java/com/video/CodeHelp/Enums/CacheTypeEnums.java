package com.video.CodeHelp.Enums;

import com.github.benmanes.caffeine.cache.Cache;
import com.video.CodeHelp.Caffine.CaffineCacheFactory;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CacheTypeEnums {

  ONE_DAY_COMMON_CACHE(CacheTTLS.ONE_DAY_CACHE);


  CacheTypeEnums(CacheTTLS cacheTTLS) {
    this.cache = CaffineCacheFactory.getCacheInstance(cacheTTLS);
  }

  public Cache<String, Object> getCache() {
    return cache;
  }
  Cache<String,Object> cache;
}
