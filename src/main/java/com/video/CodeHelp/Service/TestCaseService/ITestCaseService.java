package com.video.CodeHelp.Service.TestCaseService;

import com.video.CodeHelp.Enums.CompilerTypeEnums;
import com.video.CodeHelp.Pojo.TestCase;

import java.util.List;

public interface ITestCaseService  {
  List<String> getFormattedTestCase(List<TestCase> testCases, CompilerTypeEnums language);
}
