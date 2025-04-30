package com.video.CodeHelp.Service.TestCaseService.TestCaseGeneratorRules;

import com.video.CodeHelp.Service.TestCaseService.impl.IntegerRulesImpl;
import lombok.Getter;

import java.util.function.BiFunction;


@Getter
public enum IntegerRules {
  GENERATE_RANDOM_INTEGER(IntegerRulesImpl::generateRandomInteger),
  SORT_DIGITS_IN_ASC(IntegerRulesImpl::sortIntegerDigitsInAsc),
  SORT_DIGITS_IN_DESC(IntegerRulesImpl::sortIntegerDigitsInAsc);

  private final BiFunction<Integer, Integer, Integer> ruleFunction;

  IntegerRules(BiFunction<Integer, Integer, Integer> ruleFunction) {
    this.ruleFunction = ruleFunction;
  }

  public Integer applyRule(int input, int size) {
    return ruleFunction.apply(input, size);
  }


}
