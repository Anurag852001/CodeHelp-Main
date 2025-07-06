package com.video.CodeHelp.Pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.video.CodeHelp.Enums.CompilerTypeEnums;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CodeCompilingRequest {
  private String code;
  private String correctCode;
  private Long qid;
  private CompilerTypeEnums compilerType;
  private Boolean runOnAll;
  private List<TestCase> testCase;
  private boolean testCompilation;
}
