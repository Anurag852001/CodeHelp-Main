package com.video.CodeHelp.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.video.CodeHelp.Dao.QuestionDao;
import com.video.CodeHelp.Enums.CacheTypeEnums;
import com.video.CodeHelp.Exception.CodeHelpException;
import com.video.CodeHelp.Pojo.CompleteQuestionResponse;
import com.video.CodeHelp.Pojo.QuestionBodyResponse;
import com.video.CodeHelp.Pojo.QuestionsConstraintsResponse;
import com.video.CodeHelp.Pojo.QuestionsExamplesResponse;
import com.video.CodeHelp.utils.CachingUtils;
import com.video.CodeHelp.utils.CommonUtils;
import io.vertx.core.eventbus.ReplyFailure;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class QuestionService {

  private final QuestionDao questionDao;
  private final CachingService cachingService;

  @Inject
  public QuestionService(QuestionDao questionDao,CachingService cachingService) {
    this.questionDao = questionDao;
    this.cachingService = cachingService;
  }

  public CompleteQuestionResponse getQuestion(Long qNo) {
    try {
      String questionBodyCacheKey = CachingUtils.getCacheKeyForQuestionBody(qNo);
      String questionExamplesCacheKey = CachingUtils.getCacheKeyForQuestionExamples(qNo);
      String questionConstraintsCacheKey = CachingUtils.getCacheKeyForQuestionConstraints(qNo);
      QuestionBodyResponse questionBodyResponse = new ObjectMapper().convertValue(cachingService.getFromCache(CachingUtils.getCacheKeyForQuestionBody(qNo), CacheTypeEnums.ONE_DAY_COMMON_CACHE), QuestionBodyResponse.class);
      if (questionBodyResponse == null) {
        questionBodyResponse = questionDao.getQuestionBodyResponse(qNo);
        cachingService.populateInCache(questionBodyCacheKey, questionBodyResponse, CacheTypeEnums.ONE_DAY_COMMON_CACHE);
        log.info("fetched question body from db and cached with key:{}", questionBodyCacheKey);
      }
      QuestionsExamplesResponse questionsExamplesResponse = new ObjectMapper().convertValue(cachingService.getFromCache(CachingUtils.getCacheKeyForQuestionBody(qNo), CacheTypeEnums.ONE_DAY_COMMON_CACHE), QuestionsExamplesResponse.class);
      if (questionsExamplesResponse == null) {
        questionsExamplesResponse = questionDao.getQuestionExamplesResponse(qNo);
        cachingService.populateInCache(questionExamplesCacheKey, questionsExamplesResponse, CacheTypeEnums.ONE_DAY_COMMON_CACHE);
        log.info("fetched questions examples from db and cached with key:{}", questionBodyCacheKey);
      }
      QuestionsConstraintsResponse questionsConstraintsResponse = new ObjectMapper().convertValue(cachingService.getFromCache(CachingUtils.getCacheKeyForQuestionBody(qNo), CacheTypeEnums.ONE_DAY_COMMON_CACHE), QuestionsConstraintsResponse.class);
      if (questionsConstraintsResponse == null) {
        questionsConstraintsResponse = questionDao.getQuestionConstraintsResponse(qNo);
        cachingService.populateInCache(questionConstraintsCacheKey, questionsConstraintsResponse, CacheTypeEnums.ONE_DAY_COMMON_CACHE);
        log.info("fetched questions constraints from db and cached with key:{}", questionBodyCacheKey);
      }
      return new CompleteQuestionResponse(questionBodyResponse, questionsConstraintsResponse, questionsExamplesResponse);
    } catch (Exception e){
      log.error("Error occurred while fetching question", e);
      throw new CodeHelpException(ReplyFailure.ERROR,"Error occured while fetching question info");
    }

  }

}
