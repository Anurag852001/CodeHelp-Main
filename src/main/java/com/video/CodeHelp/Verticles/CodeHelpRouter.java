package com.video.CodeHelp.Verticles;

import com.video.CodeHelp.Config.CodeHelpConfig;
import com.video.CodeHelp.Constants.DataConstants;
import com.video.CodeHelp.Enums.ApiEnums;
import com.video.CodeHelp.Handler.CodeHelpRoutingHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class CodeHelpRouter extends AbstractVerticle {

  private final Router router;
  private final CodeHelpRoutingHandler codeHelpRoutingHandler;
  private final CodeHelpConfig codeHelpConfig;

  @Inject
  public CodeHelpRouter(CodeHelpRoutingHandler codeHelpRoutingHandler,CodeHelpConfig codeHelpConfig) {
    this.router = Router.router(vertx);
    this.codeHelpRoutingHandler = codeHelpRoutingHandler;
    this.codeHelpConfig = codeHelpConfig;
  }


  @Override
  public void start() {
    try {
      HttpServer server = vertx.createHttpServer();
      log.info("Starting the Code help router");
      router.get(ApiEnums.WELCOME_API.getApiKey())
        .handler(codeHelpRoutingHandler);

      router.get(ApiEnums.CACHE_GET_API.getApiKey())
        .handler(codeHelpRoutingHandler);

      router.get(ApiEnums.GET_DEFAULT_CODE.getApiKey())
        .handler(codeHelpRoutingHandler);

      router.get(ApiEnums.QUESTION_GET_API.getApiKey())
        .handler(codeHelpRoutingHandler);

      router.post(ApiEnums.CONFIG_SAVE_API.getApiKey())
        .produces(DataConstants.APPLICATION_JSON)
        .consumes(DataConstants.APPLICATION_JSON)
        .handler(BodyHandler.create())
        .handler(codeHelpRoutingHandler);


      router.post(ApiEnums.QUESTION_SAVE_API.getApiKey())
        .produces(DataConstants.APPLICATION_JSON)
        .consumes(DataConstants.APPLICATION_JSON)
        .handler(BodyHandler.create())
        .handler(codeHelpRoutingHandler);


      router.post(ApiEnums.CONFIG_UPDATE_API.getApiKey())
        .produces(DataConstants.APPLICATION_JSON)
        .consumes(DataConstants.APPLICATION_JSON)
        .handler(BodyHandler.create())
        .handler(codeHelpRoutingHandler);


      router.post( ApiEnums.CONFIG_GET_API.getApiKey())
        .produces(DataConstants.APPLICATION_JSON)
        .consumes(DataConstants.APPLICATION_JSON)
        .handler(BodyHandler.create())
        .handler(codeHelpRoutingHandler);

      router.post( ApiEnums.COMPILE_CODE_API.getApiKey())
        .produces(DataConstants.APPLICATION_JSON)
        .consumes(DataConstants.APPLICATION_JSON)
        .handler(BodyHandler.create())
        .handler(codeHelpRoutingHandler);


      server.requestHandler(router).listen(codeHelpConfig.getPort().intValue());
    } catch (Exception e) {
      log.error("error occured while starting the server", e);
      throw e;
    }
  }

}
