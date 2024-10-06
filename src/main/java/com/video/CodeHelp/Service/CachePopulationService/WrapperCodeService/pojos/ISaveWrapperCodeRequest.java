package com.video.CodeHelp.Service.CachePopulationService.WrapperCodeService.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.video.CodeHelp.Service.CachePopulationService.WrapperCodeService.enums.WrapperCodeEnums;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ISaveWrapperCodeRequest {
 private WrapperCodeEnums wrapperCodeEnum;
}
