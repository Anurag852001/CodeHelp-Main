package com.video.CodeHelp.Service;

import com.video.CodeHelp.Dao.CodeHelpConfigDao;
import com.video.CodeHelp.Pojo.SaveCodeHelpConfigRequest;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Singleton
@Slf4j
public class CodeHelpConfigService {

  private final CodeHelpConfigDao codeHelpConfigDao;
  @Inject
  public CodeHelpConfigService(CodeHelpConfigDao codeHelpConfigDao){
    this.codeHelpConfigDao = codeHelpConfigDao;
  }


  public Integer saveCodeHelpConfig(SaveCodeHelpConfigRequest request){
    Long startTime = System.currentTimeMillis();
//    Integer id = codeHelpConfigDao.saveCodeHelpConfig(request);
    log.info("Time taken to save config to db:{} ms",System.currentTimeMillis()-startTime);
    return 1;
  }

}
