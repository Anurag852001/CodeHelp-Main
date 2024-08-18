package com.video.CodeHelp.Verticles;

import com.video.CodeHelp.Constants.DataConstants;
import com.video.CodeHelp.Enums.ApiEnums;
import com.video.CodeHelp.Pojo.Config;
import com.video.CodeHelp.Pojo.SaveOrUpdateConfigRequest;
import com.video.CodeHelp.Service.ConfigService;
import com.video.CodeHelp.Service.WelcomeService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Singleton
@Slf4j
public class CodeHelpAdminVerticle extends AbstractVerticle {

  private WelcomeService welcomeService;
  private ConfigService configService;

  @Inject
  public CodeHelpAdminVerticle(WelcomeService welcomeService, ConfigService configService) {
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
          response.put(DataConstants.MESSAGE, "Welcome to code help!!");
          welcomeService.intoWelcomeService();
          future.complete(response);
          handler.reply(response);
        } catch (Exception e) {
          log.error("Error while welcome api", e);
          JsonObject responseFail = new JsonObject();
          future.fail(e);
        }
      });
    });

    eventBus.consumer(ApiEnums.CONFIG_SAVE_API.getEventPath(), message -> {
      vertx.executeBlocking(future -> {
        try {
          JsonObject body = new JsonObject(message.body().toString());
          SaveOrUpdateConfigRequest request = body.mapTo(SaveOrUpdateConfigRequest.class);
          JsonObject response = new JsonObject();
          Long id = configService.saveCodeHelpConfig(request);
          response.put(DataConstants.SUCCESS, true);
          response.put(DataConstants.MESSAGE, "Config saved successfully");
          response.put(DataConstants.ID, id);
          message.reply(response);
          future.complete(response);
        } catch (Exception e) {
          log.error("Error while saving config", e);
          message.reply(e);
          future.fail(e);
        }
      });
    });

    eventBus.consumer(ApiEnums.CONFIG_UPDATE_API.getEventPath(), message -> {
      vertx.executeBlocking(future -> {
        try {
          JsonObject body = new JsonObject(message.body().toString());
          SaveOrUpdateConfigRequest request = body.mapTo(SaveOrUpdateConfigRequest.class);
          JsonObject response = new JsonObject();
          Long id = configService.updateCodeHelpConfig(request);
          response.put(DataConstants.SUCCESS, true);
          response.put(DataConstants.MESSAGE, "Config updated successfully");
          response.put(DataConstants.ID, id);
          future.complete(response);
          message.reply(response);
        } catch (Exception e) {
          log.error("Error while saving config", e);
          message.reply(e);
          future.fail(e);
        }
      });
    });

    eventBus.consumer(ApiEnums.CONFIG_GET_API.getEventPath(), message -> {
        vertx.executeBlocking(future -> {
            try {
              JsonObject request = new JsonObject(message.body().toString());
              Config config = configService.getCodeHelpConfig(request.getString(DataConstants.CONFIG_KEY), request.getString(DataConstants.CONFIG_TYPE));
              JsonObject configJ = JsonObject.mapFrom(config);
              JsonObject response = new JsonObject().put(DataConstants.MESSAGE, DataConstants.SUCCESS);
              response.put(DataConstants.SUCCESS, true);
              response.put(DataConstants.DATA, configJ);
              message.reply(response);
              future.complete(response);
            } catch (Exception e) {
              log.error("Error while getting config", e);
              message.reply(e);
              future.fail(e);
            }
          }
        );
      }
    );

  }
}
