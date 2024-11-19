package com.video.CodeHelp.Service.CompilerService;

import com.video.CodeHelp.Enums.CompilerTypeEnums;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class CompilerFactory {
  ICompilerService cppCompilerService;
  ICompilerService javaCompilerService;
  ICompilerService pythonCompilerService;

  @Inject
  public CompilerFactory(ICompilerService cppCompilerService,ICompilerService javaCompilerService, ICompilerService pythonCompilerService) {
    this.cppCompilerService = cppCompilerService;
    this.javaCompilerService = javaCompilerService;
    this.pythonCompilerService = pythonCompilerService;
  }


  public ICompilerService getCompiler(CompilerTypeEnums compilerType) {
    switch (compilerType) {
      case CPP:
        return cppCompilerService;
      case JAVA:
        return javaCompilerService;
      case PYTHON:
        return pythonCompilerService;
      default:
        log.info("Unsupported language");
        throw new IllegalArgumentException("Unsupported language");
    }
  }
}
