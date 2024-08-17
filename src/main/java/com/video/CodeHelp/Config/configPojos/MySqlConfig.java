package com.video.CodeHelp.Config.configPojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MySqlConfig {
  private String username;
  private String password;
  private String testStatement;
  private String maxTimeMills;
  private String database;
  private String port;
}
