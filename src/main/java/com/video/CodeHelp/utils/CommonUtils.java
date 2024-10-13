package com.video.CodeHelp.utils;

import com.video.CodeHelp.Enums.CompilerTypeEnums;
import com.video.CodeHelp.Pojo.CodeCompilingRequest;
import com.video.CodeHelp.Pojo.TestCase;

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

}
