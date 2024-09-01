package com.video.CodeHelp.Enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApplicationErrorEnums {
  SOMETHING_WENT_WRONG("Something went wrong",1),
  NO_SUCH_LANGUAGE("No such language",2),
  ERROR_IN_POPULATING_CACHE("Could not populate the cache: ",4);

  String message;
  Integer errorCode;
}
