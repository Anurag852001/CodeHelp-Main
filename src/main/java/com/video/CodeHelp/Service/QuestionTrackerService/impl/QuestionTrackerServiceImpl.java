package com.video.CodeHelp.Service.QuestionTrackerService.impl;

import com.video.CodeHelp.Constants.DataConstants;
import com.video.CodeHelp.Enums.PoolEnums;
import com.video.CodeHelp.Service.CommonPoolFactory;
import com.video.CodeHelp.Service.QuestionTrackerService.IQuestionTrackerService;
import com.video.CodeHelp.Service.QuestionTrackerService.pojo.QuestionTrackerRequest;
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

    @Inject
    public QuestionTrackerServiceImpl(@Named(DataConstants.WEB_CLIENT) WebClient webClient){
        this.webClient = webClient;
    }


    @Override
    public void trackQuestion(QuestionTrackerRequest request) {
        CompletableFuture.runAsync(()->{
            try {
                log.info("Tracking question request:{}", request);
                String str = " /codehelp/track/question";
                webClient.post(9000, "localhost", str).send().onComplete(e->{
                    if(e.succeeded()){
                        log.info("Succes in tracking");
                    } else {
                        log.error("Error occured while tracking :{}",e.cause());
                    }
                });
            } catch (Exception e){
                log.error("Error while tracking question:{}",request,e);
            }
        }, CommonPoolFactory.getForkJoinPool(PoolEnums.TRACK_QUESTIONS_POOL));
    }
}
