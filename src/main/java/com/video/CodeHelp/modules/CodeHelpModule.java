package com.video.CodeHelp.modules;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.video.CodeHelp.Config.CodeHelpConfig;
import com.video.CodeHelp.Service.WelcomeService;
import jakarta.inject.Singleton;

public class CodeHelpModule extends Module {

  private CodeHelpConfig config;

  @Override
  public void configure(Binder binder) {

  }

  public CodeHelpModule(CodeHelpConfig config) {
    this.config = config;
  }

  @Singleton
  @Provides
  public WelcomeService providesWelcomeService(){
    return new WelcomeService();
  }


}
