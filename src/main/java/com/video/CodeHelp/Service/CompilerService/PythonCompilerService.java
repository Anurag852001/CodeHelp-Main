package com.video.CodeHelp.Service.CompilerService;

import com.video.CodeHelp.Pojo.CodeCompilingRequest;
import com.video.CodeHelp.Pojo.Responses.SubmitCodeResponse;
import com.video.CodeHelp.Pojo.SubmitCodeRequest;

public class PythonCompilerService implements ICompilerService{
  @Override
  public String compileCode(CodeCompilingRequest request) {
    return "";
  }

  @Override
  public String runSimpleCode(String code) {
    return "";
  }

  @Override
  public SubmitCodeResponse submitCode(SubmitCodeRequest request) {
    return null;
  }
}
