package com.video.CodeHelp.Service.TestCaseService.impl;

import com.video.CodeHelp.Pojo.Responses.RuleEngineResponse;
import com.video.CodeHelp.Pojo.TestCaseRuleInfo;
import com.video.CodeHelp.Service.TestCaseService.IRuleEngineService;

import java.util.ArrayList;
import java.util.List;

import static com.video.CodeHelp.Service.TestCaseService.impl.IntegerRulesImpl.generateRandomInteger;


public class IntegerArrayRuleEngineService implements IRuleEngineService {

  public static Integer[] generateIntegerArray(Integer size,Integer maximumValue){
    List<Integer> newArray  = new ArrayList<Integer>();
    while(newArray.size() < size){
      newArray.add(generateRandomInteger(maximumValue,null));
    }
    return newArray.toArray(new Integer[0]);
  }

  public static Integer[] sortIntegerArrayInAsc(Integer size, Integer maximumValue){
    return null;
  }

  public static Integer[] generateNegativePositiveIntegerArray(Integer size,Integer maximumSize){
    List<Integer> array = new ArrayList<Integer>();
    while(array.size() < size){
      array.add(IntegerRulesImpl.generateNegativeOrPositiveRandomInteger(maximumSize,null));
    }
    return array.toArray(new Integer[0]);
  }

  public static Integer[] generateUniqueIntegerArray(Integer size,Integer maximumValue){
    //code to generate unique integer
    return null;
  }

  public static Integer[] generateNegativePositiveUniqueIntegerArray(Integer size,Integer maximumValue){
    return null;
  }

  @Override
  public RuleEngineResponse applyRule(List<TestCaseRuleInfo> testCaseRuleInfo) {
    return null;
  }
}
