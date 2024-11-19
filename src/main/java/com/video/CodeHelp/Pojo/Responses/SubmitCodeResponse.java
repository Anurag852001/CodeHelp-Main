package com.video.CodeHelp.Pojo.Responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubmitCodeResponse {
  private Integer testCasesPassed = 0;
  private Integer totalTestCases = 0;
  private Boolean failed = false;
  private Long timeTake  = 0L;
  private String lastTestCaseResultBeforeFailure;
  private String expectedLastTestCaseResultBeforeFailure;
}
