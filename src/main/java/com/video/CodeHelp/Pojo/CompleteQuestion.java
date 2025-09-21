package com.video.CodeHelp.Pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.video.CodeHelp.Enums.CompilerTypeEnums;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompleteQuestion {
  @NotNull("question Body is a mandatory field")
  private QuestionBody questionBody;
  @NotNull("question constraints are mandatory")
  private List<QuestionConstraints> questionConstraints;
  @NotNull("questionExamples are mandatory")
  private List<QuestionExamples> questionExamples;
  private List<VariableSaveRequest> variables;
  private String correctCode;
  @NotNull("language is mandatory")
  private CompilerTypeEnums language;
  //to be set when getting
  private String defaultCode = "ssdadsd";
}
