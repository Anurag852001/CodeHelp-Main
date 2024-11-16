package com.video.CodeHelp.Service.TestCaseService;

public interface Rule {
  <T> void applyRule(T input, T... params);
}
