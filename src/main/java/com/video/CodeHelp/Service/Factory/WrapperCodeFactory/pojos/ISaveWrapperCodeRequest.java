package com.video.CodeHelp.Service.Factory.WrapperCodeFactory.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.video.CodeHelp.Enums.CompilerTypeEnums;
import com.video.CodeHelp.Service.Factory.WrapperCodeFactory.enums.WrapperCodeEnums;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ISaveWrapperCodeRequest {
 private WrapperCodeEnums wrapperCodeEnum;
}
