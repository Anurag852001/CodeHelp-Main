package com.video.CodeHelp.Service.TestCaseService.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.video.CodeHelp.Enums.TestCaseGeneratorRules;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestCaseGeneratorRequest {
    @NotNull("qId is mandatory field")
    private Long qId;
    @NotNull("number of test cases is mandatory")
    private Long numberOfTestCases;
    @NotNull("variable number vs rules map is mandatory")
    private Map<Long, List<TestCaseGeneratorRules>> variableNumberVsRules;
}
