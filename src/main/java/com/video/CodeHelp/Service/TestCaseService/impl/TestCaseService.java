package com.video.CodeHelp.Service.TestCaseService.impl;

import com.video.CodeHelp.Enums.CompilerTypeEnums;
import com.video.CodeHelp.Pojo.TestCase;
import com.video.CodeHelp.Service.TestCaseService.ITestCaseService;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Singleton
public class TestCaseService implements ITestCaseService {
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

  public List<String> getFormattedTestCaseForJava(List<TestCase> testCases) {
    //lets sort first
    testCases.sort(Comparator.comparing(TestCase::getVariableNumber));
    return testCases.stream().map(testCase -> {
      String testCaseValue = testCase.getValue();
      switch (testCase.getType()) {
        case INTEGER_ARRAY:
          return "{" + testCaseValue.substring(1, testCaseValue.length() - 1) +"};";
        default:
          return testCaseValue+";";
      }
    }).collect(Collectors.toList());
  }
}
