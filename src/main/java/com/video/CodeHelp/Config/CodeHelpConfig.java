package com.video.CodeHelp.Config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.video.CodeHelp.Config.configPojos.CodeHelpReportingServiceConfig;
import com.video.CodeHelp.Config.configPojos.MongoConfig;
import com.video.CodeHelp.Config.configPojos.MySqlConfig;
import com.video.CodeHelp.Service.CachePopulationService.enums.CachePopulationTypes;
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
public class CodeHelpConfig {
  public Long port;
  public String environment;
  public MySqlConfig mySqlConfig;
  public List<CachePopulationTypes> cachePopulationTypes;
  public MongoConfig mongoConfig;
  public CodeHelpReportingServiceConfig codeHelpReportingServiceConfig;
}
