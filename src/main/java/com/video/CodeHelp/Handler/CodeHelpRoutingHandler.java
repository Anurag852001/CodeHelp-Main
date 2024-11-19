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
    if(body == null) body = new JsonObject();
    attachHeadersAndParams(routingContext,body);
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
      case QUESTION_GET_API:s:
        eventBus.request(QUESTION_GET_API.getEventPath(), body, messageAsyncResult -> {
          if (messageAsyncResult.succeeded()) {
            handleSuccessResponse(routingContext, messageAsyncResult);
            promise.complete(messageAsyncResult);
          } else {
            promise.fail(messageAsyncResult.cause());
          }
        });
        break;

      case QUESTION_SAVE_API:
      eventBus.request(QUESTION_SAVE_API.getEventPath(), body, messageAsyncResult -> {
        if (messageAsyncResult.succeeded()) {
          handleSuccessResponse(routingContext, messageAsyncResult);
          promise.complete(messageAsyncResult);
        } else {
          promise.fail(messageAsyncResult.cause());
        }
      });
        break;
      case COMPILE_CODE_API:
      eventBus.request(COMPILE_CODE_API.getEventPath(), body, messageAsyncResult -> {
        if (messageAsyncResult.succeeded()) {
          handleSuccessResponse(routingContext, messageAsyncResult);
          promise.complete(messageAsyncResult);
        } else {
          promise.fail(messageAsyncResult.cause());
        }
      });
        break;
      case SUBMIT_CODE_API:
        eventBus.request(SUBMIT_CODE_API.getEventPath(), body, messageAsyncResult -> {
          if (messageAsyncResult.succeeded()) {
            handleSuccessResponse(routingContext, messageAsyncResult);
            promise.complete(messageAsyncResult);
          } else {
            promise.fail(messageAsyncResult.cause());
          }
        });
        break;
      case GET_WRAPPER_CODE:
        eventBus.request(GET_WRAPPER_CODE.getEventPath(), body, messageAsyncResult -> {
          if (messageAsyncResult.succeeded()) {
            handleSuccessResponse(routingContext, messageAsyncResult);
            promise.complete(messageAsyncResult);
          } else {
            promise.fail(messageAsyncResult.cause());
          }
        });
        break;
      case SAVE_WRAPPER_CODE:
        eventBus.request(SAVE_WRAPPER_CODE.getEventPath(), body, messageAsyncResult -> {
          if (messageAsyncResult.succeeded()) {
            handleSuccessResponse(routingContext, messageAsyncResult);
            promise.complete(messageAsyncResult);
          } else {
            promise.fail(messageAsyncResult.cause());
          }
        });
        break;

      case GENERIC_LIST_API:
        eventBus.request(GENERIC_LIST_API.getEventPath(), body, messageAsyncResult -> {
          if (messageAsyncResult.succeeded()) {
            handleSuccessResponse(routingContext, messageAsyncResult);
            promise.complete(messageAsyncResult);
          } else {
            promise.fail(messageAsyncResult.cause());
          }
        });
        break;

      case SAVE_MAIN_CODE_VARIABLES_API:
        eventBus.request(SAVE_MAIN_CODE_VARIABLES_API.getEventPath(), body, messageAsyncResult -> {
          if (messageAsyncResult.succeeded()) {
            handleSuccessResponse(routingContext, messageAsyncResult);
            promise.complete(messageAsyncResult);
          } else {
            promise.fail(messageAsyncResult.cause());
          }
        });
        break;

      case GET_MAIN_CODE_VARIABLES_API:
        eventBus.request(GET_MAIN_CODE_VARIABLES_API.getEventPath(), body, messageAsyncResult -> {
          if (messageAsyncResult.succeeded()) {
            handleSuccessResponse(routingContext, messageAsyncResult);
            promise.complete(messageAsyncResult);
          } else {
            promise.fail(messageAsyncResult.cause());
          }
        });
        break;

      case SAVE_CORRECT_CODE_API:
        eventBus.request(SAVE_CORRECT_CODE_API.getEventPath(), body, messageAsyncResult -> {
          if (messageAsyncResult.succeeded()) {
            handleSuccessResponse(routingContext, messageAsyncResult);
            promise.complete(messageAsyncResult);
          } else {
            promise.fail(messageAsyncResult.cause());
          }
        });
        break;

      case GET_CORRECT_CODE_API:
        eventBus.request(GET_CORRECT_CODE_API.getEventPath(), body, messageAsyncResult -> {
          if (messageAsyncResult.succeeded()) {
            handleSuccessResponse(routingContext, messageAsyncResult);
            promise.complete(messageAsyncResult);
          } else {
            promise.fail(messageAsyncResult.cause());
          }
        });
        break;

      case UPDATE_CORRECT_CODE_API:
        eventBus.request(UPDATE_CORRECT_CODE_API.getEventPath(), body, messageAsyncResult -> {
          if (messageAsyncResult.succeeded()) {
            handleSuccessResponse(routingContext, messageAsyncResult);
            promise.complete(messageAsyncResult);
          } else {
            promise.fail(messageAsyncResult.cause());
          }
        });
        break;

      case TESTCASE_SAVE_API:
        eventBus.request(TESTCASE_SAVE_API.getEventPath(), body, messageAsyncResult -> {
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

  private void attachHeadersAndParams(RoutingContext routingContext,JsonObject jsonObject) {
    if(routingContext.request().params()!=null && !routingContext.request().params().isEmpty()) {
      routingContext.request().params().forEach(param -> {
        jsonObject.put(param.getKey(), param.getValue());
      });
    }
  }
}
