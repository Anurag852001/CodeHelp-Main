package com.video.CodeHelp.Verticles;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.video.CodeHelp.Constants.DataConstants;
import com.video.CodeHelp.Enums.ApiEnums;
import com.video.CodeHelp.Pojo.CodeCompilingRequest;
import com.video.CodeHelp.Service.Factory.CompilerFactory.CompilerFactory;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import jakarta.inject.Inject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CodeHelpCompilerVerticle extends AbstractVerticle {

  EventBus eventBus;
  Vertx vertx;
  CompilerFactory compilerFactory;

  @Inject
  public CodeHelpCompilerVerticle(EventBus eventBus, Vertx vertx,CompilerFactory compilerFactory) {
    this.eventBus = eventBus;
    this.vertx = vertx;
    this.compilerFactory = compilerFactory;
  }

  @Override
  public void start() {
    log.info("Deploying CodeHelpCompilerVerticle");

    eventBus.consumer(ApiEnums.COMPILE_CODE_API.getEventPath(),message->{
      vertx.executeBlocking(future->{
        CodeCompilingRequest request = new ObjectMapper().convertValue(message.body(), CodeCompilingRequest.class);
        log.info("Recevied request for code compiling:{}",request);
         String result =  compilerFactory.getCompiler(request.getCompilerType()).compileCode(request.getCode());
        JsonObject response = new JsonObject();
        response.put(DataConstants.SUCCESS,true);
        response.put(DataConstants.MESSAGE,DataConstants.SUCCESS);
        response.put(DataConstants.DATA, result);
        future.complete(response);
        message.reply(response);
      });
      }
    );

    log.info("CodeHelpCompilerVerticle Deployed Successfully");
  }
}
