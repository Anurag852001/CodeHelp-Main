package com.video.CodeHelp.Dao;

import com.video.CodeHelp.Pojo.SaveCodeHelpConfigRequest;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

public interface CodeHelpConfigDao {


  @SqlQuery("Insert into config config_type =:request.configType,config_key = request.config_key,config_value = request.config_value, status = request.status")
  public Integer saveCodeHelpConfig(@BindBean("request") SaveCodeHelpConfigRequest request);

}
