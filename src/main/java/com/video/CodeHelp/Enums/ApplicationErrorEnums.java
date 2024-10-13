package com.video.CodeHelp.Enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApplicationErrorEnums {
  SOMETHING_WENT_WRONG("Something went wrong",1),
  NO_SUCH_LANGUAGE("No such language",2),
  ERROR_IN_POPULATING_CACHE("Could not populate the cache",4),
  NOT_SUPPORTED("Not supported",5),
  CODE_COMPILING_ERROR("Error in compiling code ",5),
  ERROR_WHILE_SAVING_CORRECT_CODE("Error while saving correct code: ",6),
  ERROR_WHILE_UPDATING_CORRECT_CODE("Error while updating correct code ",7);

  String message;
  Integer errorCode;
}
