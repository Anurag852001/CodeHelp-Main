package com.video.CodeHelp.Dao;

import com.video.CodeHelp.Enums.QuestionStatusEnums;
import com.video.CodeHelp.Pojo.QuestionBody;
import com.video.CodeHelp.Pojo.QuestionConstraints;
import com.video.CodeHelp.Pojo.QuestionExamples;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface QuestionDao {

  @RegisterBeanMapper(QuestionBody.class)
  @SqlQuery("Select q.id,q.question_heading,q.difficulty,qd.likes,qd.dislikes,qd.description from questions " +
    "as q join questions_data as qd on q.id = qd.q_id where q.id = :qNo")
  QuestionBody getQuestionBodyResponse(@Bind("qNo") Long qNo);

  @RegisterBeanMapper(QuestionExamples.class)
  @SqlQuery("Select * from question_examples where q_id = :qId")
  List<QuestionExamples> getQuestionExamplesResponse(@Bind("qId") Long qId);

  @RegisterBeanMapper(QuestionConstraints.class)
  @SqlQuery("Select * from constraints where q_id = :qId")
  List<QuestionConstraints> getQuestionConstraintsResponse(@Bind("qId") Long qId);

  @GetGeneratedKeys("id")
  @SqlUpdate("Insert into questions(difficulty,question_heading)" +
    "values(:body.difficulty, :body.questionHeading)")
  Long saveQuestionHeaders(@BindBean("body") QuestionBody body);

  @GetGeneratedKeys("id")
  @SqlUpdate("Insert into questions_data(q_id,likes,dislikes,description)" +
    "values(:qId, :body.likes, :body.dislikes,:body.description)")
  Long saveQuestionData(@Bind("qId") Long qId, @BindBean("body") QuestionBody body);

  @GetGeneratedKeys("id")
  @SqlUpdate("Insert into constraints(constraint_description,q_id) values(:constraint.constraintDescription, :qId)")
  Long saveConstraints(@Bind("qId") Long qId, @BindBean("constraint") QuestionConstraints constraint);

  @GetGeneratedKeys("id")
  @SqlUpdate("Insert into question_examples(q_id,example_name,example_input,example_output,explanation) values(:qId,:example.exampleName, :example.exampleInput,:example.exampleOutput,:example.explanation)")
  Long saveExamples(@Bind("qId") Long qId, @BindBean("example") QuestionExamples example);

  @RegisterBeanMapper(List.class)
  @SqlQuery("Select id from questions where status = :status")
  List<Long> getQuestionIdsByStatus(@Bind("status") QuestionStatusEnums status);


}
