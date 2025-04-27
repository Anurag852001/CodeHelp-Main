package com.video.CodeHelp.Service.QuestionTrackerService.impl;

import com.video.CodeHelp.Config.CodeHelpConfig;
import com.video.CodeHelp.Config.configPojos.CodeHelpReportingServiceConfig;
import com.video.CodeHelp.Constants.DataConstants;
import com.video.CodeHelp.Enums.CompilerTypeEnums;
import com.video.CodeHelp.Enums.PoolEnums;
import com.video.CodeHelp.Service.CommonPoolFactory;
import com.video.CodeHelp.Service.QuestionTrackerService.IQuestionTrackerService;
import com.video.CodeHelp.Service.QuestionTrackerService.pojo.QuestionTrackerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Singleton
public class QuestionTrackerServiceImpl implements IQuestionTrackerService {

    private final WebClient webClient;
    private final CodeHelpReportingServiceConfig codeHelpReportingServiceConfig;

    @Inject
    public QuestionTrackerServiceImpl(@Named(DataConstants.WEB_CLIENT) WebClient webClient, CodeHelpConfig codeHelpConfig){
        this.webClient = webClient;
        this.codeHelpReportingServiceConfig = codeHelpConfig.getCodeHelpReportingServiceConfig();
    }


    @Override
    public void trackQuestion(QuestionTrackerRequest request) {
        QuestionTrackerRequest request2 = QuestionTrackerRequest.builder().questionNo(1L).uuid("anurag")
                .runtime(198L).compilerTypeEnums(CompilerTypeEnums.JAVA).solved(true).build();
        CompletableFuture.runAsync(()->{
            try {
                log.info("Tracking question request:{}", request2);
                String str = "/codehelp/track/question";
                webClient.post(codeHelpReportingServiceConfig.getPort(), codeHelpReportingServiceConfig.getHost(), str)
                        .putHeader("Content-Type","application/json")
                        .sendJson(JsonObject.mapFrom(request2)).onComplete(e->{
                    if(e.succeeded()){
                        log.info("Success in tracking");
                    } else {
                        log.error("Error occured while tracking :{}",e);
                    }
                });
            } catch (Exception e){
                log.error("Error while tracking question:{}",request,e);
            }
        }, CommonPoolFactory.getForkJoinPool(PoolEnums.TRACK_QUESTIONS_POOL));
    }
}
