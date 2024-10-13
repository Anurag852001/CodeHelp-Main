package com.video.CodeHelp.Exception;

import com.video.CodeHelp.Enums.ApplicationErrorEnums;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.eventbus.ReplyFailure;

public class CodeHelpException extends ReplyException {

  public CodeHelpException(ReplyFailure failureType, int failureCode, String message) {
    super(failureType, failureCode, message);
  }

  public CodeHelpException(ReplyFailure failureType, String message) {
    super(failureType, message);
  }

  public CodeHelpException(ReplyFailure failureType) {
    super(failureType);
  }
  public CodeHelpException(String message){
    super(ReplyFailure.ERROR, message);
  }

  public CodeHelpException(ApplicationErrorEnums applicationErrorEnum) {
    super(ReplyFailure.ERROR,applicationErrorEnum.getErrorCode(),applicationErrorEnum.getMessage());
  }
}
