package com.video.CodeHelp.Service;

import com.video.CodeHelp.Enums.ApiEnums;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import jakarta.inject.Inject;

public class CodeHelpRoutingHandler implements Handler<RoutingContext> {

  private final Vertx vertx;
  private final EventBus eventBus;
  @Inject
  public CodeHelpRoutingHandler(Vertx vertx, EventBus eventBus){
    this.vertx = vertx;
    this.eventBus = eventBus;
  }

  @Override
  public void handle(RoutingContext routingContext) {
    vertx.executeBlocking(promise -> {
      eventBus.request(ApiEnums.WELCOME_API.getEventPath(), routingContext, messageAsyncResult -> {
        if (messageAsyncResult.succeeded()) {
          promise.complete(messageAsyncResult);
        } else {
          promise.fail(messageAsyncResult.cause());
        }
      });
    });
  }
}
