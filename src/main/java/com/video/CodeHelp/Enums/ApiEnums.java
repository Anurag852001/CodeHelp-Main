package com.video.CodeHelp.Enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ApiEnums {
  WELCOME_API("/welcome/api", "welcomeApiHandler"),

  //config apis
  CONFIG_SAVE_API("/save/config", "saveConfig"),
  CONFIG_GET_API("/get/config", "getConfig"),
  CONFIG_UPDATE_API("/update/config", "updateConfig"),

  //cache get api
  CACHE_GET_API("/get/cache", "cacheGet"),


  //Question apis
  QUESTION_LIST_API("/list/questions", "questionList"),
  QUESTION_GET_API("/get/question", "questionGet"),
  QUESTION_SAVE_API("/save/question", "questionSave"),
  QUESTION_UPDATE_API("/update/question", "questionUpdate"),
  QUESTION_DELETE_API("/delete/question", "questionDelete"),


  //Compiler apis
  COMPILE_CODE_API("/compile/code","compileCode"),
  GET_WRAPPER_CODE("/get/wrapper/code", "defaultCode"),
  SAVE_WRAPPER_CODE("/save/wrapper/code", "defaultCode"),


  //testcases api
  TESTCASE_LIST_API("/list/testcases", "testCaseList"),
  TESTCASE_GET_API("/get/testcase", "testCaseGet"),
  TESTCASE_SAVE_API("/save/testcase", "testCaseSave"),
  TESTCASE_UPDATE_API("/update/testcase", "testCaseUpdate");

  String apiKey;
  String eventPath;

  public static ApiEnums fromValue(String apiKey) {
    for (ApiEnums api : ApiEnums.values()) {
      if (api.getApiKey().equalsIgnoreCase(apiKey)) {
        return api;
      }
    }
    return null;
  }
}
