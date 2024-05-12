package com.video.CodeHelp;

import com.google.inject.Inject;
import com.video.CodeHelp.Verticles.CodeHelpRoutingHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;

import java.util.concurrent.CompletableFuture;


public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    deployVerticles();
  }

  public void deployVerticles(){
    Promise codeRoutingHandlerPromise = Promise.promise();
    DeploymentOptions codeRoutingHandlerDeploymentOptions = new DeploymentOptions();
    vertx.deployVerticle(CodeHelpRoutingHandler.class,codeRoutingHandlerDeploymentOptions);
  }
}
