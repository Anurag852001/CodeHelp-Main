package com.video.CodeHelp.Service.TestCaseService.impl;

import com.video.CodeHelp.Enums.ApplicationErrorEnums;
import com.video.CodeHelp.Enums.TestCaseGeneratorRules;
import com.video.CodeHelp.Exception.CodeHelpException;
import com.video.CodeHelp.Pojo.Responses.RuleEngineResponse;
import com.video.CodeHelp.Pojo.TestCaseRuleInfo;
import com.video.CodeHelp.Service.TestCaseService.IRuleEngineService;

import java.util.*;

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
    if(size>maximumValue){
      throw new CodeHelpException(ApplicationErrorEnums.SIZE_SHOULD_BE_LESS_THAN_MAX);
    }
    Random random = new Random();
    HashSet<Integer> set = new HashSet<Integer>();
    while(set.size()<size){
      set.add(random.nextInt(maximumValue+1));
    }
    List<Integer> list = new ArrayList<Integer>(set);
    Collections.shuffle(list);
    return list.toArray(new Integer[0]);
  }


  public static Integer[] generateNegativePositiveUniqueIntegerArray(Integer size,Integer maximumValue){
    if(size>maximumValue){
      throw new CodeHelpException(ApplicationErrorEnums.SIZE_SHOULD_BE_LESS_THAN_MAX);
    }
    Random random = new Random();
    HashSet<Integer> set = new HashSet<Integer>();
    while(set.size()<size){
      Integer val = random.nextInt(maximumValue+1);
      if(set.size()%2 == 0){
        set.add(-val);
      } else{
        set.add(val);
      }
    }
    List<Integer> list = new ArrayList<Integer>(set);
    Collections.shuffle(list);
    return list.toArray(new Integer[0]);
  }


  @Override
  public RuleEngineResponse applyRule(List<TestCaseGeneratorRules> testCaseRuleInfo) {
    return null;
  }
}
