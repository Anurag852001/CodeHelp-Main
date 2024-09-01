package com.video.CodeHelp.Service.CachePopulationService;

import com.video.CodeHelp.Dao.QuestionDao;
import com.video.CodeHelp.Enums.QuestionStatusEnums;
import com.video.CodeHelp.Service.CachePopulationService.enums.CachePopulationTypes;
import jakarta.inject.Inject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CachePopulationFactory {
  QuestionDao questionDao;
  Map<CachePopulationTypes,ICachePopulationService> cachePopulationServiceMap = new HashMap<>();


  @Inject
  public CachePopulationFactory(ICachePopulationService wrapperCachePopulationService,ICachePopulationService defaultCachePopulationService,QuestionDao questionDao,Map<CachePopulationTypes,ICachePopulation>) {
    cachePopulationServiceMap.put(CachePopulationTypes.WRAPPER_CODE_CACHE, wrapperCachePopulationService);
    cachePopulationServiceMap.put(CachePopulationTypes.DEFAULT_CODE_CACHE, defaultCachePopulationService);
    this.questionDao = questionDao;
  }

  void populateAllCaches(List<CachePopulationTypes> cachePopulationTypesList){
      List<Long> qIds = questionDao.getQuestionIdsByStatus(QuestionStatusEnums.ACTIVE);
      cachePopulationTypesList.parallelStream().forEach(type->cachePopulationServiceMap.get(type).populateCache(qIds));
  }

}
