package com.video.CodeHelp.Service.TestCaseService.impl;

import com.video.CodeHelp.Constants.DataConstants;
import com.video.CodeHelp.Enums.TestCaseGeneratorRules;
import com.video.CodeHelp.Pojo.Responses.RuleEngineResponse;
import com.video.CodeHelp.Pojo.RunRuleRequest;
import com.video.CodeHelp.Pojo.TestCaseRuleInfo;
import com.video.CodeHelp.Service.TestCaseService.IRuleEngineService;
import com.video.CodeHelp.Service.TestCaseService.TestCaseGeneratorRules.IntegerArrayRules;
import com.video.CodeHelp.Service.TestCaseService.TestCaseGeneratorRules.IntegerRules;
import io.vertx.core.json.JsonObject;

import java.util.Arrays;
import java.util.List;

public class IntegerRulesImpl implements IRuleEngineService {
  public static Integer generateRandomInteger(Integer size,Integer input){
    int randomSize = (int) (Math.random() * size+1);
    return  (int) (Math.random() * Math.pow(10, randomSize));
  }

  public static Integer generateNegativeOrPositiveRandomInteger(Integer size,Integer input){
    return (int) (Math.random() * Math.pow(10, size)) * (Math.random() < 0.5? -1 : 1);
  }


  public static Integer sortIntegerDigitsInAsc(Integer number,Integer input){
    char[] digits = number.toString().toCharArray();
    Arrays.sort(digits);
    return Integer.parseInt(new String(digits));
  }

  public static Integer sortIntegerDigitsInDesc(Integer number,Integer input){
    char[] digits = number.toString().toCharArray();
    Arrays.sort(digits);
    return Integer.parseInt(new StringBuilder().append(new String(digits)).reverse().toString());
  }

  @Override
  public RuleEngineResponse applyRule(List<TestCaseGeneratorRules> testCaseRuleInfo) {
    for (TestCaseGeneratorRules info : testCaseRuleInfo) {
     Integer[] rule = IntegerArrayRules.valueOf(info.toString()).applyRule(100,100);
    }
    return RuleEngineResponse.builder().build();
  }
}
