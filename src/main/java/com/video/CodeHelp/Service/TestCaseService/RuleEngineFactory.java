package com.video.CodeHelp.Service.TestCaseService;

import com.video.CodeHelp.Constants.DataConstants;
import com.video.CodeHelp.Enums.DataTypeEnums;
import com.video.CodeHelp.Exception.CodeHelpException;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.N;

@Singleton
@Slf4j
public class RuleEngineFactory {

  IRuleEngineService integerRuleEngineService;
  IRuleEngineService integerArrayRuleEngineService;

  @Inject
  public RuleEngineFactory(@Named(DataConstants.INTEGER_RULE_ENGINE_SERVICE) IRuleEngineService integerRuleEngineService,
                           @Named(DataConstants.INTEGER_ARRAY_RULE_ENGINE_SERVICE) IRuleEngineService integerArrayRuleEngineService){
    this.integerRuleEngineService = integerRuleEngineService;
    this.integerArrayRuleEngineService = integerArrayRuleEngineService;
  }

  public IRuleEngineService getRuleEngineService(DataTypeEnums dataType){
    switch (dataType) {
      case INTEGER :
        return integerRuleEngineService;
      case INTEGER_ARRAY:
        return integerArrayRuleEngineService;
      default:
        log.error("Rule engine service not supported:{}" ,dataType);
        throw new CodeHelpException("Rule Engine service not supported");
    }
  }
}
