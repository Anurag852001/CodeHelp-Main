package com.video.CodeHelp.Service;

import com.video.CodeHelp.Dao.ConfigDao;
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
    return configDao.getConfig(configKey, configType);
  }

}
