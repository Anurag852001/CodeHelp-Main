package com.video.CodeHelp.Service.Factory.WrapperCodeFactory.pojos;

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
public class DefaultWrapperCodeResponse implements IWrapperCodeResponse{
  public String defaultCode;
  public CompilerTypeEnums language;
  public String qNo;
}
