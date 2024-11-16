package com.video.CodeHelp.Service.TestCaseService;

import com.video.CodeHelp.Enums.CompilerTypeEnums;
import com.video.CodeHelp.Enums.TestCaseType;
import com.video.CodeHelp.Pojo.*;
import com.video.CodeHelp.Service.CompilerService.CompilerFactory;
import com.video.CodeHelp.Service.CompilerService.ICompilerService;
import com.video.CodeHelp.Service.CorrectCodeService;
import com.video.CodeHelp.Service.MainCodeVariableService;
import com.video.CodeHelp.Service.TestCaseService.impl.TestCaseService;
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

  @Inject
  public TestCaseGeneratorService(MainCodeVariableService mainCodeVariableService, ITestCaseService testCaseService, CompilerFactory compilerFactory, CorrectCodeService correctCodeService){
    this.mainCodeVariableService = mainCodeVariableService;
    this.testCaseService = testCaseService;
    this.compilerFactory = compilerFactory;
    this.correctCodeService = correctCodeService;
  }

  public TestCaseGeneratorResponse generateTestCase(Long qid,CompilerTypeEnums language, Long numberOfTestCases){
    List<MainCodeVariable> mainCodeVariables = mainCodeVariableService.getVariables(qid,language);
    Map<Long,List<TestCaseRuleInfo>> variableVstestCaseRuleInfo = mainCodeVariables.stream()
      .collect(Collectors.toMap(m->m.getVariableNumber(),m->testCaseService.getTestCaseRuleInfo(qid,m.getVariableNumber())));

    for(int i =0;i<numberOfTestCases;i++){
        mainCodeVariables.stream().forEach(mcv->{
          List<TestCase> testCases = generateTestCaseForEachVariable(mainCodeVariables,variableVstestCaseRuleInfo);
          String correctCode = correctCodeService.getCorrectCode(CorrectCodePojo.builder().qid(qid).language(language).build()).getCode();
          String solution = compilerFactory.getCompiler(language).compileCode(CommonUtils.getCodeCompilingRequest(correctCode,language,testCases,qid));
          TestCaseSaveRequest testCaseSaveRequest = CommonUtils.getTestCaseSaveRequest(testCases,qid,solution,language);
          testCaseService.saveTestCases(testCaseSaveRequest);
       } );
    }

  }

  private List<TestCase> generateTestCaseForEachVariable(List<MainCodeVariable> mainCodeVariables,Map<Long,List<TestCaseRuleInfo>> variableVsTestCaseRuleInfo){
    List<TestCase> testCase = new ArrayList<>();
    mainCodeVariables.sort(Comparator.comparingLong(mainCodeVariable -> mainCodeVariable.getVariableNumber()));
    mainCodeVariables.forEach(mainCodeVariable -> {
      RuleEngineService ruleEngineService = mainCodeVariable.getType().getRuleEngineService();
      List<TestCaseRuleInfo> testCaseRuleInfo = variableVsTestCaseRuleInfo.get(mainCodeVariable.getVariableNumber());
      String generatedValue = testCaseRuleInfo.forEach(tcR->
        ruleEngineService.applyRule(tcR.getRule(), tcR.getParams())
        );
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
