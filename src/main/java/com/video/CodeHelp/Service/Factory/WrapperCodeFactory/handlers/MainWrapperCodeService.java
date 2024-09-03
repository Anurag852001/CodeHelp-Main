package com.video.CodeHelp.Service.Factory.WrapperCodeFactory.handlers;

import com.google.inject.Inject;
import com.video.CodeHelp.Constants.DataConstants;
import com.video.CodeHelp.Dao.DefaultCodeDao;
import com.video.CodeHelp.Service.Factory.WrapperCodeFactory.ICodeWrapperService;
import com.video.CodeHelp.Service.Factory.WrapperCodeFactory.pojos.DefaultWrapperCodeSaveRequest;
import com.video.CodeHelp.Service.Factory.WrapperCodeFactory.pojos.ISaveWrapperCodeRequest;
import com.video.CodeHelp.Service.Factory.WrapperCodeFactory.pojos.IWrapperCodeResponse;
import com.video.CodeHelp.utils.CachingUtils;
import com.video.CodeHelp.utils.CommonUtils;
import io.vertx.core.eventbus.EventBus;

import java.util.List;

public class MainWrapperCodeService implements ICodeWrapperService {

  EventBus eventBus;

  @Inject
  public MainWrapperCodeService(EventBus eventBuso) {
    this.eventBus = eventBus;

  }

  @Override
  public void wrapCode(String code, List<String> inputs) {

  }

  @Override
  public IWrapperCodeResponse getWrapperCode(Long qId) {
    return null;
  }

  @Override
  public void saveWrapperCode(ISaveWrapperCodeRequest request) {
  }
}
