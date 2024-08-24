package com.video.CodeHelp.Pojo.Responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class SaveQuestionResponse {
  private Long qId;
  private Long qDataId;
  private List<Long> constraintId;
  private List<Long> examplesId;
}
