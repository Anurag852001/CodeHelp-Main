package com.video.CodeHelp.Service.TestCaseService.TestCaseGeneratorRules;

import com.video.CodeHelp.Service.TestCaseService.impl.IntegerArrayRuleEngineService;
import lombok.Getter;

import java.util.function.BiFunction;

@Getter
public enum IntegerArrayRules {
  GENERATE_INTEGER_ARRAY(IntegerArrayRuleEngineService::generateIntegerArray),
  SORT_ARRAY(IntegerArrayRuleEngineService::sortIntegerArrayInAsc),
  GENERATE_UNIQUE_ARRAY(IntegerArrayRuleEngineService::generateUniqueIntegerArray),
  GENERATE_POSITIVE_NEGATIVE_ARRAY(IntegerArrayRuleEngineService::generateNegativePositiveIntegerArray),
  GENERATE_POSITIVE_NEGATIVE_UNIQUE_ARRAY(IntegerArrayRuleEngineService::generateNegativePositiveUniqueIntegerArray);

  final BiFunction<Integer, Integer, Integer[]> ruleFunction;

  IntegerArrayRules(BiFunction<Integer, Integer, Integer[]> ruleFunction) {
    this.ruleFunction = ruleFunction;
  }

  public Integer[] applyRule(Integer size, Integer maximumValue) {
    return ruleFunction.apply(size, maximumValue);
  }
}
