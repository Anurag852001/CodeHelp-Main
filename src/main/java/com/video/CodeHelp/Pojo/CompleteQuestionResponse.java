package com.video.CodeHelp.Pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompleteQuestionResponse {
  private QuestionBodyResponse questionBodyResponse;
  private QuestionsConstraintsResponse questionsConstraintsResponse;
  private QuestionsExamplesResponse questionsExamplesResponse;
}
