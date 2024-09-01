package com.video.CodeHelp.Dao;

import com.video.CodeHelp.Service.CachePopulationService.pojo.DefaultCodeCachePopulationPojo;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.List;

public interface DefaultCodeDao {

  @RegisterBeanMapper(DefaultCodeCachePopulationPojo.class)
  @SqlQuery("SELECT * FROM default_code where q_id in <qIds>")
  List<DefaultCodeCachePopulationPojo> getDefaultCodeCachePojoByQIds(@Bind("qIds") List<Long> qIds);

  @RegisterBeanMapper(DefaultCodeCachePopulationPojo.class)
  @SqlQuery("SELECT * FROM default_code where q_id = :qIds")
  DefaultCodeCachePopulationPojo getDefaultCodeCachePojoByQId(@Bind("qId") Long qId);
}
