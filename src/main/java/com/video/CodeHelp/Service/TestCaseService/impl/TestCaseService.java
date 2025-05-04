package com.video.CodeHelp.Service.TestCaseService.impl;

import com.google.inject.Inject;
import com.video.CodeHelp.Constants.DataConstants;
import com.video.CodeHelp.Constants.MongoConstants;
import com.video.CodeHelp.Dao.TestCaseDao;
import com.video.CodeHelp.Enums.CacheTypeEnums;
import com.video.CodeHelp.Enums.CompilerTypeEnums;
import com.video.CodeHelp.Enums.TestCaseType;
import com.video.CodeHelp.Pojo.*;
import com.video.CodeHelp.Service.CachingService;
import com.video.CodeHelp.Service.MainCodeVariableService;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.video.CodeHelp.Enums.DataTypeEnums.INTEGER_ARRAY;


@Slf4j
@Singleton
public class TestCaseService implements ITestCaseService {

  private final TestCaseDao dao;
  private final CachingService cachingService;
  private final MongoService mongoService;
  private final MainCodeVariableService mainCodeVariableService;

  @Inject
  public TestCaseService(TestCaseDao dao, CachingService cachingService,MongoService mongoService, MainCodeVariableService mainCodeVariableService) {
    this.dao = dao;
    this.cachingService = cachingService;
    this.mongoService = mongoService;
    this.mainCodeVariableService =mainCodeVariableService;
  }

  @Override
  public List<List<String>> getFormattedTestCase(List<TestCase> testCases,Long qid, CompilerTypeEnums language) {
    switch (language) {
      case JAVA:
        return getFormattedTestCaseForJava(testCases,qid,language);
      default:
        log.error("Not implemented test case formmater for this");
        return null;
    }
  }

  @Override
  public List<TestCase> getTestCases(Long qId, CompilerTypeEnums language, TestCaseType testCaseType) {
    //these testcase should be refreshed into cache
    List<TestCase> testCases = (List<TestCase>) cachingService.getFromCache(CachingUtils.getCacheKeyForTestCase(language, qId), CacheTypeEnums.TWO_HUNDERED_CACHE);
    if (CollectionUtils.isEmpty(testCases)) {
      testCases = mongoService.getMultiple(MongoConstants.TESTCASES,new JsonObject().put(DataConstants.QID,qId))
              .stream().map(jsonObject -> jsonObject.mapTo(TestCase.class)).collect(Collectors.toList());
      cachingService.populateInCache(CachingUtils.getCacheKeyForTestCase(language, qId), testCases, CacheTypeEnums.TWO_HUNDERED_CACHE);
      return testCases;
    }
    return testCases;
  }

  @Override
  public void saveTestCases(TestCaseSaveRequest request) {
    //TODO need to add heavy validations here
    List<JsonObject> docs = new ArrayList<>();
         for(int i = 0 ;i < request.getTestCases().size(); i++){
           JsonObject jsonObject = new JsonObject();
           jsonObject.put(DataConstants.QID,request.getQid());
           jsonObject.put(DataConstants.TEST_CASE_CAMEL,request.getTestCases().get(i));
           jsonObject.put(DataConstants.SOLUTION,request.getSolutions().get(i));
           docs.add(jsonObject);
         }
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



  public List<List<String>> getFormattedTestCaseForJava(List<TestCase> testCases,Long qid,CompilerTypeEnums compilerTypeEnums) {
    if(CollectionUtils.isEmpty(testCases)){
      log.info("No test cases to format");
      return new ArrayList<>();
    }
    List<List<String>> formattedTestCases = new ArrayList<>();
    List<MainCodeVariable> mainCodeVariables = mainCodeVariableService.getVariables(qid,compilerTypeEnums);
    Collections.sort(mainCodeVariables,(x,y)-> Long.compare(x.getVariableNumber() , y.getVariableNumber()));
    testCases.forEach(testCase -> {
      List<String> currentFormattedTestCase = new ArrayList<>();
      for(int i = 0; i<mainCodeVariables.size(); i++){
        switch (mainCodeVariables.get(i).getType()){
          case INTEGER_ARRAY -> currentFormattedTestCase.add("{" + testCase.getTestCase().get(i).substring(1, testCase.getTestCase().get(i).length() - 1) +"};");
          default ->currentFormattedTestCase.add(testCase.getTestCase().get(i)+";");
        }
      }
      formattedTestCases.add(currentFormattedTestCase);
    });
    return formattedTestCases;
  }

  public List<TestCaseRuleInfo> getTestCaseRuleInfo(Long qid,Long variableNumber){
    return dao.getTestCaseGeneratorRules(qid, variableNumber);
  }

  @Override
  public void saveTestCasesRuleInfo(TestCaseRuleInfo testCaseRuleInfo) {

  }

}
