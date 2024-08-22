package com.video.CodeHelp.Dao;

import com.video.CodeHelp.Pojo.QuestionResponse;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

public interface QuestionDao {

  @RegisterBeanMapper(QuestionResponse.class)
  @SqlQuery("Select q.heading,q.difficulty from questions as q where q.id = qNo")
  QuestionResponse getQuestionResponse( @Bind("qNo") Integer qNo);



}
