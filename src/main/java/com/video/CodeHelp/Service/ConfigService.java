package com.video.CodeHelp.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.video.CodeHelp.Caffine.CaffineCacheFactory;
import com.video.CodeHelp.Dao.ConfigDao;
import com.video.CodeHelp.Enums.CacheTTLS;
import com.video.CodeHelp.Pojo.Config;
import com.video.CodeHelp.Pojo.SaveOrUpdateConfigRequest;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Singleton
@Slf4j
public class ConfigService {

  private final ConfigDao configDao;
  @Inject
  public ConfigService(ConfigDao configDao){
    this.configDao = configDao;
  }


  public Long saveCodeHelpConfig(SaveOrUpdateConfigRequest request){
    Long id = configDao.saveConfig(request);
    log.info("saved code help runtime modifiable config with id:{}" ,id);
    return id;
  }

  public Long updateCodeHelpConfig(SaveOrUpdateConfigRequest request){
    Long id = configDao.updateConfig(request);
    log.info("update config successfully:{}" ,id);
    return request.getId();
  }

  public Config getCodeHelpConfig(String configKey,String configType){
    Cache<Object,Object> cache =  CaffineCacheFactory.getCacheInstance(CacheTTLS.ONE_DAY_CACHE);
    Object config = cache.getIfPresent(configKey);
    if(config == null){
      config = configDao.getConfig(configKey, configType);
      cache.put(configKey,config);
      log.info("fetched config from db and cached with key:{}" ,configKey);
    } else {
      log.info("fetched config from cache with key:{}" ,configKey);
    }
    return config == null ? null : new ObjectMapper().convertValue(config, Config.class);
  }

}
