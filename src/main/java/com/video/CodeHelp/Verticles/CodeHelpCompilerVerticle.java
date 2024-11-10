package com.video.CodeHelp.Verticles;

import com.video.CodeHelp.Constants.DataConstants;
import com.video.CodeHelp.Enums.ApiEnums;
import com.video.CodeHelp.Pojo.CodeCompilingRequest;
import com.video.CodeHelp.Pojo.Responses.SubmitCodeResponse;
import com.video.CodeHelp.Pojo.SubmitCodeRequest;
import com.video.CodeHelp.Service.CompilerService.CompilerFactory;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import jakarta.inject.Inject;
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
        try {
          CodeCompilingRequest request = new JsonObject(message.body().toString()).mapTo(CodeCompilingRequest.class);
          log.info("Recevied request for code compiling:{}", request);
          String result = compilerFactory.getCompiler(request.getCompilerType()).compileCode(request);
          JsonObject response = new JsonObject();
          response.put(DataConstants.SUCCESS, true);
          response.put(DataConstants.MESSAGE, DataConstants.SUCCESS);
          response.put(DataConstants.DATA, result);
          future.complete(response);
          message.reply(response);
        } catch (Exception e){
          log.error("Error while compiling code", e);
          JsonObject response = new JsonObject().put(DataConstants.SUCCESS,false).put(DataConstants.DATA,e.getMessage());
          message.reply(response);
          future.complete(response);
        }
      });
      }
    );


    eventBus.consumer(ApiEnums.SUBMIT_CODE_API.getEventPath(),message->{
        vertx.executeBlocking(future->{
          try {
            SubmitCodeRequest request = new JsonObject(message.body().toString()).mapTo(SubmitCodeRequest.class);
            log.info("Recevied request for code submit:{}", request);
            SubmitCodeResponse submitCodeResponse = compilerFactory.getCompiler(request.getCompilerType()).submitCode(request);
            JsonObject response = new JsonObject();
            response.put(DataConstants.SUCCESS, true);
            response.put(DataConstants.MESSAGE, DataConstants.SUCCESS);
            response.put(DataConstants.DATA, JsonObject.mapFrom(submitCodeResponse));
            future.complete(response);
            message.reply(response);
          } catch (Exception e){
            log.error("Error while submitting code", e);
            JsonObject response = new JsonObject().put(DataConstants.SUCCESS,false).put(DataConstants.DATA,e.getMessage());
            message.reply(response);
            future.complete(response);
          }
        });
      }
    );

    log.info("CodeHelpCompilerVerticle Deployed Successfully");
  }
}
