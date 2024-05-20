package com.video.CodeHelp.Service;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import jakarta.inject.Inject;

public class CodeHelpAdminService extends AbstractVerticle {
  EventBus eventBus;

  @Inject
  CodeHelpAdminService(EventBus eventBus){
    this.eventBus = eventBus;
  }

  @Override
  public void start(){
    vertx.executeBlocking(promise -> {
      eventBus.consumer()
    });
  }
}
