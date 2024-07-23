package com.video.CodeHelp.Verticles;

import com.video.CodeHelp.Constants.DataConstants;
import com.video.CodeHelp.Enums.ApiEnums;
import com.video.CodeHelp.Pojo.SaveCodeHelpConfigRequest;
import com.video.CodeHelp.Service.CodeHelpConfigService;
import com.video.CodeHelp.Service.WelcomeService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Singleton
@Slf4j
public class CodeHelpAdminVerticle extends AbstractVerticle {

  private WelcomeService welcomeService;
  private CodeHelpConfigService configService;
  @Inject
  public CodeHelpAdminVerticle(WelcomeService welcomeService,CodeHelpConfigService configService) {
    log.info("Intializing the codeHelpAdminVerticle");
    this.welcomeService = welcomeService;
    this.configService = configService;
  }


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
          welcomeService.intoWelcomeService();
          future.complete(response);
          handler.reply(response);
        } catch (Exception e){
          log.error("Error while welcome api",e);
          JsonObject responseFail = new JsonObject();
          future.fail(e);
        }
      });
    });

    eventBus.consumer(ApiEnums.CONFIG_SAVE_API.getEventPath(),  message->{
      vertx.executeBlocking(future->{
        try {
          JsonObject body = new JsonObject(message.body().toString());
          SaveCodeHelpConfigRequest request = body.mapTo(SaveCodeHelpConfigRequest.class);
          JsonObject response = new JsonObject();
          Integer id = configService.saveCodeHelpConfig(request);
          response.put(DataConstants.SUCCESS,true);
          response.put(DataConstants.MESSAGE,"Config saved successfully");
          response.put(DataConstants.ID,id);
          message.reply(response);
          future.complete(response);
        } catch (Exception e){
          log.error("Error while saving config", e);
          future.fail(e.getMessage());
        }
      });
    });
  }
}
