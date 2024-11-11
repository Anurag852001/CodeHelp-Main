package com.video.CodeHelp.Dao;

import com.video.CodeHelp.Enums.CompilerTypeEnums;
import com.video.CodeHelp.Pojo.CorrectCodePojo;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface CorrectCodeDao {

  @GetGeneratedKeys
  @SqlUpdate("INSERT INTO correct_code (code, language, q_id) VALUES (:code, :language, :qId)")
  Long saveCorrectCode(@Bind("code") String code, @Bind("language")CompilerTypeEnums language, @Bind("qId") Long qId);


  @SqlUpdate("UPDATE correct_code SET code = :code WHERE q_id = :qid and language = :language")
  void updateCorrectCode(@Bind("code") String code, @Bind("qid") Long qid, @Bind("language") CompilerTypeEnums language);

  @RegisterBeanMapper(CorrectCodePojo.class)
  @SqlQuery("SELECT * FROM correct_code WHERE q_no = :q_no AND language = :language")
  CorrectCodePojo getCorrectCodeByQNoAndLanguage(@Bind("q_no") Long qNo, @Bind("language") CompilerTypeEnums language);
}
