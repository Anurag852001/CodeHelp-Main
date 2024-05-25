package com.video.CodeHelp;

import com.englishtown.vertx.guice.GuiceVerticleFactory;
import com.google.inject.Guice;
import com.google.inject.Injector;

import com.video.CodeHelp.Service.CodeHelpAdminVerticle;
import com.video.CodeHelp.Service.CodeHelpRoutingHandler;
import com.video.CodeHelp.Service.WelcomeService;
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
      CodeHelpModule codeHelpModule = new CodeHelpModule(vertx);
      Injector injector = Guice.createInjector(codeHelpModule);
      GuiceVerticleFactory verticleFactory = new GuiceVerticleFactory();
      deployVerticles(startPromise,verticleFactory);
      log.info("Total threads:{}",Thread.activeCount());

      Runtime.getRuntime().addShutdownHook(new Thread(() -> destroyVertx()));
    } catch (Exception e) {
      log.error("Error occured while staring the main verticle ",e);
      startPromise.fail(e);
      vertx.close();
    }
  }

  public void deployVerticles(Promise<Void> startPromise, GuiceVerticleFactory verticleFactory) {

try {

  Promise codeRoutingHandlerPromise = Promise.promise();
  Promise codeHelpAdminVerticlePromise = Promise.promise();
  Promise codeHelpRoutingRouter = Promise.promise();
  Verticle codeHelpAdminVerticle = verticleFactory.createVerticle(CodeHelpAdminVerticle.class.getName(), codeHelpAdminVerticlePromise.getClass().getClassLoader());
  Verticle codeHelpRoutingHandler = verticleFactory.createVerticle(CodeHelpRoutingHandler.class.getName(), codeRoutingHandlerPromise.getClass().getClassLoader());
  Verticle codeHelpRoutingRouterVerticle = verticleFactory.createVerticle(CodeHelpRoutingRouter.class.getName(), codeHelpRoutingRouter.getClass().getClassLoader());
  codeHelpAdminVerticlePromise.complete();
  vertx.deployVerticle(codeHelpAdminVerticle,new DeploymentOptions().setWorker(true).setWorkerPoolSize(50));
  vertx.deployVerticle(codeHelpRoutingHandler,new DeploymentOptions().setWorker(true).setWorkerPoolSize(50));
  vertx.deployVerticle(codeHelpRoutingRouterVerticle,new DeploymentOptions().setWorker(true).setWorkerPoolSize(50));
  codeRoutingHandlerPromise.complete();
  startPromise.complete();
}catch (Exception e){
 log.error("Exception occurred while trying to deploy verticles",e);
 throw new IllegalStateException("Exception occurred while trying to deploy verticles");
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
