package com.video.CodeHelp;


import com.google.inject.Guice;
import com.google.inject.Injector;
import com.video.CodeHelp.Config.CodeHelpConfig;
import com.video.CodeHelp.Exception.CodeHelpException;
import com.video.CodeHelp.Guice.GuiceVerticleFactory;
import com.video.CodeHelp.Verticles.CodeHelpAdminVerticle;
import com.video.CodeHelp.Verticles.CodeHelpCompilerVerticle;
import com.video.CodeHelp.Verticles.CodeHelpRouter;
import com.video.CodeHelp.modules.CodeHelpModule;
import io.vertx.config.ConfigRetriever;
import io.vertx.core.*;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.eventbus.ReplyFailure;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;


@Slf4j
public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) {
    try {
      log.info("Starting the main verticle");
      ConfigRetriever configRetriever = ConfigRetriever.create(vertx);
      CodeHelpConfig config = getConfig(configRetriever);
      log.info("The full config is :{}", JsonObject.mapFrom(config));
      CodeHelpModule codeHelpModule = new CodeHelpModule(vertx,config);
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
      DeploymentOptions codeHelpAdminDeploymentOptions = new DeploymentOptions().setWorker(true).setWorkerPoolSize(50);
      DeploymentOptions codeHelpRouterDeploymentOptions = new DeploymentOptions().setWorker(true).setWorkerPoolSize(50);
      DeploymentOptions codeHelpCompilerDeploymentOptions = new DeploymentOptions().setWorker(false);
      CompletableFuture.runAsync(() -> {
        vertx.deployVerticle(injector.getInstance(CodeHelpAdminVerticle.class), codeHelpAdminDeploymentOptions);
        vertx.deployVerticle(injector.getInstance(CodeHelpRouter.class), codeHelpRouterDeploymentOptions);
        vertx.deployVerticle(injector.getInstance(CodeHelpCompilerVerticle.class),codeHelpCompilerDeploymentOptions);
      }).get();
      startPromise.complete();
    } catch (Exception e) {
      log.error("Error while starting verticles", e);
    }
  }

  public CodeHelpConfig getConfig(ConfigRetriever configRetriever){
    AtomicReference<CodeHelpConfig> config = new AtomicReference<>();
    configRetriever.getConfig(handler->{
      if(handler.succeeded()){
        config.set(handler.result().mapTo(CodeHelpConfig.class));
      } else{
        throw new CodeHelpException(ReplyFailure.ERROR,"Failed to retrieve Config");
      }
    });
    return config.get();
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
