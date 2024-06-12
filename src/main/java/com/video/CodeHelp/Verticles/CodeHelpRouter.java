package com.video.CodeHelp.Verticles;

import com.video.CodeHelp.Enums.ApiEnums;
import com.video.CodeHelp.Handler.CodeHelpRoutingHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class CodeHelpRouter extends AbstractVerticle {

  private final Router router;
  private final CodeHelpRoutingHandler codeHelpRoutingHandler;

  @Inject
  public CodeHelpRouter(CodeHelpRoutingHandler codeHelpRoutingHandler) {
    this.router = Router.router(vertx);
    this.codeHelpRoutingHandler = codeHelpRoutingHandler;
  }


  @Override
  public void start() {
    try {
      HttpServer server = vertx.createHttpServer();
      log.info("Starting the Code help router");
      router.get(ApiEnums.WELCOME_API.getApiKey())
        .handler(codeHelpRoutingHandler);

      router.post(ApiEnums.CONFIG_SAVE_API.getApiKey())
          .handler(codeHelpRoutingHandler);

      server.requestHandler(router).listen(8000);
    } catch (Exception e) {
      log.error("error occured while starting the server", e);
      throw e;
    }
  }

}
