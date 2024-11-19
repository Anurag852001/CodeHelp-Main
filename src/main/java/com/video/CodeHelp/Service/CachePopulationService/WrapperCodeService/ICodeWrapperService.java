package com.video.CodeHelp.Service.CachePopulationService.WrapperCodeService;

import com.video.CodeHelp.Enums.CompilerTypeEnums;
import com.video.CodeHelp.Pojo.GetWrapperCodeRequest;
import com.video.CodeHelp.Service.CachePopulationService.WrapperCodeService.pojos.IWrapperCodeResponse;
import io.vertx.core.json.JsonObject;

import java.util.List;

public interface ICodeWrapperService {
   String wrapCode(String code, Long qid, CompilerTypeEnums compilerType);
   IWrapperCodeResponse getWrapperCode(GetWrapperCodeRequest request);
   Long saveWrapperCode(JsonObject request);
}
