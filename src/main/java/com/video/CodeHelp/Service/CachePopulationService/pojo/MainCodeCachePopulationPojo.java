package com.video.CodeHelp.Service.CachePopulationService.pojo;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.video.CodeHelp.Enums.CompilerTypeEnums;
import com.video.CodeHelp.Enums.DataTypeEnums;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MainCodeCachePopulationPojo {
  private String mainCode;
  private CompilerTypeEnums compilerType;
  private Long qid;
  private DataTypeEnums returnType;
}
