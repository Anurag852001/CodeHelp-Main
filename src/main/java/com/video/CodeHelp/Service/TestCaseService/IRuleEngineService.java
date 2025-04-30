package com.video.CodeHelp.Service.TestCaseService;

import com.video.CodeHelp.Enums.TestCaseGeneratorRules;
import com.video.CodeHelp.Pojo.Responses.RuleEngineResponse;
import com.video.CodeHelp.Pojo.TestCaseRuleInfo;

import java.util.List;

public interface IRuleEngineService {
  public RuleEngineResponse applyRule(List<TestCaseGeneratorRules> testCaseRuleInfo);
}
