package com.video.CodeHelp.Service.TestCaseService.TestCaseGeneratorRules;

import java.util.Arrays;

public class IntegerRulesImpl {
  public static Integer generateRandomInteger(Integer size,Integer input){
    int randomSize = (int) (Math.random() * size+1);
    return  (int) (Math.random() * Math.pow(10, randomSize));
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

}
