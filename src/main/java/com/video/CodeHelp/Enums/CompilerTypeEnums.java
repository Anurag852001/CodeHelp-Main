package com.video.CodeHelp.Enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CompilerTypeEnums {
  CPP("c++"),JAVA("java"),PYTHON("python");

  public String language;

  public static CompilerTypeEnums getFromLanguage(String language) {
    for(CompilerTypeEnums e : CompilerTypeEnums.values()) {
      if(language.equalsIgnoreCase(e.language)){
        return e;
      }
    }
    return null;
  }
}
