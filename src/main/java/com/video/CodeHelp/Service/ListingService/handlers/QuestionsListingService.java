package com.video.CodeHelp.Service.ListingService.handlers;

import com.video.CodeHelp.Dao.QuestionDao;
import com.video.CodeHelp.Service.ListingService.IListingService;
import com.video.CodeHelp.Service.ListingService.pojo.IListingResponse;
import com.video.CodeHelp.Service.ListingService.pojo.QuestionListingPojo;
import jakarta.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;

public class QuestionsListingService implements IListingService {

  private final QuestionDao dao;

  @Inject
  public QuestionsListingService(QuestionDao dao) {
    this.dao = dao;
  }

  @Override
  public List<IListingResponse> getListing(Integer limit, Integer offset) {
    List<QuestionListingPojo> questionListingPojoList =  dao.getAllQuestions(limit, offset);
    return questionListingPojoList.stream().map(pojo->(IListingResponse)pojo).collect(Collectors.toList());
  }

  @Override
  public Long getTotalCount() {
   return dao.getTotalQuestionsCount();
  }

}
