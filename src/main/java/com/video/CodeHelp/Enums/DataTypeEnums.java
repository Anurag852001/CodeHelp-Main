package com.video.CodeHelp.Enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DataTypeEnums {
  STRING("PRINT_DEFAULT_CONFIG_JAVA"), INTEGER("PRINT_DEFAULT_CONFIG"), FLOAT("PRINT_DEFAULT_CONFIG_JAVA"), BOOLEAN("PRINT_DEFAULT_CONFIG_JAVA"), INTEGER_ARRAY("PRINT_INTEGER_ARRAY_CONFIG_JAVA");
  String javaPrintingConfig;
}
