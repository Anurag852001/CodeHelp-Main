package com.video.CodeHelp.Service.TestCaseService;

import com.video.CodeHelp.Pojo.Responses.RuleEngineResponse;

import java.util.List;

public interface IRuleEngineService {
  public RuleEngineResponse applyRule(String rule, List<String> params);
}
