package com.video.CodeHelp.Service;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class WelcomeService {

  @Inject
  public WelcomeService(){
    log.info("Intializing the welcome service");
  }

  public void intoWelcomeService(){
    log.info("Inside the welcome service");
  }

}
