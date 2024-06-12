package com.video.CodeHelp.Enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ApiEnums {
  WELCOME_API("/welcome/api", "welcomeApiHandler"),
  CONFIG_SAVE_API("/save/config", "saveConfig");

  String apiKey;
  String eventPath;

  public ApiEnums fromValue(String apiKey) {
    for (ApiEnums api : ApiEnums.values()) {
      if (api.getApiKey().equalsIgnoreCase(apiKey)) {
        return api;
      }
    }
    return null;
  }
}
