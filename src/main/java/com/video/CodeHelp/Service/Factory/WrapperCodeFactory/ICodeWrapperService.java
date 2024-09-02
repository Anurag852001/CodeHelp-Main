package com.video.CodeHelp.Service.Factory.WrapperCodeFactory;

import com.video.CodeHelp.Service.Factory.WrapperCodeFactory.pojos.IWrapperCodeResponse;

import java.util.List;

public interface ICodeWrapperService {
   void wrapCode(String code, List<String> inputs);
   IWrapperCodeResponse getWrapperCode(Long qId);
   void saveWrapperCode(String code,Long qId);
}
