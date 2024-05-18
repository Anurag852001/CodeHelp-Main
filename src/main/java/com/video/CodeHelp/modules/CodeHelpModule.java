package com.video.CodeHelp.modules;


import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;

import com.video.CodeHelp.Service.WelcomeService;
import jakarta.inject.Singleton;

public class CodeHelpModule implements Module {

  @Override
  public void configure(Binder binder) {

  }

  public CodeHelpModule() {

  }

  @Singleton
  @Provides
  public WelcomeService providesWelcomeService(){
    return new WelcomeService();
  }

}
