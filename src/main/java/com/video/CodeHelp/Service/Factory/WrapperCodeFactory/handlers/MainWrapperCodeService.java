package com.video.CodeHelp.Service.Factory.WrapperCodeFactory.handlers;

import com.google.inject.Inject;
import com.video.CodeHelp.Enums.CompilerTypeEnums;
import com.video.CodeHelp.Pojo.GetWrapperCodeRequest;
import com.video.CodeHelp.Service.Factory.WrapperCodeFactory.ICodeWrapperService;
import com.video.CodeHelp.Service.Factory.WrapperCodeFactory.pojos.IWrapperCodeResponse;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;

import java.util.List;

public class MainWrapperCodeService implements ICodeWrapperService {

  EventBus eventBus;

  @Inject
  public MainWrapperCodeService(EventBus eventBus) {
    this.eventBus = eventBus;

  }

  @Override
  public String wrapCode(String code, Long qid, CompilerTypeEnums compilerType, List<String> inputs) {
    return null;
  }

  @Override
  public IWrapperCodeResponse getWrapperCode(GetWrapperCodeRequest request) {
    return null;
  }

  @Override
  public Long saveWrapperCode(JsonObject request) {
    return null;
  }
}
