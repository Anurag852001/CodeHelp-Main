package com.video.CodeHelp.Pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestCaseRuleInfo {
      public Integer id;
      public Integer qid;
      public Integer variableNumber;
      public String rule;
      public Long rule_number;
      public JsonObject params;
}
