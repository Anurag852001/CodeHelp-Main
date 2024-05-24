package com.video.CodeHelp.Enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ApiEnums {
  WELCOME_API("/welcome/api","welcomeApiHandler");

  String apiKey;
  String eventPath;
}
