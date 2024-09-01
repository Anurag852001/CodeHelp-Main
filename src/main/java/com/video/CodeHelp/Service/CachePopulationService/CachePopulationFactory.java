package com.video.CodeHelp.Service.CachePopulationService;

import com.video.CodeHelp.Constants.DataConstants;
import com.video.CodeHelp.Dao.QuestionDao;
import com.video.CodeHelp.Enums.QuestionStatusEnums;
import com.video.CodeHelp.Service.CachePopulationService.enums.CachePopulationTypes;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jdk.jfr.Name;

import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CachePopulationFactory {
  QuestionDao questionDao;
  Map<CachePopulationTypes,ICachePopulationService> cachePopulationServiceMap = new HashMap<>();


  @Inject
  public CachePopulationFactory(@Named(DataConstants.MAIN_CODE_CACHE_POPULATION_SERVICE) ICachePopulationService mainCodeCachePopulationService,
                                @Named(DataConstants.DEFAULT_CODE_CACHE_POPULATION_SERVICE) ICachePopulationService defaultCachePopulationService,
                                QuestionDao questionDao) {
    cachePopulationServiceMap.put(CachePopulationTypes.MAIN_CODE_CACHE, mainCodeCachePopulationService);
    cachePopulationServiceMap.put(CachePopulationTypes.DEFAULT_CODE_CACHE, defaultCachePopulationService);
    this.questionDao = questionDao;
  }

 public void populateAllCaches(List<CachePopulationTypes> cachePopulationTypesList){
      List<Long> qIds = questionDao.getQuestionIdsByStatus(QuestionStatusEnums.ACTIVE);
      cachePopulationTypesList.parallelStream().forEach(type->cachePopulationServiceMap.get(type).populateCache(qIds));
  }

}
