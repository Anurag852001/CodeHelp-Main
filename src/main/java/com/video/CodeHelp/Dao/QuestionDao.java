package com.video.CodeHelp.Dao;

import com.video.CodeHelp.Pojo.QuestionBodyResponse;
import com.video.CodeHelp.Pojo.QuestionsConstraintsResponse;
import com.video.CodeHelp.Pojo.QuestionsExamplesResponse;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface QuestionDao {

  @RegisterBeanMapper(QuestionBodyResponse.class)
  @SqlQuery("Select q.heading,q.difficulty,qd.likes,qd.dislikes, from questions " +
    "as join questions_data as qd on q.id = qd.q_id" +" where q.id = :qNo")
  QuestionBodyResponse getQuestionBodyResponse(@Bind("qNo") Long qNo);

  @RegisterBeanMapper(QuestionsExamplesResponse.class)
  @SqlQuery("Select * from questions_examples where q_id = :qId")
  QuestionsExamplesResponse getQuestionExamplesResponse(@Bind("qId") Long qId);

  @RegisterBeanMapper(QuestionsConstraintsResponse.class)
  @SqlQuery("Select * from questions_constraints where q_id = :qId")
  QuestionsConstraintsResponse getQuestionConstraintsResponse(@Bind("qId") Long qId);

  @GetGeneratedKeys("id")
  @SqlUpdate("Insert into questions()" +
    "values(:difficulty, :heading)")
  Long saveQuestionHeaders(@Bind("difficulty") Integer difficulty, @Bind("heading") String heading);

  @GetGeneratedKeys("id")
  @SqlUpdate("Insert into questions_data(q_id,likes,dislikes,description)" +
    "values(:qId, :likes, :dislikes,:description)")
  Long saveQuestionData(@Bind("qId") Long qId, @Bind("likes") Integer likes,
                       @Bind("dislikes") Integer dislikes, @Bind("description") String description);

  @GetGeneratedKeys("id")
  @SqlUpdate("Insert into constraints(constraint_description,q_id) values(:constraint_description, :qId)")
  Long saveConstraints(@Bind("constraint_description") String constraintDescription, @Bind("qId") Long qId);

  @GetGeneratedKeys("id")
  @SqlUpdate("Insert into questions_examples(example_name,example_input,example_output,explanation) values(:example_name, :example_input,:example_output,:explanation)")
  Long saveExamples(@Bind("example_name") String exampleName, @Bind("example_input") String exampleInput,
                   @Bind("example_output") String exampleOutput, @Bind("explanation") String explanation);

}
