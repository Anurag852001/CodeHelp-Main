package com.video.CodeHelp.Service.TestCaseService;

import com.video.CodeHelp.Enums.CompilerTypeEnums;
import com.video.CodeHelp.Enums.TestCaseGeneratorRules;
import com.video.CodeHelp.Enums.TestCaseType;
import com.video.CodeHelp.Pojo.*;
import com.video.CodeHelp.Service.CompilerService.CompilerFactory;
import com.video.CodeHelp.Service.CorrectCodeService;
import com.video.CodeHelp.Service.MainCodeVariableService;
import com.video.CodeHelp.utils.CommonUtils;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TestCaseGeneratorService {

  private final MainCodeVariableService mainCodeVariableService;
  private final ITestCaseService testCaseService;
  private final CompilerFactory compilerFactory;
  private final CorrectCodeService correctCodeService;
  private final RuleEngineFactory ruleEngineFactory;

  @Inject
  public TestCaseGeneratorService(MainCodeVariableService mainCodeVariableService, ITestCaseService testCaseService, CompilerFactory compilerFactory, CorrectCodeService correctCodeService, RuleEngineFactory ruleEngineFactory){
    this.mainCodeVariableService = mainCodeVariableService;
    this.testCaseService = testCaseService;
    this.compilerFactory = compilerFactory;
    this.correctCodeService = correctCodeService;
    this.ruleEngineFactory = ruleEngineFactory;
  }

  public TestCaseGeneratorResponse generateTestCase(Long qid, CompilerTypeEnums language, Long numberOfTestCases, Map<Long,List<TestCaseGeneratorRules>> variableVsRules){
    List<MainCodeVariable> mainCodeVariables = mainCodeVariableService.getVariables(qid,language);

    for(int i =0;i<numberOfTestCases;i++){
        mainCodeVariables.forEach(mcv->{
          List<TestCase> testCases = generateTestCaseForEachVariable(mainCodeVariables,variableVsRules);
          String correctCode = correctCodeService.getCorrectCode(CorrectCodePojo.builder().qid(qid).language(language).build()).getCode();
          String solution = compilerFactory.getCompiler(language).compileCode(CommonUtils.getCodeCompilingRequest(correctCode,language,testCases,qid));
          TestCaseSaveRequest testCaseSaveRequest = CommonUtils.getTestCaseSaveRequest(testCases,qid,solution,language);
          testCaseService.saveTestCases(testCaseSaveRequest);
       } );
    }
  return TestCaseGeneratorResponse.builder().build();
  }

  private List<TestCase> generateTestCaseForEachVariable(List<MainCodeVariable> mainCodeVariables,Map<Long,List<TestCaseGeneratorRules>> variableVsRules){
    List<TestCase> testCase = new ArrayList<>();
    mainCodeVariables.sort(Comparator.comparingLong(MainCodeVariable::getVariableNumber));
    mainCodeVariables.forEach(mainCodeVariable -> {
      IRuleEngineService ruleEngineService = ruleEngineFactory.getRuleEngineService(mainCodeVariable.getType());


      String generatedValue = ruleEngineService.applyRule(variableVsRules.get(mainCodeVariable.getVariableNumber())).getValue();
      testCase.add(TestCase.builder()
        .testCaseType(TestCaseType.MAIN_TESTCASE)
        .testCaseId(mainCodeVariable.getVariableNumber())
        .dataType(mainCodeVariable.getType())
        .value(generatedValue)
        .build());
    });
    return testCase;
  }
}
