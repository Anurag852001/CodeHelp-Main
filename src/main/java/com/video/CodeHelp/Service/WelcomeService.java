package com.video.CodeHelp.Service;

import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WelcomeService {

  @Inject
  public WelcomeService(){
    intoWelcomeService();
  }

  public void intoWelcomeService(){
    log.info("Inside the welcome service");
  }

}
