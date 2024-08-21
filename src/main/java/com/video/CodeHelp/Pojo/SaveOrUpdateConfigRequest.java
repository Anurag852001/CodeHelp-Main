package com.video.CodeHelp.Pojo;

import com.video.CodeHelp.Enums.ConfigTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaveOrUpdateConfigRequest {
  Long id;
  ConfigTypeEnum configType;
  String configKey;
  String configValue;
  String status;
}
