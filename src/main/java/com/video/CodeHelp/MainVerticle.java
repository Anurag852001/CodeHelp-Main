package com.video.CodeHelp;


import com.google.inject.Guice;
import com.google.inject.Injector;
import com.video.CodeHelp.Guice.GuiceVerticleFactory;
import com.video.CodeHelp.Verticles.CodeHelpAdminVerticle;
import com.video.CodeHelp.Verticles.CodeHelpRouter;
import com.video.CodeHelp.modules.CodeHelpModule;
import io.vertx.core.*;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;


@Slf4j
public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) {
    try {
      log.info("Starting the main verticle");
      CodeHelpModule codeHelpModule = new CodeHelpModule(vertx);
      Injector injector = Guice.createInjector(codeHelpModule);
      GuiceVerticleFactory verticleFactory = new GuiceVerticleFactory(injector);
      vertx.registerVerticleFactory(verticleFactory);
      deployVerticles(startPromise, injector);
      log.info("Total threads:{}", Thread.activeCount());
      Runtime.getRuntime().addShutdownHook(new Thread(() -> destroyVertx()));
    } catch (Exception e) {
      log.error("Error occured while staring the main verticle ", e);
      startPromise.fail(e);
      vertx.close();
    }
  }

  public void deployVerticles(Promise<Void> startPromise, Injector injector) {
    try {
      log.info("Deploying verticles");
      DeploymentOptions codeHelpAdminDeploymentOptions = new DeploymentOptions().setWorker(true).setWorkerPoolSize(10);
      DeploymentOptions codeHelpRouterDeploymentOptions = new DeploymentOptions().setWorker(true).setWorkerPoolSize(10);
      CompletableFuture.runAsync(() -> {
        vertx.deployVerticle(injector.getInstance(CodeHelpAdminVerticle.class), codeHelpAdminDeploymentOptions);
        vertx.deployVerticle(injector.getInstance(CodeHelpRouter.class), codeHelpRouterDeploymentOptions);
      }).get();
      startPromise.complete();
    } catch (Exception e) {
      log.error("Error while starting verticles", e);
    }
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
