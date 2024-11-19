package com.video.CodeHelp.Pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.video.CodeHelp.Enums.DataTypeEnums;
import com.video.CodeHelp.Enums.TestCaseType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestCase {
  private DataTypeEnums dataType;
  private Integer variableNumber;
  private String value;
  private Long testCaseId;
  private TestCaseType testCaseType;
}
