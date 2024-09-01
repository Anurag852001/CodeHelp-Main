package com.video.CodeHelp.Service.CachePopulationService.Handlers;

import com.github.benmanes.caffeine.cache.Cache;
import com.video.CodeHelp.Constants.DataConstants;
import com.video.CodeHelp.Dao.MainCodeDao;
import com.video.CodeHelp.Enums.ApplicationErrorEnums;
import com.video.CodeHelp.Enums.CacheTypeEnums;
import com.video.CodeHelp.Exception.CodeHelpException;
import com.video.CodeHelp.Service.CachePopulationService.ICachePopulationService;
import com.video.CodeHelp.Service.CachePopulationService.enums.CachePopulationTypes;
import com.video.CodeHelp.Service.CachePopulationService.pojo.MainCodeCachePopulationPojo;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

@Slf4j
public class MainCodeCachePopulationService extends ICachePopulationService {

  MainCodeDao mainCodeDao;
  private final Cache<String, Object> cache = CacheTypeEnums.TWO_HUNDERED_CACHE.getCache();

  @Inject
  public MainCodeCachePopulationService(MainCodeDao mainCodeDao) {
    this.mainCodeDao = mainCodeDao;
  }

  @Override
  public void populateCache(List<Long> qIds) {
    try {
      log.info("Populating main code cache with qIds : {}", qIds);
      List<MainCodeCachePopulationPojo> mainCodeCachePopulationPojos = mainCodeDao.getMainCodeCachePojoByQIds(qIds);
      if (CollectionUtils.isNotEmpty(mainCodeCachePopulationPojos)) {
        mainCodeCachePopulationPojos.parallelStream().forEach(
          mainCodeCachePopulationPojo -> {
            cache.put(getCacheKey(mainCodeCachePopulationPojo.getQId(), CachePopulationTypes.MAIN_CODE_CACHE, mainCodeCachePopulationPojo.getLineNumber()), mainCodeCachePopulationPojo);
          }
        );
      }
    } catch (Exception e) {
      log.error("Error while populating main code cache", e);
      throw new CodeHelpException(ApplicationErrorEnums.ERROR_IN_POPULATING_CACHE);
    }
  }


  public String getCacheKey(Long qId, CachePopulationTypes cachePopulationTypes,Long lineNumber) {
    if(qId!=null && cachePopulationTypes!=null && lineNumber!=null) {
      return String.join(DataConstants.UNDERSCORE, qId.toString(), cachePopulationTypes.name(), lineNumber.toString());
    } else{
      throw new CodeHelpException(ApplicationErrorEnums.ERROR_IN_POPULATING_CACHE);
    }
  }

}
