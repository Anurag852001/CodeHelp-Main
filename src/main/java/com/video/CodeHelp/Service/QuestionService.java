package com.video.CodeHelp.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.video.CodeHelp.Dao.QuestionDao;
import com.video.CodeHelp.Enums.CacheTypeEnums;
import com.video.CodeHelp.Exception.CodeHelpException;
import com.video.CodeHelp.Pojo.CompleteQuestion;
import com.video.CodeHelp.Pojo.QuestionBody;
import com.video.CodeHelp.Pojo.QuestionConstraints;
import com.video.CodeHelp.Pojo.QuestionExamples;
import com.video.CodeHelp.Pojo.Responses.SaveQuestionResponse;
import com.video.CodeHelp.utils.CachingUtils;
import io.vertx.core.eventbus.ReplyFailure;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

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

  public CompleteQuestion getQuestion(Long qNo) {
    try {
      String questionBodyCacheKey = CachingUtils.getCacheKeyForQuestionBody(qNo);
      String questionExamplesCacheKey = CachingUtils.getCacheKeyForQuestionExamples(qNo);
      String questionConstraintsCacheKey = CachingUtils.getCacheKeyForQuestionConstraints(qNo);
      QuestionBody questionBody = new ObjectMapper().convertValue(cachingService.getFromCache(CachingUtils.getCacheKeyForQuestionBody(qNo), CacheTypeEnums.ONE_DAY_COMMON_CACHE), QuestionBody.class);
      if (questionBody == null) {
        questionBody = questionDao.getQuestionBodyResponse(qNo);
        cachingService.populateInCache(questionBodyCacheKey, questionBody, CacheTypeEnums.ONE_DAY_COMMON_CACHE);
        log.info("fetched question body from db and cached with key:{}", questionBodyCacheKey);
      }
      List<QuestionExamples> questionExamples = (List<QuestionExamples>) cachingService.getFromCache(CachingUtils.getCacheKeyForQuestionBody(qNo), CacheTypeEnums.ONE_DAY_COMMON_CACHE);
      if (CollectionUtils.isNotEmpty(questionExamples)) {
        questionExamples = questionDao.getQuestionExamplesResponse(qNo);
        cachingService.populateInCache(questionExamplesCacheKey, questionExamples, CacheTypeEnums.ONE_DAY_COMMON_CACHE);
        log.info("fetched questions examples from db and cached with key:{}", questionBodyCacheKey);
      }
      List<QuestionConstraints> questionConstraints = (List<QuestionConstraints>) cachingService.getFromCache(CachingUtils.getCacheKeyForQuestionBody(qNo), CacheTypeEnums.ONE_DAY_COMMON_CACHE);
      if (CollectionUtils.isNotEmpty(questionConstraints)) {
        questionConstraints = questionDao.getQuestionConstraintsResponse(qNo);
        cachingService.populateInCache(questionConstraintsCacheKey, questionConstraints, CacheTypeEnums.ONE_DAY_COMMON_CACHE);
        log.info("fetched questions constraints from db and cached with key:{}", questionBodyCacheKey);
      }
      return new CompleteQuestion(questionBody, questionConstraints, questionExamples);
    } catch (Exception e){
      log.error("Error occurred while fetching question", e);
      throw new CodeHelpException(ReplyFailure.ERROR,"Error occured while fetching question info");
    }

  }

  public SaveQuestionResponse saveQuestion(CompleteQuestion question){
    Long qId = questionDao.saveQuestionHeaders(question.getQuestionBody());
    Long qDataId = questionDao.saveQuestionData(qId,question.getQuestionBody());

    List<Long> constraintIds = question.getQuestionConstraints().stream().map(constraint->{
      return questionDao.saveConstraints(qId,constraint);
    }).toList();

    List<Long> exampleIds = question.getQuestionExamples().stream().map(example->{
      return questionDao.saveExamples(qId,example);
    }).toList();

    return SaveQuestionResponse.builder().qId(qId).constraintId(constraintIds).examplesId(exampleIds).qDataId(qDataId).build();
  }

}
