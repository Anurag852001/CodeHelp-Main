package com.video.CodeHelp.utils;

import com.video.CodeHelp.Constants.DataConstants;
import com.video.CodeHelp.Enums.ApplicationErrorEnums;
import com.video.CodeHelp.Enums.CacheTypeEnums;
import com.video.CodeHelp.Enums.CompilerTypeEnums;
import com.video.CodeHelp.Exception.CodeHelpException;
import com.video.CodeHelp.Pojo.SyncInCacheRequest;
import com.video.CodeHelp.Service.CachePopulationService.WrapperCodeService.enums.WrapperCodeEnums;

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

  public static String getCacheKeyForMainWrapperCode(Long qid, CompilerTypeEnums compilerType) {
    if(qid!=null && compilerType!=null ) {
      return new StringBuilder().append(qid).append(DataConstants.UNDERSCORE).append(compilerType).append(DataConstants.UNDERSCORE).append(WrapperCodeEnums.MAIN_CODE).toString();
    } else{
      throw new CodeHelpException(ApplicationErrorEnums.ERROR_IN_POPULATING_CACHE);
    }
  }

  public static String getCacheKeyForDefaultWrapperCode(Long qid, CompilerTypeEnums compilerType){
    StringBuilder stringBuilder = new StringBuilder();
    return stringBuilder.append(qid).append(DataConstants.UNDERSCORE).append(compilerType).append(DataConstants.UNDERSCORE).append(WrapperCodeEnums.DEFAULT_CODE).toString();
  }

  public static SyncInCacheRequest getTwoHundredCacheSyncRequest(String cacheKey,Object cacheValue){
    return SyncInCacheRequest.builder().cacheKey(cacheKey).value(cacheValue).cacheTypeEnums(CacheTypeEnums.TWO_HUNDERED_CACHE).build();
  }

  public static String getCacheKeyForMainCodeVariables(Long qid,CompilerTypeEnums compilerTypeEnums){
    return new StringBuilder().append(DataConstants.MAIN_CODE_VARIABLES).append(DataConstants.UNDERSCORE).append(qid).append(DataConstants.UNDERSCORE).append(compilerTypeEnums).toString();
  }

  public static String getCacheKeyForCorrectCode(Long qNo,CompilerTypeEnums compilerType){
    return new StringBuilder().append(DataConstants.CORRECT_CODE).append(DataConstants.UNDERSCORE).append(qNo).append(DataConstants.UNDERSCORE).append(compilerType).toString();
  }

  public static String getCacheKeyForTestCase(CompilerTypeEnums compilerType,Long qNo){
    return new StringBuilder().append(DataConstants.TEST_CASE).append(DataConstants.UNDERSCORE).append(qNo).toString();
  }
  public static String getCacheKeyForTestCaseResults(CompilerTypeEnums compilerType,Long qid){
    return new StringBuilder().append(DataConstants.TEST_CASE_RESULTS).append(DataConstants.UNDERSCORE).append(compilerType).append(DataConstants.UNDERSCORE).append(qid).toString();
  }
}
