package com.video.CodeHelp.Service;

import com.video.CodeHelp.Dao.MainCodeVariablesDao;
import com.video.CodeHelp.Enums.CacheTypeEnums;
import com.video.CodeHelp.Enums.CompilerTypeEnums;
import com.video.CodeHelp.Pojo.MainCodeVariable;
import com.video.CodeHelp.utils.CachingUtils;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


@Slf4j
public class MainCodeVariableService {
  private final MainCodeVariablesDao mainCodeVariablesDao;
  private final CachingService cachingService;

  @Inject
  public MainCodeVariableService(MainCodeVariablesDao mainCodeVariablesDao, CachingService cachingService) {
    this.mainCodeVariablesDao = mainCodeVariablesDao;
    this.cachingService = cachingService;
  }

  public List<MainCodeVariable> getVariables(Long qid, CompilerTypeEnums language) {
    List<MainCodeVariable> mainCodeVariables = (List<MainCodeVariable>) cachingService.getFromCache(CachingUtils.getCacheKeyForMainCodeVariables(qid, language), CacheTypeEnums.TWO_HUNDERED_CACHE);
    if (mainCodeVariables == null) {
      mainCodeVariables = mainCodeVariablesDao.getMainCodeVariablesByLanguageAndQId(language, qid);
      cachingService.populateInCache(CachingUtils.getCacheKeyForMainCodeVariables(qid, language), mainCodeVariables, CacheTypeEnums.TWO_HUNDERED_CACHE);
    }
    return mainCodeVariables;
  }

  public void saveVariables(List<MainCodeVariable> variables) {
    mainCodeVariablesDao.saveMainCodeVariables(variables);
  }

  public void updateVariable(MainCodeVariable variables) {
    mainCodeVariablesDao.updateMainCodeVariables(variables);
  }

}
