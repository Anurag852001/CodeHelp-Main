package com.video.CodeHelp.Verticles;

import com.google.inject.Inject;
import com.video.CodeHelp.Enums.ApiEnums;
import com.video.CodeHelp.Service.CodeHelpRoutingHandler;
import com.video.CodeHelp.Service.WelcomeService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class CodeHelpRoutingRouter extends AbstractVerticle {

  Router router;
  CodeHelpRoutingHandler codeHelpRoutingHandler;

  @Override
  public void start() {
    try {
      HttpServer server = vertx.createHttpServer();
      this.codeHelpRoutingHandler = new CodeHelpRoutingHandler(vertx, vertx.eventBus());

      log.info("Starting the Code help router");
      router = Router.router(vertx);
      router.get(ApiEnums.WELCOME_API.getApiKey())
        .handler(codeHelpRoutingHandler);


      server.requestHandler(router).listen(8000);
    } catch (Exception e) {
      log.error("error occured while starting the server", e);
      throw e;
    }
  }

}
