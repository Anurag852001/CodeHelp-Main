package com.video.CodeHelp;

import com.google.inject.Guice;
import com.google.inject.Injector;

import com.video.CodeHelp.Verticles.CodeHelpRoutingRouter;
import com.video.CodeHelp.modules.CodeHelpModule;
import io.vertx.core.*;
import io.vertx.core.DeploymentOptions;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;


@Slf4j
public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise)  {
    try {
      log.info("Starting the main verticle");
      deployVerticles(startPromise);
      Runtime.getRuntime().addShutdownHook(new Thread(() -> destroyVertx()));
    } catch (Exception e) {
      log.error("Error occured while staring the ");
      vertx.close();
      startPromise.complete();
    }
  }

  public void deployVerticles(Promise<Void> startPromise)  {
    CodeHelpModule codeHelpModule = new CodeHelpModule();
    Injector injector = Guice.createInjector(codeHelpModule);
    Promise codeRoutingHandlerPromise = Promise.promise();
    DeploymentOptions codeRoutingHandlerDeploymentOptions = new DeploymentOptions();
    CompletableFuture.runAsync(()->{
      vertx.deployVerticle(CodeHelpRoutingRouter.class.getName(),codeRoutingHandlerDeploymentOptions);
      codeRoutingHandlerPromise.complete();
    }).join();


    startPromise.complete();
  }

  public void destroyVertx() {
    //here we can destroy vertx gracefully
    if (vertx == null) {
      throw new IllegalArgumentException("Vertx was not initialized properly");
    } else {
      CompletableFuture closeVertx = new CompletableFuture<>();
      Handler<AsyncResult<Void>> handler = voidAsyncResult -> {
        if (voidAsyncResult.succeeded()) {
          closeVertx.complete(null);
        } else {
          closeVertx.completeAsync((Supplier<Void>) voidAsyncResult.cause());
        }
      };
    }
  }
}
