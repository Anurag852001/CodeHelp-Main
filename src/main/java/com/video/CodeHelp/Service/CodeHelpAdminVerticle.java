package com.video.CodeHelp.Service;

import com.video.CodeHelp.Constants.DataConstants;
import com.video.CodeHelp.Enums.ApiEnums;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CodeHelpAdminVerticle extends AbstractVerticle {

  @Override
  public void start() {
    EventBus eventBus = vertx.eventBus();
    log.info("Starting the admin verticle");
    eventBus.consumer(ApiEnums.WELCOME_API.getEventPath(), handler -> {
      vertx.executeBlocking(future -> {
        try {
          JsonObject response = new JsonObject();
          response.put(DataConstants.SUCCESS, true);
          response.put(DataConstants.MESSAGE, "Welcome to code help");
          future.complete(response);
          handler.reply(response);
        } catch (Exception e){
          log.error("Error while welcome api",e);
          JsonObject responseFail = new JsonObject();
          future.fail(e);
        }
      });
    });
  }
}
