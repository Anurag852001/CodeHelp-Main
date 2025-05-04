package com.video.CodeHelp.Pojo.Responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubmitCodeResponse {
  private Integer testCasesPassed = 0;
  private Integer totalTestCases = 0;
  private Boolean failed = false;
  private Long timeTake  = 0L;
  private String lastTestCaseResultBeforeFailure;
  private String expectedLastTestCaseResultBeforeFailure;
  private List<String> resultOfEachTestCase;
  private List<String> expectedResultOfTestCase;
}
