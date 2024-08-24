package com.video.CodeHelp.Pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompleteQuestion {
  private QuestionBody questionBody;
  private List<QuestionConstraints> questionConstraints;
  private List<QuestionExamples> questionExamples;
}
