package com.video.CodeHelp.Dao;

import com.video.CodeHelp.Enums.CompilerTypeEnums;
import com.video.CodeHelp.Pojo.MainCodeVariable;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.statement.SqlBatch;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface MainCodeVariablesDao {

  @SqlBatch("INSERT INTO main_code_variables (question_number, language, variable_number, type, name) " +
    "VALUES (:variables.questionNumber, :variables.language, :variables.variableNumber, :variables.type, :variables.name) ")
  void saveMainCodeVariables(@BindBean("variables") List<MainCodeVariable> variables);

  @SqlUpdate("Update main_code_variables set language = :variable.language, type = :variable.type, name = :variable.name,q_id = :variable.qid,variable_number = :variable.variableNumber where id = :variable.id")
  void updateMainCodeVariables(@BindBean("variable") MainCodeVariable variables);

  @RegisterBeanMapper(MainCodeVariable.class)
  @SqlQuery("Select * from main_code_variables where language = :language and question_number = :qid")
  List<MainCodeVariable> getMainCodeVariablesByLanguageAndQId(@Bind("language") CompilerTypeEnums language, @Bind("qid") Long qId);

}
