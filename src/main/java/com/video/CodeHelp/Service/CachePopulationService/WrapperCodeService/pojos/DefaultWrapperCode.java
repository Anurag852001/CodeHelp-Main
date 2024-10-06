package com.video.CodeHelp.Service.CachePopulationService.WrapperCodeService.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class DefaultWrapperCode implements IWrapperCodeResponse{
  private String defaultCode;
  private CompilerTypeEnums compilerType;
  private Long qid;
}

