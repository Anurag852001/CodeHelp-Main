package com.video.CodeHelp.Dao;

import com.video.CodeHelp.Enums.CompilerTypeEnums;
import com.video.CodeHelp.Service.CachePopulationService.pojo.MainCodeCachePopulationPojo;
import com.video.CodeHelp.Service.CachePopulationService.WrapperCodeService.pojos.MainWrapperCode;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface MainCodeDao {

  @RegisterBeanMapper(MainCodeCachePopulationPojo.class)
  @SqlQuery("SELECT * FROM main_code where q_id in (<qIds>)")
  List<MainCodeCachePopulationPojo> getMainCodeCachePojoByQIds(@BindList("qIds") List<Long> qIds);

  @RegisterBeanMapper(MainCodeCachePopulationPojo.class)
  @SqlQuery("SELECT * FROM main_code where q_id = :qId")
  MainCodeCachePopulationPojo getMainCodeCachePojoByQId(@Bind("qId") Long qId);

  @GetGeneratedKeys("id")
  @SqlUpdate("INSERT INTO main_code(q_id, main_code,compiler_type,return_type) VALUES (:request.qid, :request.mainCode,:request.compilerType,:request.returnType)")
  Long saveMainCode(@BindBean("request") MainWrapperCode request);

  @RegisterBeanMapper(MainWrapperCode.class)
  @SqlQuery("SELECT * FROM main_code where q_id = :qId and compiler_type = :compilerType")
  MainWrapperCode getMainCodeByQId(@Bind("qId") Long qId, @Bind("compilerType") CompilerTypeEnums compilerType);

}
