package com.video.CodeHelp.Service.Factory.WrapperCodeFactory;

import com.video.CodeHelp.Enums.ApplicationErrorEnums;
import com.video.CodeHelp.Exception.CodeHelpException;
import com.video.CodeHelp.Service.ConfigService;
import com.video.CodeHelp.Service.Factory.WrapperCodeFactory.enums.WrapperCodeEnums;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class WrapperFactory {

  private final ICodeWrapperService defaultCodeWrapperService;
  private final ICodeWrapperService mainCodeWrapperService;

  public WrapperFactory(ICodeWrapperService defaultCodeWrapperService, ICodeWrapperService mainCodeWrapperService) {
    this.defaultCodeWrapperService = defaultCodeWrapperService;
    this.mainCodeWrapperService = mainCodeWrapperService;
  }

  public ICodeWrapperService getWrapperService(WrapperCodeEnums wrapperCodeEnum) {
    switch (wrapperCodeEnum) {
      case DEFAULT_CODE -> {
        return defaultCodeWrapperService;
      }
      case MAIN_CODE -> {
        return mainCodeWrapperService;
      }
      default -> {
        log.error("Not supported this wrapper code service");
        throw new CodeHelpException(ApplicationErrorEnums.NOT_SUPPORTED);
      }
    }
  }

}
