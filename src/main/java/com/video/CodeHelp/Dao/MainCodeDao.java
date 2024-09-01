package com.video.CodeHelp.Dao;

import com.video.CodeHelp.Service.CachePopulationService.pojo.DefaultCodeCachePopulationPojo;
import com.video.CodeHelp.Service.CachePopulationService.pojo.MainCodeCachePopulationPojo;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.List;

public interface MainCodeDao {

  @RegisterBeanMapper(MainCodeCachePopulationPojo.class)
  @SqlQuery("SELECT * FROM main_code where q_id in <qIds>")
  List<MainCodeCachePopulationPojo> getMainCodeCachePojoByQIds(@Bind("qIds") List<Long> qIds);

  @RegisterBeanMapper(MainCodeCachePopulationPojo.class)
  @SqlQuery("SELECT * FROM main_code where q_id = :qIds")
  DefaultCodeCachePopulationPojo getMainCodeCachePojoByQId(@Bind("qId") Long qId);
}
