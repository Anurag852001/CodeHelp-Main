package com.video.CodeHelp.Service.Factory.WrapperCodeFactory.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.video.CodeHelp.Enums.CompilerTypeEnums;
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
public class MainWrapperCode implements IWrapperCodeResponse{
  private String mainCode;
  private CompilerTypeEnums compilerType;
  private Long qid;
}
