package com.video.CodeHelp.Service.QuestionTrackerService.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.video.CodeHelp.Enums.CompilerTypeEnums;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionTrackerRequest {
    @JsonProperty("question_no")
    private long questionNo;
    private String uuid;
    private boolean solved;
    @JsonProperty("language")
    private CompilerTypeEnums compilerTypeEnums;
    private Long runtime;
}
