package com.video.CodeHelp.utils;

import com.video.CodeHelp.Constants.DataConstants;
import com.video.CodeHelp.Enums.ApplicationErrorEnums;
import com.video.CodeHelp.Enums.CacheTypeEnums;
import com.video.CodeHelp.Enums.CompilerTypeEnums;
import com.video.CodeHelp.Exception.CodeHelpException;
import com.video.CodeHelp.Pojo.SyncInCacheRequest;
import com.video.CodeHelp.Service.CachePopulationService.enums.CachePopulationTypes;
import org.apache.commons.lang3.StringUtils;

public class CachingUtils {

  public static String getCacheKeyForQuestionBody(Long qNo){
    StringBuilder sb = new StringBuilder();
    sb.append("QUESTION_HEADERS_").append(qNo);
    return sb.toString();
  }
  public static String getCacheKeyForQuestionExamples(Long qNo){
    StringBuilder sb = new StringBuilder();
    sb.append("QUESTION_EXAMPLES_").append(qNo);
    return sb.toString();
  }

  public static String getCacheKeyForQuestionConstraints(Long qNo){
    StringBuilder sb = new StringBuilder();
    sb.append("QUESTION_CONSTRAINTS_").append(qNo);
    return sb.toString();
  }

  public static String getCacheKeyForMainWrapperCode(Long qId, CachePopulationTypes cachePopulationTypes, Long lineNumber, CompilerTypeEnums compilerType) {
    if(qId!=null && cachePopulationTypes!=null && lineNumber!=null) {
      return String.join(DataConstants.UNDERSCORE, qId.toString(), cachePopulationTypes.name(), lineNumber.toString(),compilerType.name());
    } else{
      throw new CodeHelpException(ApplicationErrorEnums.ERROR_IN_POPULATING_CACHE);
    }
  }

  public static String getCacheKeyForDefaultWrapperCode(Long id, CompilerTypeEnums compilerType){
    return StringUtils.join(DataConstants.UNDERSCORE,id,CachePopulationTypes.DEFAULT_CODE_CACHE,compilerType.name());
  }

  public static SyncInCacheRequest getTwoHundredCacheSyncRequest(String cacheKey,Object cacheValue){
    return SyncInCacheRequest.builder().cacheKey(cacheKey).value(cacheValue).cacheTypeEnums(CacheTypeEnums.TWO_HUNDERED_CACHE).build();
  }
}
