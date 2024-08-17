package com.video.CodeHelp.Dao;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

public interface CodeHelpConfigDao  {

  @SqlQuery("SELECT count(*) FROM config")
  int findById();
}
