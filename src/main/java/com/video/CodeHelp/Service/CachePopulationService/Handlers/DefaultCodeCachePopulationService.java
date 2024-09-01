package com.video.CodeHelp.Service.CachePopulationService.Handlers;

import com.github.benmanes.caffeine.cache.Cache;
import com.video.CodeHelp.Constants.DataConstants;
import com.video.CodeHelp.Dao.DefaultCodeDao;
import com.video.CodeHelp.Enums.ApplicationErrorEnums;
import com.video.CodeHelp.Enums.CacheTypeEnums;
import com.video.CodeHelp.Exception.CodeHelpException;
import com.video.CodeHelp.Service.CachePopulationService.ICachePopulationService;
import com.video.CodeHelp.Service.CachePopulationService.enums.CachePopulationTypes;
import com.video.CodeHelp.Service.CachePopulationService.pojo.CachePopulationPojo;
import com.video.CodeHelp.Service.CachePopulationService.pojo.DefaultCodeCachePopulationPojo;
import io.vertx.core.eventbus.ReplyFailure;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Slf4j
public class DefaultCodeCachePopulationService extends ICachePopulationService {

  Cache<String,Object> twoHunderedDayCache = CacheTypeEnums.TWO_HUNDERED_CACHE.getCache();
  DefaultCodeDao defaultCodeDao;

  @Inject
  public DefaultCodeCachePopulationService(DefaultCodeDao defaultCodeDao) {
    this.defaultCodeDao = defaultCodeDao;
  }

  @Override
  public void populateCache(List<Long> qIds) {
    try {
      log.info("Populating Default Code Cache Started...");
      List<DefaultCodeCachePopulationPojo> defaultCodeCachePopulationPojos = defaultCodeDao.getDefaultCodeCachePojoByQIds(qIds);
      for (DefaultCodeCachePopulationPojo defaultCodeCachePopulationPojo : defaultCodeCachePopulationPojos) {
        String cacheKey = getCacheKey(defaultCodeCachePopulationPojo.getQId(), CachePopulationTypes.DEFAULT_CODE_CACHE);
        twoHunderedDayCache.put(cacheKey, defaultCodeCachePopulationPojo);
      }
      log.info("Populating Default Code Cache Completed...");
    } catch (Exception e) {
      log.error("Error while populating Default Code Cache", e);
      throw new CodeHelpException(ApplicationErrorEnums.ERROR_IN_POPULATING_CACHE);
    }
  }
}
