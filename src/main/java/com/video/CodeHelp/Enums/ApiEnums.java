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
  SUBMIT_CODE_API("/submit/code","submitCode"),
  GET_WRAPPER_CODE("/get/wrapper/code", "getWrapperCode"),
  SAVE_WRAPPER_CODE("/save/wrapper/code", "saveWrapperCode"),


  //testcases api
  TESTCASE_LIST_API("/list/testcases", "testCaseList"),
  TESTCASE_GET_API("/get/testcase", "testCaseGet"),
  TESTCASE_SAVE_API("/save/testcase", "testCaseSave"),
  TESTCASE_UPDATE_API("/update/testcase", "testCaseUpdate"),


  //generic listing api
  GENERIC_LIST_API("/list/generic", "genericList"),

  //variables save api
  SAVE_MAIN_CODE_VARIABLES_API("/save/mainCode/variables", "saveVariables"),
  GET_MAIN_CODE_VARIABLES_API("/get/mainCode/variables", "getMainCodeVariables"),

  //correct code apis
  SAVE_CORRECT_CODE_API("/save/correctCode","saveCorrectCode"),
  GET_CORRECT_CODE_API("/get/correctCode","getCorrectCode"),
  UPDATE_CORRECT_CODE_API("/update/correctCode","updateCorrectCode");

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
