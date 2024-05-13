package com.video.CodeHelp;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.video.CodeHelp.Config.CodeHelpConfig;
import com.video.CodeHelp.Verticles.CodeHelpRoutingHandler;
import com.video.CodeHelp.modules.CodeHelpModule;
import io.vertx.core.*;
import io.vertx.core.DeploymentOptions;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Supplier;

@Slf4j
public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    try {
      deployVerticles(startPromise);
      Runtime.getRuntime().addShutdownHook(new Thread(() -> destroyVertx()));
    } catch (Exception e) {
      vertx.close();
      startPromise.complete();
    }
  }

  public void deployVerticles(Promise<Void> startPromise) {
    CodeHelpConfig cofig = new CodeHelpConfig();
    CodeHelpModule codeHelpModule = new CodeHelpModule(cofig);
    Injector injector = Guice.createInjector(codeHelpModule);
    Promise codeRoutingHandlerPromise = Promise.promise();
    DeploymentOptions codeRoutingHandlerDeploymentOptions = new DeploymentOptions();
    vertx.deployVerticle(CodeHelpRoutingHandler.class, codeRoutingHandlerDeploymentOptions);
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
