package com.video.CodeHelp.Dao;

import com.video.CodeHelp.Enums.CompilerTypeEnums;
import com.video.CodeHelp.Service.CachePopulationService.pojo.DefaultCodeCachePopulationPojo;
import com.video.CodeHelp.Service.CachePopulationService.WrapperCodeService.pojos.DefaultWrapperCode;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface DefaultCodeDao {

  @RegisterBeanMapper(DefaultCodeCachePopulationPojo.class)
  @SqlQuery("SELECT * FROM default_code where q_id in (<qIds>)")
  List<DefaultCodeCachePopulationPojo> getDefaultCodeCachePojoByQIds(@BindList("qIds") List<Long> qIds);

  @RegisterBeanMapper(DefaultCodeCachePopulationPojo.class)
  @SqlQuery("SELECT * FROM default_code where q_id = :qId")
  DefaultCodeCachePopulationPojo getDefaultCodeCachePojoByQId(@Bind("qId") Long qId);

  @RegisterBeanMapper(DefaultWrapperCode.class)
  @SqlQuery("SELECT * FROM default_code where q_id = :qId and compiler_type = :compilerType")
  DefaultWrapperCode getDefaultCodeByQId(@Bind("qId") Long qId,@Bind("compilerType") CompilerTypeEnums compilerType);

  @GetGeneratedKeys("id")
  @SqlUpdate("INSERT INTO default_code(q_id, default_code,compiler_type) VALUES (:request.qid, :request.defaultCode,:request.compilerType)")
  Long saveDefaultCode(@BindBean("request") DefaultWrapperCode request);
}
