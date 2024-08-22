package com.video.CodeHelp.Caffine;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.video.CodeHelp.Enums.CacheTTLS;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.concurrent.TimeUnit;

@Slf4j
@Singleton
public class CaffineCacheFactory {
  public static Cache<String, Object> getCacheInstance(CacheTTLS cacheTTLS) {
    return Caffeine.newBuilder().expireAfterWrite(cacheTTLS.getTime(), TimeUnit.SECONDS).maximumSize(1000000).build();
  }

  public static boolean saveInCache(String key, Object value, Cache<Object, Object> cache) {
    try {
      cache.put(key, value);
      return true;
    } catch (Exception e) {
      log.error("Exception occurred while saving data in cache");
      return false;
    }
  }

  public static JsonObject getAllDataInCache(Cache<String, Object> cache) {
    JsonObject data = new JsonObject();
    cache.asMap().entrySet().stream().forEach(entry ->{
      data.put(entry.getKey().toString(), Json.encode(entry.getValue()));
    });
    return data;
  }
}
