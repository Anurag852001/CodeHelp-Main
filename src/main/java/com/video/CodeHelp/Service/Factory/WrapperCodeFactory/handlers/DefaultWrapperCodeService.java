package com.video.CodeHelp.Service.Factory.WrapperCodeFactory.handlers;

import com.video.CodeHelp.Constants.DataConstants;
import com.video.CodeHelp.Dao.DefaultCodeDao;
import com.video.CodeHelp.Service.Factory.WrapperCodeFactory.ICodeWrapperService;
import com.video.CodeHelp.Service.Factory.WrapperCodeFactory.pojos.DefaultWrapperCodeSaveRequest;
import com.video.CodeHelp.Service.Factory.WrapperCodeFactory.pojos.ISaveWrapperCodeRequest;
import com.video.CodeHelp.Service.Factory.WrapperCodeFactory.pojos.IWrapperCodeResponse;
import com.video.CodeHelp.utils.CachingUtils;
import io.vertx.core.eventbus.EventBus;
import jakarta.inject.Inject;

import java.util.List;

public class DefaultWrapperCodeService implements ICodeWrapperService {

  private final EventBus eventBus;
  private final DefaultCodeDao defaultCodeDao;

  @Inject
  public DefaultWrapperCodeService(EventBus eventBus, DefaultCodeDao defaultCodeDao) {
    this.eventBus = eventBus;
    this.defaultCodeDao = defaultCodeDao;
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
    DefaultWrapperCodeSaveRequest request1 = (DefaultWrapperCodeSaveRequest) request;

    String cacheKey = CachingUtils.getCacheKeyForDefaultWrapperCode(request1.getQNo(),request1.getCompilerType());
    eventBus.send(DataConstants.SYNC_IN_CACHE, CachingUtils.getTwoHundredCacheSyncRequest(cacheKey,request1.getDefaultCode()));

  }

}
