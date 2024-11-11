package com.video.CodeHelp.Service;

import com.video.CodeHelp.Dao.CorrectCodeDao;
import com.video.CodeHelp.Enums.ApplicationErrorEnums;
import com.video.CodeHelp.Enums.CacheTypeEnums;
import com.video.CodeHelp.Enums.TestCaseType;
import com.video.CodeHelp.Exception.CodeHelpException;
import com.video.CodeHelp.Pojo.CorrectCodePojo;
import com.video.CodeHelp.Pojo.TestCase;
import com.video.CodeHelp.Service.CompilerService.CompilerFactory;
import com.video.CodeHelp.Service.TestCaseService.ITestCaseService;
import com.video.CodeHelp.utils.CachingUtils;
import com.video.CodeHelp.utils.CommonUtils;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Singleton
@Slf4j
public class CorrectCodeService {

  private final CorrectCodeDao dao;
  private final CachingService cachingService;
  private final CompilerFactory compilerFactory;
  private final ITestCaseService testCaseService;

  @Inject
  public CorrectCodeService(CorrectCodeDao dao, CachingService cachingService, CompilerFactory compilerFactory, ITestCaseService testCaseService) {
    this.dao = dao;
    this.cachingService = cachingService;
    this.compilerFactory = compilerFactory;
    this.testCaseService = testCaseService;
  }


  public Long saveCorrectCode(CorrectCodePojo correctCodePojo){
    //let's first try to compile it
    try {
      List<TestCase> defaultTestCase = testCaseService.getTestCases(correctCodePojo.getQid(),correctCodePojo.getLanguage(), TestCaseType.DEFAULT_TESTCASE);
      compilerFactory.getCompiler(correctCodePojo.getLanguage()).compileCode(CommonUtils.getCodeCompilingRequest(correctCodePojo.getCode(),correctCodePojo.getLanguage(),defaultTestCase,correctCodePojo.getQid()));
      return dao.saveCorrectCode(correctCodePojo.getCode(), correctCodePojo.getLanguage(), correctCodePojo.getQid());
    } catch (CodeHelpException e) {
      log.error("Error while compiling code for request: {}",correctCodePojo,e);
      throw new CodeHelpException(ApplicationErrorEnums.ERROR_WHILE_SAVING_CORRECT_CODE.getMessage() +" "+ e.getMessage());
    } catch (Exception e){
      log.error("Exception while saving correct code for request:{}",correctCodePojo,e);
      throw new CodeHelpException(ApplicationErrorEnums.ERROR_WHILE_SAVING_CORRECT_CODE+e.getMessage());
    }
  }

  public CorrectCodePojo getCorrectCode(CorrectCodePojo correctCodePojo) {
    CorrectCodePojo correctCodeFromCache = (CorrectCodePojo)cachingService.getFromCache(CachingUtils.getCacheKeyForCorrectCode(correctCodePojo.getQid(),correctCodePojo.getLanguage()), CacheTypeEnums.TWO_HUNDERED_CACHE);
    if(correctCodeFromCache == null){
      //reviving it from the database
      CorrectCodePojo correctCode = dao.getCorrectCodeByQNoAndLanguage(correctCodePojo.getQid(), correctCodePojo.getLanguage());
      if(correctCode!= null){
        cachingService.populateInCache(CachingUtils.getCacheKeyForCorrectCode(correctCode.getQid(),correctCode.getLanguage()), correctCode, CacheTypeEnums.TWO_HUNDERED_CACHE);
      }
      return correctCode;
    }
    return null;
  }

  public void updateCorrectCode(CorrectCodePojo correctCodePojo) {
    try {
      List<TestCase> defaultTestCase = testCaseService.getTestCases(correctCodePojo.getQid(),correctCodePojo.getLanguage(), TestCaseType.DEFAULT_TESTCASE);
      compilerFactory.getCompiler(correctCodePojo.getLanguage()).compileCode(CommonUtils.getCodeCompilingRequest(correctCodePojo.getCode(),correctCodePojo.getLanguage(),defaultTestCase,correctCodePojo.getQid()));
      dao.updateCorrectCode(correctCodePojo.getCode(), correctCodePojo.getQid(), correctCodePojo.getLanguage());
    } catch (CodeHelpException e) {
      log.error("Error while compiling code for request:{}",correctCodePojo,e);
      throw new CodeHelpException(ApplicationErrorEnums.ERROR_WHILE_UPDATING_CORRECT_CODE +" "+ e.getMessage());
    } catch (Exception e) {
      log.error("Exception while updating correct code for request:{}",correctCodePojo,e);
      throw new CodeHelpException(ApplicationErrorEnums.ERROR_WHILE_UPDATING_CORRECT_CODE);
    }
  }
}
