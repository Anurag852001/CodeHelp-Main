package com.video.CodeHelp.utils;

public class CachingUtils {

  public static String getCacheKeyForQuestionBody(Long qNo){
    StringBuilder sb = new StringBuilder();
    sb.append("QUESTION_HEADERS_").append(qNo);
    return sb.toString();
  }
  public static String getCacheKeyForQuestionExamples(Long qNo){
    StringBuilder sb = new StringBuilder();
    sb.append("QUESTION_EXAMPLES_").append(qNo);
    return sb.toString();
  }

  public static String getCacheKeyForQuestionConstraints(Long qNo){
    StringBuilder sb = new StringBuilder();
    sb.append("QUESTION_CONSTRAINTS_").append(qNo);
    return sb.toString();
  }
}
