package com.video.CodeHelp.utils;

import com.video.CodeHelp.Constants.DataConstants;
import com.video.CodeHelp.Enums.CompilerTypeEnums;
import com.video.CodeHelp.Enums.TestCaseType;
import com.video.CodeHelp.Pojo.CodeCompilingRequest;
import com.video.CodeHelp.Pojo.TestCase;
import com.video.CodeHelp.Pojo.TestCaseSaveRequest;
import io.vertx.core.json.JsonObject;

import java.util.List;

public class CommonUtils {
  public static String splitAndSeparateByLine(String str){
    String[] lines = str.split("\n");
    StringBuilder result = new StringBuilder();
    for(String line : lines){
      result.append(line).append(System.lineSeparator());
    }
    return result.toString();
  }

  public static CodeCompilingRequest getCodeCompilingRequest(String code, CompilerTypeEnums compilerTypeEnum, List<TestCase> testCaseList,Long qid){
    return CodeCompilingRequest.builder()
      .code(code)
      .compilerType(compilerTypeEnum)
      .testCase(testCaseList)
      .qid(qid)
      .runOnAll(false)
      .build();
  }

  public static JsonObject prepareResponseForCompileCodeApi(String result,String expectedResult,Long timeTaken){
    return new JsonObject().put(DataConstants.RESULT,result)
      .put(DataConstants.EXPECTED_RESULT,expectedResult)
      .put(DataConstants.TIME_TAKEN,timeTaken)
      .put(DataConstants.SUCCESS,result.equalsIgnoreCase(expectedResult));
  }

  public static TestCaseSaveRequest getTestCaseSaveRequest(List<TestCase> testCases, Long qid, String solution, CompilerTypeEnums language) {
  return TestCaseSaveRequest.builder().build();
  }
}
