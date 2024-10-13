package com.video.CodeHelp.Service.CompilerService;

import com.video.CodeHelp.Pojo.CodeCompilingRequest;

public interface ICompilerService {
  public String compileCode(CodeCompilingRequest request);
  public String runSimpleCode(String code);
}
