package com.video.CodeHelp.Service.CachePopulationService.WrapperCodeService.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.video.CodeHelp.Dao.MainCodeDao;
import com.video.CodeHelp.Enums.CacheTypeEnums;
import com.video.CodeHelp.Enums.CompilerTypeEnums;
import com.video.CodeHelp.Pojo.GetWrapperCodeRequest;
import com.video.CodeHelp.Service.CachingService;
import com.video.CodeHelp.Service.CachePopulationService.WrapperCodeService.ICodeWrapperService;
import com.video.CodeHelp.Service.CachePopulationService.WrapperCodeService.pojos.IWrapperCodeResponse;
import com.video.CodeHelp.Service.CachePopulationService.WrapperCodeService.pojos.MainWrapperCode;
import com.video.CodeHelp.utils.CachingUtils;
import com.video.CodeHelp.utils.CommonUtils;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class MainWrapperCodeService implements ICodeWrapperService {

  EventBus eventBus;
  MainCodeDao mainCodeDao;
  CachingService cachingService;

  @Inject
  public MainWrapperCodeService(EventBus eventBus,MainCodeDao mainCodeDao,CachingService cachingService) {
    this.eventBus = eventBus;
    this.mainCodeDao = mainCodeDao;
    this.cachingService = cachingService;

  }

  @Override
  public String wrapCode(String code, Long qid, CompilerTypeEnums compilerType, List<String> inputs) {
    MainWrapperCode wrapperCode =  (MainWrapperCode) getWrapperCode(GetWrapperCodeRequest.builder().qid(qid).compilerType(compilerType).build());
    return new StringBuilder().append(code).append(System.lineSeparator()).append(wrapperCode.getMainCode()).append(System.lineSeparator()).toString();
  }

  @Override
  public IWrapperCodeResponse getWrapperCode(GetWrapperCodeRequest request) {
    MainWrapperCode result = new ObjectMapper().convertValue(cachingService.getFromCache(CachingUtils.getCacheKeyForMainWrapperCode(request.getQid(),request.getCompilerType()), CacheTypeEnums.TWO_HUNDERED_CACHE), MainWrapperCode.class);
    if (result == null) {
      result = mainCodeDao.getMainCodeByQId(request.getQid(), request.getCompilerType());
      if(result == null) {
        log.info("No default code found for request:{}",request);
        return null;
      }
      String cacheKey= CachingUtils.getCacheKeyForMainWrapperCode(request.getQid(), request.getCompilerType());
      cachingService.populateInCache(cacheKey, result, CacheTypeEnums.TWO_HUNDERED_CACHE);
      log.info("fetched default wrapper code from db and cached with key:{}", cacheKey);
    }

    String defaultCode =  CommonUtils.splitAndSeparateByLine(result.getMainCode());
    result.setMainCode(defaultCode);
    return result;
  }

  @Override
  public Long saveWrapperCode(JsonObject request) {
    MainWrapperCode request1 = request.mapTo(MainWrapperCode.class);
    Long id = mainCodeDao.saveMainCode(request1);
    String cacheKey = CachingUtils.getCacheKeyForMainWrapperCode(request1.getQid(),request1.getCompilerType());
    cachingService.populateInCache(cacheKey,request1, CacheTypeEnums.TWO_HUNDERED_CACHE);
    return id;
  }
}
