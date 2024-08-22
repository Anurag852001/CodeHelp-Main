package com.video.CodeHelp.modules;


import com.github.benmanes.caffeine.cache.Cache;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.video.CodeHelp.Caffine.CaffineCacheFactory;
import com.video.CodeHelp.Config.CodeHelpConfig;
import com.video.CodeHelp.Dao.ConfigDao;
import com.video.CodeHelp.Dao.QuestionDao;
import com.video.CodeHelp.Enums.CacheTTLS;
import com.video.CodeHelp.Exception.CodeHelpException;
import com.video.CodeHelp.Service.CachingService;
import com.video.CodeHelp.Service.ConfigService;
import com.video.CodeHelp.Service.QuestionService;
import com.video.CodeHelp.Service.WelcomeService;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.ReplyFailure;
import io.vertx.core.file.FileSystem;
import io.vertx.core.shareddata.SharedData;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp2.BasicDataSource;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

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


  @Singleton
  @Provides
  public Jdbi provideJdbi() {
    try {
      BasicDataSource dataSource = new BasicDataSource();
      dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/codehelp");
      dataSource.setUsername(config.mySqlConfig.getUsername());
      dataSource.setPassword(config.mySqlConfig.getPassword());
      dataSource.setValidationQuery("SELECT 1");
      dataSource.setMaxIdle(20);
      dataSource.setMaxWaitMillis(10000);

      // Initialize Jdbi with the DataSource=
      Jdbi jdbi = Jdbi.create(dataSource);
      jdbi.installPlugin(new SqlObjectPlugin());

      jdbi.useHandle(handle -> {
        handle.execute("SELECT 1");
      });
      log.info("Success");
      return jdbi;
    } catch (Exception e) {
      log.error("Error while initializing Jdbi", e);
      throw new CodeHelpException(ReplyFailure.ERROR, "Error while initializing Jdbi");
    }
  }
  @Provides
  @Singleton
  public ConfigDao providesCodeHelpConfigDao(Jdbi jdbi){
    try{
      return jdbi.onDemand(ConfigDao.class);
    } catch (Exception e){
      log.error("Error while initializing CodeHelpConfigDao",e);
      throw new CodeHelpException(ReplyFailure.ERROR,"Error while initializing CodeHelpConfigDao");
    }
  }

  @Provides
  @Singleton
  public QuestionDao providesQuestionDao(Jdbi jdbi){
    try{
      return jdbi.onDemand(QuestionDao.class);
    } catch (Exception e){
      log.error("Error while initializing CodeHelpConfigDao",e);
      throw new CodeHelpException(ReplyFailure.ERROR,"Error while initializing CodeHelpConfigDao");
    }
  }

  @Provides
  @Singleton
  public QuestionService providesQuestionService(CachingService cachingService, QuestionDao questionDao){
    return new QuestionService(questionDao,cachingService);
  }

  @Singleton
  @Provides
  public ConfigService prividesCodeHelpConfigService(ConfigDao configDao){
    return new ConfigService(configDao);
  }

  @Singleton
  @Provides
  public Cache<String,Object> providedCaffineCacheInstance(){
    return CaffineCacheFactory.getCacheInstance(CacheTTLS.ONE_DAY_CACHE);
  }



}
