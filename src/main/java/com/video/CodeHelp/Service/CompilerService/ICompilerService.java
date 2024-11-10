package com.video.CodeHelp.Service.CompilerService;

import com.video.CodeHelp.Pojo.CodeCompilingRequest;
import com.video.CodeHelp.Pojo.Responses.SubmitCodeResponse;
import com.video.CodeHelp.Pojo.SubmitCodeRequest;

public interface ICompilerService {
  public String compileCode(CodeCompilingRequest request);
  public String runSimpleCode(String code);
  public SubmitCodeResponse submitCode(SubmitCodeRequest request);
}
