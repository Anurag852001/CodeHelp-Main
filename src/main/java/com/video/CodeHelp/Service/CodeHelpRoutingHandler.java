package com.video.CodeHelp.Service;

import com.video.CodeHelp.Enums.ApiEnums;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.ext.web.RoutingContext;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Singleton
public class CodeHelpRoutingHandler implements Handler<RoutingContext> {

  private final Vertx vertx;
  private final EventBus eventBus;


  @Inject
  public CodeHelpRoutingHandler(Vertx vertx, EventBus eventBus) {
    this.vertx = vertx;
    this.eventBus = eventBus;
  }

  @Override
  public void handle(RoutingContext routingContext) {
    Promise promise = Promise.promise();
    log.info("Recieved request for api :{}", routingContext.currentRoute().getPath());
    if (routingContext.currentRoute().getPath().equalsIgnoreCase(ApiEnums.WELCOME_API.getApiKey())) {
      eventBus.request(ApiEnums.WELCOME_API.getEventPath(), routingContext.getBody(), messageAsyncResult -> {
        if (messageAsyncResult.succeeded()) {
          promise.complete(messageAsyncResult);
          handleSuccessResponse(routingContext, messageAsyncResult);
        } else {
          promise.fail(messageAsyncResult.cause());
        }
      });
    }
  }

  private void handleSuccessResponse(RoutingContext routingContext, AsyncResult<io.vertx.core.eventbus.Message<Object>> res) {
    routingContext.response().setStatusCode(200).end(res.result().body().toString());
  }
}
