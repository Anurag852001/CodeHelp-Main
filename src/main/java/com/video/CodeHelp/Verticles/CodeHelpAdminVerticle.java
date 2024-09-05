package com.video.CodeHelp.Verticles;

import com.video.CodeHelp.Caffine.CaffineCacheFactory;
import com.video.CodeHelp.Config.CodeHelpConfig;
import com.video.CodeHelp.Constants.DataConstants;
import com.video.CodeHelp.Enums.ApiEnums;
import com.video.CodeHelp.Enums.CacheTypeEnums;
import com.video.CodeHelp.Pojo.*;
import com.video.CodeHelp.Service.CachingService;
import com.video.CodeHelp.Service.Factory.WrapperCodeFactory.enums.WrapperCodeEnums;
import com.video.CodeHelp.Service.Factory.WrapperCodeFactory.pojos.ISaveWrapperCodeRequest;
import com.video.CodeHelp.Service.Factory.WrapperCodeFactory.pojos.IWrapperCodeResponse;
import com.video.CodeHelp.Pojo.Responses.SaveQuestionResponse;
import com.video.CodeHelp.Service.CachePopulationService.CachePopulationFactory;
import com.video.CodeHelp.Service.ConfigService;
import com.video.CodeHelp.Service.Factory.WrapperCodeFactory.WrapperFactory;
import com.video.CodeHelp.Service.QuestionService;
import com.video.CodeHelp.Service.WelcomeService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;



@Singleton
@Slf4j
public class CodeHelpAdminVerticle extends AbstractVerticle {

  private final WelcomeService welcomeService;
  private final ConfigService configService;
  private final QuestionService questionService;
  private final CachePopulationFactory cachePopulationFactory;
  private final CodeHelpConfig codeHelpCofig;
  private final WrapperFactory wrapperFactory;
  private final CachingService cachingService;

  @Inject
  public CodeHelpAdminVerticle(WelcomeService welcomeService, ConfigService configService,QuestionService questionService
                               , CachePopulationFactory cachePopulationFactory,CodeHelpConfig codeHelpConfig,WrapperFactory wrapperFactory
                              , CachingService cachingService) {
    log.info("Intializing the codeHelpAdminVerticle");
    this.welcomeService = welcomeService;
    this.configService = configService;
    this.questionService = questionService;
    this.cachePopulationFactory = cachePopulationFactory;
    this.codeHelpCofig = codeHelpConfig;
    this.wrapperFactory = wrapperFactory;
    this.cachingService = cachingService;
  }


