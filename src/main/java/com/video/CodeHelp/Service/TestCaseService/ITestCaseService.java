package com.video.CodeHelp.Service.TestCaseService;

import com.video.CodeHelp.Enums.CompilerTypeEnums;
import com.video.CodeHelp.Enums.TestCaseType;
import com.video.CodeHelp.Pojo.TestCase;
import com.video.CodeHelp.Pojo.TestCaseResult;
import com.video.CodeHelp.Pojo.TestCaseSaveRequest;

import java.util.List;

public interface ITestCaseService  {
  List<String> getFormattedTestCase(List<TestCase> testCases, CompilerTypeEnums language);
  List<TestCase> getTestCases(Long qNo, CompilerTypeEnums language, TestCaseType testCaseType);
  void saveTestCases(TestCaseSaveRequest testCaseSaveRequest);
  List<TestCaseResult> getTestCaseResults(Long qNo, CompilerTypeEnums language);
}
