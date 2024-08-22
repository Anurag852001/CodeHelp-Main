package com.video.CodeHelp.Enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ApiEnums {
  WELCOME_API("/welcome/api", "welcomeApiHandler"),

  //config apis
  CONFIG_SAVE_API("/save/config", "saveConfig"),
  CONFIG_GET_API("/get/config", "getConfig"),
  CONFIG_UPDATE_API("/update/config", "updateConfig"),

  //cache get api
  CACHE_GET_API("/get/cache", "cacheGet");

  String apiKey;
  String eventPath;

  public static ApiEnums fromValue(String apiKey) {
    for (ApiEnums api : ApiEnums.values()) {
      if (api.getApiKey().equalsIgnoreCase(apiKey)) {
        return api;
      }
    }
    return null;
  }
}
