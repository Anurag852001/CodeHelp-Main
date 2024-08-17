package com.video.CodeHelp.Dao;

import com.video.CodeHelp.Pojo.Config;
import com.video.CodeHelp.Pojo.SaveConfigRequest;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface ConfigDao {

  @GetGeneratedKeys
  @SqlUpdate("Insert into config(config_type,config_key,config_value,status)  values(:request.configType,:request.configKey,:request.configValue,:request.status)")
  Long saveConfig(@BindBean("request") SaveConfigRequest request);

  @RegisterBeanMapper(Config.class)
  @SqlQuery("Select * from config where config_type = :configType and config_key = :configKey")
  Config getConfig(@Bind("configKey") String configKey, @Bind("configType") String configType);
}
