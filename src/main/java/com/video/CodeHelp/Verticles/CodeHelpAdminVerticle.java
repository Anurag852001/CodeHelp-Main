package com.video.CodeHelp.Verticles;

import com.video.CodeHelp.Caffine.CaffineCacheFactory;
import com.video.CodeHelp.Config.CodeHelpConfig;
import com.video.CodeHelp.Constants.DataConstants;
import com.video.CodeHelp.Enums.ApiEnums;
import com.video.CodeHelp.Enums.CacheTypeEnums;
import com.video.CodeHelp.Enums.CompilerTypeEnums;
import com.video.CodeHelp.Pojo.*;
import com.video.CodeHelp.Pojo.Responses.SaveQuestionResponse;
import com.video.CodeHelp.Service.CachePopulationService.CachePopulationFactory;
import com.video.CodeHelp.Service.CachePopulationService.WrapperCodeService.WrapperFactory;
import com.video.CodeHelp.Service.CachePopulationService.WrapperCodeService.enums.WrapperCodeEnums;
import com.video.CodeHelp.Service.CachePopulationService.WrapperCodeService.pojos.IWrapperCodeResponse;
import com.video.CodeHelp.Service.*;
import com.video.CodeHelp.Service.ListingService.ListingFactory;
import com.video.CodeHelp.Service.ListingService.pojo.GetListingRequest;
import com.video.CodeHelp.Service.ListingService.pojo.IListingResponse;
import com.video.CodeHelp.Service.TestCaseService.ITestCaseService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;


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
  private final ListingFactory listingFactory;
  private final MainCodeVariableService mainCodeVariableService;
  private final CorrectCodeService correctCodeService;
  private final ITestCaseService testCaseService;

  @Inject
  public CodeHelpAdminVerticle(WelcomeService welcomeService, ConfigService configService, QuestionService questionService
    , CachePopulationFactory cachePopulationFactory, CodeHelpConfig codeHelpConfig, WrapperFactory wrapperFactory
    , CachingService cachingService, ListingFactory listingFactory, MainCodeVariableService mainCodeVariableService,
                               CorrectCodeService correctCodeService, ITestCaseService testCaseService) {
    this.mainCodeVariableService = mainCodeVariableService;
    this.correctCodeService = correctCodeService;
    this.testCaseService = testCaseService;
    log.info("Intializing the codeHelpAdminVerticle");
    this.welcomeService = welcomeService;
    this.configService = configService;
    this.questionService = questionService;
    this.cachePopulationFactory = cachePopulationFactory;
    this.codeHelpCofig = codeHelpConfig;
    this.wrapperFactory = wrapperFactory;
    this.cachingService = cachingService;
    this.listingFactory = listingFactory;
  }


  @Override
  public void start() {

    EventBus eventBus = vertx.eventBus();
    log.info("Starting the admin verticle");
//    cachePopulationFactory.populateAllCaches(codeHelpCofig.getCachePopulationTypes());

    eventBus.consumer(ApiEnums.WELCOME_API.getEventPath(), message ->
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
      })
    );

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

    eventBus.consumer(ApiEnums.CONFIG_GET_API.getEventPath(), message -> vertx.executeBlocking(future -> {
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
    )
    );

    eventBus.consumer("cacheGet", message -> vertx.executeBlocking(future -> {
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
    }));

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

    eventBus.consumer(DataConstants.SYNC_IN_CACHE, message -> {
      vertx.executeBlocking(future -> {
        try {
          JsonObject body = JsonObject.mapFrom(message.body());
          SyncInCacheRequest request = body.mapTo(SyncInCacheRequest.class);
          log.info("Request received to sync in cache:{}", request);
          cachingService.populateInCache(request.getCacheKey(), request.getValue(), request.getCacheTypeEnums());
          message.reply(true);
          future.complete(true);
        } catch (Exception e) {
          log.error("Error while syncing data in cache", e);
          future.fail(e);
        }
      });
    });

    eventBus.consumer(ApiEnums.SAVE_WRAPPER_CODE.getEventPath(), message -> {
      vertx.executeBlocking(future -> {
        try {
          JsonObject body = JsonObject.mapFrom(message.body());
          Long id = wrapperFactory.getWrapperService(WrapperCodeEnums.valueOf(body.getString(DataConstants.WRAPPER_CODE_ENUM))).saveWrapperCode(body);
          JsonObject response = new JsonObject();
          response.put(DataConstants.SUCCESS, true);
          response.put(DataConstants.MESSAGE, DataConstants.SUCCESS);
          response.put(DataConstants.DATA, id);
          message.reply(response);
          future.complete(true);
        } catch (Exception e) {
          log.error("Error while syncing data in cache", e);
          message.reply(e);
          future.fail(e);
        }
      });
    });


    eventBus.consumer(ApiEnums.SAVE_MAIN_CODE_VARIABLES_API.getEventPath(), message -> {
      vertx.executeBlocking(future -> {
        try {
          List<MainCodeVariable> variables = JsonObject.mapFrom(message.body())
            .getJsonArray(DataConstants.VARIABLES)
            .stream().map(object -> JsonObject.mapFrom(object).mapTo(MainCodeVariable.class))
            .collect(Collectors.toList());
          mainCodeVariableService.saveVariables(variables);
          JsonObject response = new JsonObject();
          response.put(DataConstants.SUCCESS, true);
          response.put(DataConstants.MESSAGE, DataConstants.SUCCESS);
          message.reply(response);
          future.complete(response);
        } catch (Exception e) {
          log.error("Error while saving main code variables", e);
          message.reply(e);
          future.fail(e);
        }
      });
    });

    eventBus.consumer(ApiEnums.GET_MAIN_CODE_VARIABLES_API.getEventPath(), message -> {
      vertx.executeBlocking(future -> {
        try {
          JsonObject body = JsonObject.mapFrom(message.body());
          Long qid = body.getLong(DataConstants.QID);
          String language = body.getString(DataConstants.LANGUAGE);
          List<MainCodeVariable> variables = mainCodeVariableService.getVariables(qid, CompilerTypeEnums.getFromLanguage(language));
          JsonObject response = new JsonObject();
          response.put(DataConstants.SUCCESS, true);
          response.put(DataConstants.MESSAGE, DataConstants.SUCCESS);
          response.put(DataConstants.VARIABLES, new JsonArray(variables.stream().map(obj->JsonObject.mapFrom(obj)).collect(Collectors.toList())));
          message.reply(response);
          future.complete(response);
        } catch (Exception e) {
          log.error("Error while syncing data in cache", e);
          message.reply(e);
          future.fail(e);
        }
      });
    });

    eventBus.consumer(ApiEnums.SAVE_CORRECT_CODE_API.getEventPath(), message -> {
      vertx.executeBlocking(future -> {
        try {
          JsonObject body = JsonObject.mapFrom(message.body());
          CorrectCodePojo correctCodePojo = body.mapTo(CorrectCodePojo.class);
          log.info("Received correct code save request with body:{}", correctCodePojo);
          Long id = correctCodeService.saveCorrectCode(correctCodePojo);
          JsonObject response = new JsonObject();
          response.put(DataConstants.SUCCESS, true);
          response.put(DataConstants.MESSAGE, DataConstants.SUCCESS);
          response.put(DataConstants.ID, id);
          message.reply(response);
          future.complete(response);
        } catch (Exception e) {
          log.error("Error while saving correct code", e);
          message.reply(processResponse(e.getMessage()));
          future.fail(e);
        }
      });
    });

    eventBus.consumer(ApiEnums.UPDATE_CORRECT_CODE_API.getEventPath(), message -> {
      vertx.executeBlocking(future -> {
        try {
          JsonObject body = JsonObject.mapFrom(message.body());
          CorrectCodePojo correctCodePojo = body.mapTo(CorrectCodePojo.class);
          log.info("Received correct code update request with body:{}", correctCodePojo);
           correctCodeService.updateCorrectCode(correctCodePojo);
          JsonObject response = new JsonObject();
          response.put(DataConstants.SUCCESS, true);
          response.put(DataConstants.MESSAGE, DataConstants.SUCCESS);
          message.reply(response);
          future.complete(response);
        } catch (Exception e) {
          log.error("Error while saving correct code", e);
          message.reply(processResponse(e.getMessage()));
          future.fail(e);
        }
      });
    });

    eventBus.consumer(ApiEnums.TESTCASE_SAVE_API.getEventPath(), message -> {
      vertx.executeBlocking(future -> {
        try {
          JsonObject body = JsonObject.mapFrom(message.body());
          TestCaseSaveRequest testCaseSaveRequest = body.mapTo(TestCaseSaveRequest.class);
          log.info("Received test case save request:{}", testCaseSaveRequest);
          testCaseService.saveTestCases(testCaseSaveRequest);
          JsonObject response = new JsonObject();
          response.put(DataConstants.SUCCESS, true);
          response.put(DataConstants.MESSAGE, DataConstants.SUCCESS);
          message.reply(response);
          future.complete(response);
        } catch (Exception e) {
          log.error("Error while saving correct code", e);
          message.reply(processResponse(e.getMessage()));
          future.fail(e);
        }
      });
    });


    eventBus.consumer(ApiEnums.GET_CORRECT_CODE_API.getEventPath(), message -> {
      vertx.executeBlocking(future -> {
        try {
          JsonObject body = JsonObject.mapFrom(message.body());
          CorrectCodePojo correctCodePojo = body.mapTo(CorrectCodePojo.class);
          log.info("Received correct code get request with body:{}", correctCodePojo);
          CorrectCodePojo responsePojo =  correctCodeService.getCorrectCode(correctCodePojo);
          JsonObject response = new JsonObject();
          response.put(DataConstants.SUCCESS, true);
          response.put(DataConstants.MESSAGE, DataConstants.SUCCESS);
          response.put(DataConstants.CORRECT_CODE_CAMEL,JsonObject.mapFrom(responsePojo));
          message.reply(response);
          future.complete(response);
        } catch (Exception e) {
          log.error("Error while saving correct code", e);
          message.reply(e);
          future.fail(e);
        }
      });
    });

    eventBus.consumer(ApiEnums.GENERIC_LIST_API.getEventPath(), message -> {
      vertx.executeBlocking(future -> {
        try {
          JsonObject body = JsonObject.mapFrom(message.body());
          GetListingRequest request = body.mapTo(GetListingRequest.class);
          List<IListingResponse> listingResponseList = listingFactory.getListingService(request.getListingEnum())
            .getListing(request.getCount(), (request.getPage() - 1) * request.getCount());

          Long totalCount = listingFactory
            .getListingService(request.getListingEnum()).getTotalCount();

          JsonObject response = new JsonObject();
          response.put(DataConstants.SUCCESS, true);
          response.put(DataConstants.MESSAGE, DataConstants.SUCCESS);
          response.put(DataConstants.DATA, new JsonObject()
            .put(DataConstants.LISTING,
              new JsonArray(listingResponseList.stream()
                .map(pojo -> JsonObject.mapFrom(pojo))
                .collect(Collectors.toList()))).put(DataConstants.TOTAL_COUNT, totalCount));

          message.reply(response);
          future.complete(true);
        } catch (Exception e) {
          log.error("Error while getting main code variables", e);
          message.reply(e);
          future.fail(e);
        }
      });
    });


  }

  private JsonObject processResponse(String str){
    return new JsonObject().put(DataConstants.SUCCESS,false).put(DataConstants.MESSAGE,str);
  }
}
