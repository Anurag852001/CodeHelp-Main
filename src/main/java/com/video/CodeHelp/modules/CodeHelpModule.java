package com.video.CodeHelp.modules;


import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.video.CodeHelp.Config.CodeHelpConfig;
import com.video.CodeHelp.Dao.CodeHelpConfigDao;
import com.video.CodeHelp.Exception.CodeHelpException;
import com.video.CodeHelp.Service.CodeHelpConfigService;
import com.video.CodeHelp.Service.WelcomeService;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.ReplyFailure;
import io.vertx.core.file.FileSystem;
import io.vertx.core.shareddata.SharedData;
import jakarta.inject.Singleton;
import jdk.jfr.Percentage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp2.BasicDataSource;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.extension.Extensions;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import javax.sql.DataSource;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.util.Properties;

@Singleton
@Slf4j
public class CodeHelpModule extends AbstractModule {

  private final Vertx vertx;
  private final CodeHelpConfig config;

  public CodeHelpModule(Vertx vertx,CodeHelpConfig config) {
    log.info("Starting the codeHelp module");
    this.vertx = vertx;
    this.config = config;
  }

  @Override
  protected void configure() {
    bind(Vertx.class).toInstance(this.vertx);
    bind(EventBus.class).toInstance(this.vertx.eventBus());
    bind(FileSystem.class).toInstance(this.vertx.fileSystem());
    bind(SharedData.class).toInstance(this.vertx.sharedData());
  }

  @Singleton
  @Provides
  public WelcomeService providesWelcomeService(){
    return new WelcomeService();
  }

  @Provides
  @Singleton
  public CodeHelpConfigDao providesCodeHelpConfigDao(Jdbi jdbi){
    try{
      return jdbi.onDemand(CodeHelpConfigDao.class);
    } catch (Exception e){
      log.error("Error while initializing CodeHelpConfigDao",e);
      throw new CodeHelpException(ReplyFailure.ERROR,"Error while initializing CodeHelpConfigDao");
    }
  }

  @Singleton
  @Provides
  public CodeHelpConfigService prividesCodeHelpConfigService(CodeHelpConfigDao configDao){
    return new CodeHelpConfigService(configDao);
  }



  @Singleton
  @Provides
  public Jdbi provideJdbi() {
    try {
      // Configure BasicDataSource
      BasicDataSource dataSource = new BasicDataSource();
      dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
      dataSource.setUrl("jdbc:mysql://localhost:3306/codehelp");
      dataSource.setUsername(config.mySqlConfig.getUsername());
      dataSource.setPassword(config.mySqlConfig.getPassword());
      dataSource.setDefaultAutoCommit(false);
      dataSource.setDefaultTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
      dataSource.setMinIdle(5);
      dataSource.setMaxIdle(20);
      dataSource.setMaxWaitMillis(10000);

      // Initialize Jdbi with the DataSource=
      Jdbi jdbi = Jdbi.create(dataSource);
      jdbi.installPlugin(new SqlObjectPlugin());
      jdbi.open().execute("Select * from config");
      return jdbi;
    } catch (Exception e) {
      log.error("Error while initializing Jdbi", e);
      throw new CodeHelpException(ReplyFailure.ERROR, "Error while initializing Jdbi");
    }
  }

}
