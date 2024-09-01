package com.video.CodeHelp.Service.CachePopulationService;

import com.video.CodeHelp.Constants.DataConstants;
import com.video.CodeHelp.Service.CachePopulationService.enums.CachePopulationTypes;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public abstract class ICachePopulationService {
  protected abstract void populateCache(List<Long> qIds);

  public String getCacheKey(Long id, CachePopulationTypes type){
    return StringUtils.join(DataConstants.UNDERSCORE,id,type.name());
  }
}
