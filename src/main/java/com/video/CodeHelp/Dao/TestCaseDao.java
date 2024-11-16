package com.video.CodeHelp.Dao;

import com.video.CodeHelp.Enums.CompilerTypeEnums;
import com.video.CodeHelp.Enums.TestCaseType;
import com.video.CodeHelp.Pojo.TestCase;
import com.video.CodeHelp.Pojo.TestCaseResult;
import com.video.CodeHelp.Pojo.TestCaseRuleInfo;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface TestCaseDao {

  @RegisterBeanMapper(TestCase.class)
  @SqlQuery("Select * from test_cases where q_id =:qId  and test_case_type =:testCaseType")
  public List<TestCase> getTestCases(@Bind("qId") Long qId, @Bind("testCaseType")TestCaseType testCaseType);

  @SqlUpdate("Insert into test_cases (q_id,test_case_id,value,test_case_type,variable_number,data_type) values (:qNo, :language, :testCase.value,:testCase.testCaseType,:testCase.variableNumber,:testCase.dataType)")
  public void saveTestCase(@Bind("qNo") Long qNo, @Bind("language") CompilerTypeEnums language, @BindList("testCase") List<TestCase> testCase);

  @SqlUpdate("Insert into test_case_solution(test_case_id,solution) values(:testCaseId,:solution)")
  public void saveTestCaseSolution(@Bind("testCaseId") Long testCaseId,@Bind("solution") String solution);

  @RegisterBeanMapper(TestCaseResult.class)
  @SqlQuery("Select * from test_case_results where q_id = :qid,language =:language")
  public List<TestCaseResult>getTestCaseResults(@Bind("language") CompilerTypeEnums compilerTypeEnums,@Bind("qid") Long qid);

  @RegisterBeanMapper(TestCaseRuleInfo.class)
  @SqlQuery("Select * from test_case_generator_rules where qid = :qid,variable_number =:variableNumber")
  public List<TestCaseRuleInfo> getTestCaseGeneratorRules(@Bind("qid") Integer qid,@Bind("variableNumber") Integer variableNumber);

}
