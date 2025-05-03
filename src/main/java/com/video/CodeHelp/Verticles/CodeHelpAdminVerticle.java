package com.video.CodeHelp.Verticles;

import com.video.CodeHelp.Caffine.CaffineCacheFactory;
import com.video.CodeHelp.Config.CodeHelpConfig;
import com.video.CodeHelp.Constants.DataConstants;
import com.video.CodeHelp.Enums.*;
import com.video.CodeHelp.Exception.CodeHelpException;
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
import com.video.CodeHelp.Service.QuestionTrackerService.IQuestionTrackerService;
import com.video.CodeHelp.Service.QuestionTrackerService.impl.QuestionTrackerServiceImpl;
import com.video.CodeHelp.Service.TestCaseService.ITestCaseService;
import com.video.CodeHelp.Service.TestCaseService.TestCaseGeneratorService;
import com.video.CodeHelp.Service.TestCaseService.pojo.TestCaseGeneratorRequest;
import com.video.CodeHelp.Service.validator.ValidationFactory;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
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
  private final IQuestionTrackerService questionTrackerService;
  private final TestCaseGeneratorService testCaseGeneratorService;
  private final ValidationFactory validationFactory;

  @Inject
  public CodeHelpAdminVerticle(WelcomeService welcomeService, ConfigService configService, QuestionService questionService
    , CachePopulationFactory cachePopulationFactory, CodeHelpConfig codeHelpConfig, WrapperFactory wrapperFactory
    , CachingService cachingService, ListingFactory listingFactory, MainCodeVariableService mainCodeVariableService,
                               CorrectCodeService correctCodeService, ITestCaseService testCaseService,
                               QuestionTrackerServiceImpl questionTrackerService, TestCaseGeneratorService testCaseGeneratorService,
                               ValidationFactory validationFactory) {
    this.mainCodeVariableService = mainCodeVariableService;
    this.correctCodeService = correctCodeService;
    this.testCaseService = testCaseService;
      this.validationFactory = validationFactory;
      log.info("Intializing the codeHelpAdminVerticle");
    this.welcomeService = welcomeService;
    this.configService = configService;
    this.questionService = questionService;
    this.cachePopulationFactory = cachePopulationFactory;
    this.codeHelpCofig = codeHelpConfig;
    this.wrapperFactory = wrapperFactory;
    this.cachingService = cachingService;
    this.listingFactory = listingFactory;
    this.questionTrackerService = questionTrackerService;
    this.testCaseGeneratorService = testCaseGeneratorService;
  }


  @Override
  public void start() {

    EventBus eventBus = vertx.eventBus();
    log.info("Starting the admin verticle");
    cachePopulationFactory.populateAllCaches(codeHelpCofig.getCachePopulationTypes());

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

    eventBus.consumer(ApiEnums.CACHE_GET_API.getEventPath(), message -> vertx.executeBlocking(future -> {
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
          questionTrackerService.trackQuestion(null);
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

    eventBus.consumer(ApiEnums.TESTCASE_GENERATE_API.getEventPath(), message -> {
      vertx.executeBlocking(future -> {
        try {
          log.info("Got request in generate test case consumer:{}",message.body());
          JsonObject body = JsonObject.mapFrom(message.body());
          if(body == null){
            throw new CodeHelpException(ApplicationErrorEnums.BAD_REQUEST);
          }
          TestCaseGeneratorRequest testCaseGeneratorRequest = body.mapTo(TestCaseGeneratorRequest.class);
          String error = validationFactory.getValidator(CodeHelpClasses.TestCaseGeneratorClass).genericValidate(testCaseGeneratorRequest);
          if(StringUtils.isNotBlank(error)){
            throw new CodeHelpException(error);
          }

          CompletableFuture.runAsync(()->{
            testCaseGeneratorService.generateTestCase(testCaseGeneratorRequest.getQId(),CompilerTypeEnums.JAVA,testCaseGeneratorRequest.getNumberOfTestCases(),testCaseGeneratorRequest.getVariableNumberVsRules());
          },CommonPoolFactory.getForkJoinPool(PoolEnums.GENERATE_TEST_CASES_POOL));
          message.reply(new JsonObject().put(DataConstants.SUCCESS,true));
          future.complete(true);
        } catch (Exception e) {
          log.error("Error while generating testcases", e);
          message.reply(e);
          future.fail(e);
        }
      });
    });


    eventBus.consumer(ApiEnums.TEST_CASE_RULE_SAVE_API.getEventPath(), message -> {
      vertx.executeBlocking(future -> {
        try {
          log.info("Got request in  test case rule save consumer:{}",message.body());
          JsonObject body = JsonObject.mapFrom(message.body());
          if(body == null){
            throw new CodeHelpException(ApplicationErrorEnums.BAD_REQUEST);
          }

          message.reply(new JsonObject().put(DataConstants.SUCCESS,true));
          future.complete(true);
        } catch (Exception e) {
          log.error("Error while generating testcases", e);
          message.reply(e);
          future.fail(e);
        }
      });
    });

    eventBus.consumer(ApiEnums.TESTCASE_GET_API.getEventPath(), message -> {
      vertx.executeBlocking(future -> {
        try {
          log.info("Got request in  test case get apir:{}",message.body());
          JsonObject body = JsonObject.mapFrom(message.body());
          if(body == null){
            throw new CodeHelpException(ApplicationErrorEnums.BAD_REQUEST);
          }
          List<JsonObject> testCases = testCaseService.getTestCases(Long.valueOf(body.getString(DataConstants.QID)),null,null)
                  .stream().map(JsonObject::mapFrom).collect(Collectors.toList());

          message.reply(new JsonObject().put(DataConstants.SUCCESS,true).put(DataConstants.DATA,testCases));
          future.complete(true);
        } catch (Exception e) {
          log.error("Error while generating testcases", e);
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