  @Override
  public void start() {

    EventBus eventBus = vertx.eventBus();
    log.info("Starting the admin verticle");
    cachePopulationFactory.populateAllCaches(codeHelpCofig.getCachePopulationTypes());

    eventBus.consumer(ApiEnums.WELCOME_API.getEventPath(), message -> {
      vertx.executeBlocking(future -> {
        try {
          JsonObject response = new JsonObject();
          response.put(DataConstants.SUCCESS, true);
          response.put(DataConstants.MESSAGE, "Welcome to code help!!");
          welcomeService.intoWelcomeService();
          message.reply(response);
          future.complete(response);
        } catch (Exception e) {
          log.error("Error while welcome api", e);
          future.fail(e);
        }
      });
    });

    eventBus.consumer(ApiEnums.CONFIG_SAVE_API.getEventPath(), message -> {
      vertx.executeBlocking(future -> {
        try {
          JsonObject body = new JsonObject(message.body().toString());
          SaveOrUpdateConfigRequest request = body.mapTo(SaveOrUpdateConfigRequest.class);
          JsonObject response = new JsonObject();
          Long id = configService.saveCodeHelpConfig(request);
          response.put(DataConstants.SUCCESS, true);
          response.put(DataConstants.MESSAGE, "Config saved successfully");
          response.put(DataConstants.ID, id);
          message.reply(response);
          future.complete(response);
        } catch (Exception e) {
          log.error("Error while saving config", e);
          message.reply(e);
          future.fail(e);
        }
      });
    });

    eventBus.consumer(ApiEnums.CONFIG_UPDATE_API.getEventPath(), message -> {
      vertx.executeBlocking(future -> {
        try {
          JsonObject body = new JsonObject(message.body().toString());
          SaveOrUpdateConfigRequest request = body.mapTo(SaveOrUpdateConfigRequest.class);
          JsonObject response = new JsonObject();
          Long id = configService.updateCodeHelpConfig(request);
          response.put(DataConstants.SUCCESS, true);
          response.put(DataConstants.MESSAGE, "Config updated successfully");
          response.put(DataConstants.ID, id);
          message.reply(response);
          future.complete(response);
        } catch (Exception e) {
          log.error("Error while saving config", e);
          message.reply(e);
          future.fail(e);
        }
      });
    });

    eventBus.consumer(ApiEnums.CONFIG_GET_API.getEventPath(), message -> {
        vertx.executeBlocking(future -> {
            try {
              JsonObject request = new JsonObject(message.body().toString());
              Config config = configService.getCodeHelpConfig(request.getString(DataConstants.CONFIG_KEY), request.getString(DataConstants.CONFIG_TYPE));
              JsonObject configJ = JsonObject.mapFrom(config);
              JsonObject response = new JsonObject().put(DataConstants.MESSAGE, DataConstants.SUCCESS);
              response.put(DataConstants.SUCCESS, true);
              response.put(DataConstants.DATA, configJ);
              message.reply(response);
              future.complete(response);
            } catch (Exception e) {
              log.error("Error while getting config", e);
              message.reply(e);
              future.fail(e);
            }
          }
        );
      }
    );

    eventBus.consumer(ApiEnums.CACHE_GET_API.getEventPath(), message -> {
      vertx.executeBlocking(future -> {
        try {
          JsonObject body = new JsonObject(message.body().toString());
          CacheTypeEnums cacheTypeEnum = CacheTypeEnums.valueOf(body.getString(DataConstants.CACHE_TYPE));
          JsonObject response = new JsonObject();
          response.put(DataConstants.DATA, CaffineCacheFactory.getAllDataInCache(cacheTypeEnum.getCache()));
          message.reply(response);
          future.complete(response);
        } catch (Exception e) {
          log.error("Error while saving config", e);
          message.reply(e);
          future.fail(e);
        }
      });
    });

    eventBus.consumer(ApiEnums.QUESTION_GET_API.getEventPath(), message -> {
      vertx.executeBlocking(future -> {
        try {
          JsonObject body = new JsonObject(message.body().toString());
          Long qNo = Long.parseLong(body.getString(DataConstants.Q_NO));
          CompleteQuestion completeQuestion = questionService.getQuestion(qNo);
          JsonObject response = new JsonObject();
          response.put(DataConstants.SUCCESS, true);
          response.put(DataConstants.MESSAGE, "Question fetched successfully");
          response.put(DataConstants.DATA, JsonObject.mapFrom(completeQuestion));
          message.reply(response);
          future.complete(response);
        } catch (Exception e) {
          log.error("Error while saving config", e);
          message.reply(e);
          future.fail(e);
        }
      });
    });

    eventBus.consumer(ApiEnums.QUESTION_SAVE_API.getEventPath(), message -> {
      vertx.executeBlocking(future -> {
        try {
          JsonObject body = new JsonObject(message.body().toString());
          SaveQuestionResponse saveQuestionResponse = questionService.saveQuestion(body.mapTo(CompleteQuestion.class));
          JsonObject response = new JsonObject();
          response.put(DataConstants.SUCCESS, true);
          response.put(DataConstants.MESSAGE, "Question saved successfully");
          response.put(DataConstants.DATA, JsonObject.mapFrom(saveQuestionResponse));
          message.reply(response);
          future.complete(response);
        } catch (Exception e) {
          log.error("Error while saving config", e);
          message.reply(e);
          future.fail(e);
        }
      });
    });

    eventBus.consumer(ApiEnums.GET_WRAPPER_CODE.getEventPath(), message -> {
      vertx.executeBlocking(future -> {
        try {
          JsonObject body = new JsonObject(message.body().toString());
          GetWrapperCodeRequest request = body.mapTo(GetWrapperCodeRequest.class);
          IWrapperCodeResponse response = wrapperFactory.getWrapperService(request.getWrapperCodeType()).getWrapperCode(request);
          JsonObject responseJ = new JsonObject();
          responseJ.put(DataConstants.SUCCESS, true);
          responseJ.put(DataConstants.MESSAGE, "Wrapper code fetched successfully");
          responseJ.put(DataConstants.DATA, JsonObject.mapFrom(response));
          message.reply(responseJ);
          future.complete(responseJ);
        } catch (Exception e) {
          log.error("Error while saving config", e);
          message.reply(e);
          future.fail(e);
        }
      });
    });

    eventBus.consumer(DataConstants.SYNC_IN_CACHE,message->{
      vertx.executeBlocking(future->{
        try{
          JsonObject body = JsonObject.mapFrom(message.body());
          SyncInCacheRequest request = body.mapTo(SyncInCacheRequest.class);
          log.info("Request received to sync in cache:{}",request);
          cachingService.populateInCache(request.getCacheKey(),request.getValue(),request.getCacheTypeEnums());
          message.reply(true);
          future.complete(true);
        } catch (Exception e){
          log.error("Error while syncing data in cache", e);
          future.fail(e);
        }
      });
    });

    eventBus.consumer(ApiEnums.SAVE_WRAPPER_CODE.getEventPath(),message->{
      vertx.executeBlocking(future->{
        try{
          JsonObject body = JsonObject.mapFrom(message.body());
          Long id = wrapperFactory.getWrapperService(WrapperCodeEnums.valueOf(body.getString(DataConstants.WRAPPER_CODE_ENUM))).saveWrapperCode(body);
          JsonObject response = new JsonObject();
          response.put(DataConstants.SUCCESS, true);
          response.put(DataConstants.MESSAGE, DataConstants.SUCCESS);
          response.put(DataConstants.DATA, id);
          message.reply(response);
          future.complete(true);
        } catch (Exception e){
          log.error("Error while syncing data in cache", e);
          message.reply(e);
          future.fail(e);
        }
      });
    });


  }
}
