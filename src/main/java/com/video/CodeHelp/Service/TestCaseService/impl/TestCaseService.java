package com.video.CodeHelp.Service.TestCaseService.impl;

import com.google.inject.Inject;
import com.video.CodeHelp.Constants.DataConstants;
import com.video.CodeHelp.Constants.MongoConstants;
import com.video.CodeHelp.Dao.TestCaseDao;
import com.video.CodeHelp.Enums.CacheTypeEnums;
import com.video.CodeHelp.Enums.CompilerTypeEnums;
import com.video.CodeHelp.Enums.TestCaseType;
import com.video.CodeHelp.Pojo.TestCase;
import com.video.CodeHelp.Pojo.TestCaseResult;
import com.video.CodeHelp.Pojo.TestCaseRuleInfo;
import com.video.CodeHelp.Pojo.TestCaseSaveRequest;
import com.video.CodeHelp.Service.CachingService;
import com.video.CodeHelp.Service.TestCaseService.ITestCaseService;
import com.video.CodeHelp.mongo.MongoService;
import com.video.CodeHelp.utils.CachingUtils;
import io.vertx.core.json.JsonObject;
import jakarta.inject.Singleton;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Singleton
public class TestCaseService implements ITestCaseService {

  private final TestCaseDao dao;
  private final CachingService cachingService;
  private final MongoService mongoService;

  @Inject
  public TestCaseService(TestCaseDao dao, CachingService cachingService,MongoService mongoService) {
    this.dao = dao;
    this.cachingService = cachingService;
    this.mongoService = mongoService;
  }

  @Override
  public List<String> getFormattedTestCase(List<TestCase> testCases, CompilerTypeEnums language) {
    switch (language) {
      case JAVA:
        return getFormattedTestCaseForJava(testCases);
      default:
        log.error("Not implemented test case formmater for this");
        return null;
    }
  }

  @Override
  public List<TestCase> getTestCases(Long qId, CompilerTypeEnums language, TestCaseType testCaseType) {
    List<TestCase> testCases = (List<TestCase>) cachingService.getFromCache(CachingUtils.getCacheKeyForTestCase(language, qId), CacheTypeEnums.TWO_HUNDERED_CACHE);
    if (CollectionUtils.isEmpty(testCases)) {
      testCases = dao.getTestCases(qId, testCaseType);
      cachingService.populateInCache(CachingUtils.getCacheKeyForTestCase(language, qId), testCases, CacheTypeEnums.TWO_HUNDERED_CACHE);
      return testCases;
    }
    return null;
  }

  @Override
  public void saveTestCases(TestCaseSaveRequest request) {
    //TODO need to add heavy validations here
    List<JsonObject> docs = request.getTestCases().stream().map(testCase->{
      return new JsonObject().put(DataConstants.QID,request.getQid()).put(DataConstants.TEST_CASE_CAMEL,testCase);
    }).collect(Collectors.toList());
    mongoService.insertMultiple(MongoConstants.TESTCASES,docs);
  }

  @Override
  public List<TestCaseResult> getTestCaseResults(Long qid, CompilerTypeEnums language) {
   List<TestCaseResult> testCasesResults = (List<TestCaseResult>) cachingService.getFromCache(CachingUtils.getCacheKeyForTestCaseResults(language,qid),CacheTypeEnums.TWO_HUNDERED_CACHE);
   if(testCasesResults == null){
     List<TestCaseResult> testCaseResultsFromDao = dao.getTestCaseResults(language,qid);
     cachingService.populateInCache(CachingUtils.getCacheKeyForTestCaseResults(language,qid),testCaseResultsFromDao,CacheTypeEnums.TWO_HUNDERED_CACHE);
     return testCaseResultsFromDao;
   }
   return testCasesResults;

  }



  public List<String> getFormattedTestCaseForJava(List<TestCase> testCases) {
    if(CollectionUtils.isEmpty(testCases)){
      log.info("No test cases to format");
      return new ArrayList<>();
    }
    //lets sort first
    testCases.sort(Comparator.comparing(TestCase::getVariableNumber));
    return testCases.stream().map(testCase -> {
      String testCaseValue = testCase.getValue();
      switch (testCase.getDataType()) {
        case INTEGER_ARRAY:
          return "{" + testCaseValue.substring(1, testCaseValue.length() - 1) +"};";
        default:
          return testCaseValue+";";
      }
    }).collect(Collectors.toList());
  }

  public List<TestCaseRuleInfo> getTestCaseRuleInfo(Long qid,Long variableNumber){
    return dao.getTestCaseGeneratorRules(qid, variableNumber);
  }

  @Override
  public void saveTestCasesRuleInfo(TestCaseRuleInfo testCaseRuleInfo) {

  }

}
