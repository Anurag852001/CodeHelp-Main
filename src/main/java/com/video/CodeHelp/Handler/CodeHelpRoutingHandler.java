package com.video.CodeHelp.Handler;

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

import static com.video.CodeHelp.Enums.ApiEnums.CONFIG_SAVE_API;
import static com.video.CodeHelp.Enums.ApiEnums.WELCOME_API;


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
    switch (api) {
      case WELCOME_API:
        eventBus.request(WELCOME_API.getEventPath(), routingContext.getBody(), messageAsyncResult -> {
          if (messageAsyncResult.succeeded()) {
            promise.complete(messageAsyncResult);
            handleSuccessResponse(routingContext, messageAsyncResult);
          } else {
            promise.fail(messageAsyncResult.cause());
          }
        });
      case CONFIG_SAVE_API:
        eventBus.request(CONFIG_SAVE_API.getEventPath(), routingContext.getBody(), messageAsyncResult -> {
          if (messageAsyncResult.succeeded()) {
            promise.complete(messageAsyncResult);
            handleSuccessResponse(routingContext, messageAsyncResult);
          } else {
            promise.fail(messageAsyncResult.cause());
          }
        });
        break;
      default:
        break;
    }
  }

  private void handleSuccessResponse(RoutingContext routingContext, AsyncResult<io.vertx.core.eventbus.Message<Object>> res) {
    routingContext.response().setStatusCode(200).end(res.result().body().toString());
  }
}
