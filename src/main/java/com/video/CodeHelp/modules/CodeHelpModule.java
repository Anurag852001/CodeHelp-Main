package com.video.CodeHelp.modules;


import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;

import com.video.CodeHelp.Service.WelcomeService;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Singleton
@Slf4j
public class CodeHelpModule extends AbstractModule {


  public CodeHelpModule() {
    log.info("Starting the codeHelp module");
  }

  @Singleton
  @Provides
  public WelcomeService providesWelcomeService(){
    return new WelcomeService();
  }

}
