package com.video.CodeHelp.Verticles;

import com.video.CodeHelp.Enums.ApiEnums;
import com.video.CodeHelp.Service.CodeHelpRoutingHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class CodeHelpRoutingRouter extends AbstractVerticle {

  Router router;
  CodeHelpRoutingHandler codeHelpRoutingHandler;

  @Inject
  CodeHelpRoutingRouter(CodeHelpRoutingHandler codeHelpRoutingHandler){
    this.codeHelpRoutingHandler = codeHelpRoutingHandler;
  }

  @Override
  public void start(){
    router = Router.router(vertx);
    router.post().path(ApiEnums.WELCOME_API.getApiKey()).handler(codeHelpRoutingHandler);
  }
}
