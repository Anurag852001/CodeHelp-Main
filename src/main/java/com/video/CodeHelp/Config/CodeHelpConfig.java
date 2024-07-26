package com.video.CodeHelp.Config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.video.CodeHelp.Config.configPojos.MySqlConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CodeHelpConfig {
  public Integer port;
  public String environment;
  public MySqlConfig mySqlConfig;
}
