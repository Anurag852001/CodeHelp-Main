package com.video.CodeHelp.Service.CachePopulationService.WrapperCodeService.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.video.CodeHelp.Dao.DefaultCodeDao;
import com.video.CodeHelp.Enums.CacheTypeEnums;
import com.video.CodeHelp.Enums.CompilerTypeEnums;
import com.video.CodeHelp.Pojo.GetWrapperCodeRequest;
import com.video.CodeHelp.Pojo.MainCodeVariable;
import com.video.CodeHelp.Pojo.TestCase;
import com.video.CodeHelp.Service.CachingService;
import com.video.CodeHelp.Service.CachePopulationService.WrapperCodeService.ICodeWrapperService;
import com.video.CodeHelp.Service.CachePopulationService.WrapperCodeService.pojos.DefaultWrapperCode;
import com.video.CodeHelp.Service.CachePopulationService.WrapperCodeService.pojos.IWrapperCodeResponse;
import com.video.CodeHelp.utils.CachingUtils;
import com.video.CodeHelp.utils.CommonUtils;
import io.vertx.core.json.JsonObject;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class DefaultWrapperCodeService implements ICodeWrapperService {


  private final DefaultCodeDao defaultCodeDao;
  private final CachingService cachingService;

  @Inject
  public DefaultWrapperCodeService( DefaultCodeDao defaultCodeDao, CachingService cachingService) {
    this.defaultCodeDao = defaultCodeDao;
    this.cachingService = cachingService;
  }

  @Override
  public String wrapCode(String code, Long qid, CompilerTypeEnums compilerType) {
      DefaultWrapperCode wrapperCode =  (DefaultWrapperCode) getWrapperCode(GetWrapperCodeRequest.builder().qid(qid).compilerType(compilerType).build());
      return wrapperCode.getDefaultCode()+System.lineSeparator()+code+System.lineSeparator();
  }

  @Override
  public IWrapperCodeResponse getWrapperCode(GetWrapperCodeRequest request) {
    DefaultWrapperCode result = new ObjectMapper().convertValue(cachingService.getFromCache(CachingUtils.getCacheKeyForDefaultWrapperCode(request.getQid(),request.getCompilerType()), CacheTypeEnums.TWO_HUNDERED_CACHE), DefaultWrapperCode.class);
    if (result == null) {
      result = defaultCodeDao.getDefaultCodeByQId(request.getQid(), request.getCompilerType());
     if(result == null) {
       log.info("No default code found for request:{}",request);
       return null;
     }
      String cacheKey= CachingUtils.getCacheKeyForDefaultWrapperCode(request.getQid(), request.getCompilerType());
      cachingService.populateInCache(cacheKey, result, CacheTypeEnums.TWO_HUNDERED_CACHE);
      log.info("fetched default wrapper code from db and cached with key:{}", cacheKey);
  }
    String defaultCode =  CommonUtils.splitAndSeparateByLine(result.getDefaultCode());
    result.setDefaultCode(defaultCode);
    return result;
  }

  @Override
  public Long saveWrapperCode(JsonObject request) {
    DefaultWrapperCode request1 =  request.mapTo(DefaultWrapperCode.class);
    log.info("Got request to save default wrapper code:{}",request1);
    // save code to database
    Long id = defaultCodeDao.saveDefaultCode(request1);
    String cacheKey = CachingUtils.getCacheKeyForDefaultWrapperCode(request1.getQid(),request1.getCompilerType());
    cachingService.populateInCache(cacheKey,request1, CacheTypeEnums.TWO_HUNDERED_CACHE);
    return id;
  }
  
}
