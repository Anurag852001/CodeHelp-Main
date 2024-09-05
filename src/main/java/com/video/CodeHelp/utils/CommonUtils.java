package com.video.CodeHelp.utils;

public class CommonUtils {
  public static String splitAndSeparateByLine(String str){
    String[] lines = str.split("\n");
    StringBuilder result = new StringBuilder();
    for(String line : lines){
      result.append(line).append(System.lineSeparator());
    }
    return result.toString();
  }

}
