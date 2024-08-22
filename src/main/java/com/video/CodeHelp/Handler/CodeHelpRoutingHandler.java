package com.video.CodeHelp.Handler;

import com.video.CodeHelp.Enums.ApiEnums;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import static com.video.CodeHelp.Enums.ApiEnums.*;


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
    ApiEnums api = ApiEnums.fromValue(routingContext.currentRoute().getPath());
    JsonObject body = routingContext.getBodyAsJson();
    switch (api) {
      case WELCOME_API:
        eventBus.request(WELCOME_API.getEventPath(), body, messageAsyncResult -> {
          if (messageAsyncResult.succeeded()) {
            handleSuccessResponse(routingContext, messageAsyncResult);
            promise.complete(messageAsyncResult);
          } else {
            promise.fail(messageAsyncResult.cause());
          }
        });
        break;
      case CONFIG_SAVE_API:
        eventBus.request(CONFIG_SAVE_API.getEventPath(), body, messageAsyncResult -> {
          if (messageAsyncResult.succeeded()) {
            handleSuccessResponse(routingContext, messageAsyncResult);
            promise.complete(messageAsyncResult);
          } else {
            promise.fail(messageAsyncResult.cause());
          }
        });
        break;
      case CONFIG_UPDATE_API:
        eventBus.request(CONFIG_UPDATE_API.getEventPath(), body, messageAsyncResult -> {
          if (messageAsyncResult.succeeded()) {
            handleSuccessResponse(routingContext, messageAsyncResult);
            promise.complete(messageAsyncResult);
          } else {
            promise.fail(messageAsyncResult.cause());
          }
        });
        break;
      case CONFIG_GET_API:
        eventBus.request(CONFIG_GET_API.getEventPath(), body, messageAsyncResult -> {
          if (messageAsyncResult.succeeded()) {
            handleSuccessResponse(routingContext, messageAsyncResult);
            promise.complete(messageAsyncResult);
          } else {
            promise.fail(messageAsyncResult.cause());
          }
        });
        break;
      case CACHE_GET_API:
        eventBus.request(CACHE_GET_API.getEventPath(), body, messageAsyncResult -> {
          if (messageAsyncResult.succeeded()) {
            handleSuccessResponse(routingContext, messageAsyncResult);
            promise.complete(messageAsyncResult);
          } else {
            promise.fail(messageAsyncResult.cause());
          }
        });
        break;
    }
  }

  private void handleSuccessResponse(RoutingContext routingContext, AsyncResult<io.vertx.core.eventbus.Message<Object>> res) {
    routingContext.response().setStatusCode(200).end(res.result().body().toString());
  }
}
