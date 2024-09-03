package com.video.CodeHelp.Service.Factory.WrapperCodeFactory.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.video.CodeHelp.Enums.CompilerTypeEnums;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MainWrapperCodeResponse implements IWrapperCodeResponse {
  Map<Long,String> lineNumberVsMainCode;
  CompilerTypeEnums language;
  Long qNo;
}
