package com.video.CodeHelp.modules;


import com.github.benmanes.caffeine.cache.Cache;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.video.CodeHelp.Caffine.CaffineCacheFactory;
import com.video.CodeHelp.Config.CodeHelpConfig;
import com.video.CodeHelp.Constants.DataConstants;
import com.video.CodeHelp.Dao.*;
import com.video.CodeHelp.Enums.CacheTTLS;
import com.video.CodeHelp.Exception.CodeHelpException;
import com.video.CodeHelp.Service.*;
import com.video.CodeHelp.Service.CachePopulationService.Handlers.DefaultCodeCachePopulationService;
import com.video.CodeHelp.Service.CachePopulationService.Handlers.MainCodeCachePopulationService;
import com.video.CodeHelp.Service.CachePopulationService.ICachePopulationService;
import com.video.CodeHelp.Service.CompilerService.*;
import com.video.CodeHelp.Service.CachePopulationService.WrapperCodeService.ICodeWrapperService;
import com.video.CodeHelp.Service.CachePopulationService.WrapperCodeService.WrapperFactory;
import com.video.CodeHelp.Service.CachePopulationService.WrapperCodeService.handlers.DefaultWrapperCodeService;
import com.video.CodeHelp.Service.CachePopulationService.WrapperCodeService.handlers.MainWrapperCodeService;
import com.video.CodeHelp.Service.ListingService.IListingService;
import com.video.CodeHelp.Service.ListingService.ListingFactory;
import com.video.CodeHelp.Service.ListingService.handlers.QuestionsListingService;
import com.video.CodeHelp.Service.TestCaseService.ITestCaseService;
import com.video.CodeHelp.Service.TestCaseService.impl.TestCaseService;
import com.video.CodeHelp.mongo.MongoService;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.ReplyFailure;
import io.vertx.core.file.FileSystem;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.SharedData;
import io.vertx.ext.mongo.MongoClient;
import jakarta.inject.Named;
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
    bind(CodeHelpConfig.class).toInstance(config);
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



  @Singleton
  @Provides
  @Named(DataConstants.JAVA_COMPILER_SERVICE)
  public ICompilerService providesJavaCompilerService(WrapperFactory wrapperFactory, ConfigService configService,MainCodeVariableService mainCodeVariableService,
                                                    ITestCaseService testCaseService){
    return new JavaCompilerService(wrapperFactory, configService,mainCodeVariableService,testCaseService);
  }

  @Singleton
  @Provides
  @Named(DataConstants.CPP_COMPILER_SERVICE)
  public ICompilerService providesCppCompilerService(){
    return new CppCompilerService();
  }

  @Singleton
  @Provides
  @Named(DataConstants.PYTHON_COMPILER_SERVICE)
  public ICompilerService providesPythonCompilerService(){
    return new PythonCompilerService();
  }

  @Singleton
  @Provides
  public CompilerFactory providesCompilerFactory(@Named(DataConstants.JAVA_COMPILER_SERVICE) ICompilerService javaCompilerService, @Named(DataConstants.CPP_COMPILER_SERVICE) ICompilerService cppCompilerService, @Named(DataConstants.PYTHON_COMPILER_SERVICE) ICompilerService pythonCompilerService){
    return new CompilerFactory(cppCompilerService,javaCompilerService, pythonCompilerService);
  }

  @Singleton
  @Provides
  public MainCodeDao provideMainCodeDao(Jdbi jdbi){
    try{
      return jdbi.onDemand(MainCodeDao.class);
    } catch (Exception e){
      log.error("Error while initializing MainCodeDao",e);
      throw new CodeHelpException(ReplyFailure.ERROR,"Error while initializing MainCodeDao");
    }
  }

  @Provides
  @Singleton
  public DefaultCodeDao provideDefaultCodeDao(Jdbi jdbi){
    try{
      return jdbi.onDemand(DefaultCodeDao.class);
    } catch (Exception e){
      log.error("Error while initializing DefaultCodeDao",e);
      throw new CodeHelpException(ReplyFailure.ERROR,"Error while initializing DefaultCodeDao");
    }
  }

  @Provides
  @Singleton
  @Named(DataConstants.DEFAULT_CODE_CACHE_POPULATION_SERVICE)
  public ICachePopulationService provideDefaultCachePopulationService(DefaultCodeDao dao){
    return new DefaultCodeCachePopulationService(dao);
  }

  @Provides
  @Singleton
  @Named(DataConstants.MAIN_CODE_CACHE_POPULATION_SERVICE)
  public ICachePopulationService providesMainCodeCachingService(MainCodeDao dao) {
    return new MainCodeCachePopulationService(dao);
  }

  @Provides
  @Singleton
  @Named(DataConstants.MAIN_CODE_WRAPPER_SERVICE)
  public ICodeWrapperService providesMainCodeWrapperService(EventBus eventBus,MainCodeDao mainCodeDao,CachingService cachingService,ConfigService configService) {
    return new MainWrapperCodeService(eventBus,mainCodeDao,cachingService,configService);
  }

  @Provides
  @Singleton
  @Named(DataConstants.DEFAULT_CODE_WRAPPER_SERVICE)
  public ICodeWrapperService providesDefaultCodeWrapperService(DefaultCodeDao defaultCodeDao,CachingService cachingService) {
    return new DefaultWrapperCodeService(defaultCodeDao,cachingService);
  }

  @Provides
  @Singleton
  public WrapperFactory providesWrapperFactory(@Named(DataConstants.DEFAULT_CODE_WRAPPER_SERVICE) ICodeWrapperService defaultCodeWrapperService, @Named(DataConstants.MAIN_CODE_WRAPPER_SERVICE) ICodeWrapperService mainCodeWrapperService) {
    return new WrapperFactory(defaultCodeWrapperService,mainCodeWrapperService);
  }

  @Provides
  @Singleton
  @Named(DataConstants.QUESTIONS_LISTING_SERVICE)
  public IListingService providesQuestionsListingService(QuestionDao dao) {
    return new QuestionsListingService(dao);
  }

  @Provides
  @Singleton
  public ListingFactory providesListingFactory(@Named(DataConstants.QUESTIONS_LISTING_SERVICE) IListingService questionsListingService){
    return new ListingFactory(questionsListingService);
  }

  @Provides
  @Singleton
  public MainCodeVariableService provideMainCodeVariableService(MainCodeVariablesDao dao,CachingService cachingService){
    return new MainCodeVariableService(dao,cachingService);
  }

  @Provides
  @Singleton
  public MainCodeVariablesDao provideMainCodeVariablesDao(Jdbi jdbi){
    try{
      return jdbi.onDemand(MainCodeVariablesDao.class);
    } catch (Exception e){
      log.error("Error while initializing MainCodeVariablesDao",e);
      throw new CodeHelpException(ReplyFailure.ERROR,"Error while initializing MainCodeVariablesDao");
    }
  }

  @Provides
  @Singleton
  public ITestCaseService providesTestCaseService(TestCaseDao testCaseDao,CachingService cachingService){
    return  new TestCaseService(testCaseDao,cachingService);
  }

  @Provides
  @Singleton
  public TestCaseDao providesTestCaseDao(Jdbi jdbi){
    try{
      return jdbi.onDemand(TestCaseDao.class);
    } catch (Exception e){
      log.error("Error while initializing TestCaseDao",e);
      throw new CodeHelpException(ReplyFailure.ERROR,"Error while initializing TestCaseDao");
    }
  }


  @Provides
  @Singleton
  public CorrectCodeDao providesCorrectCodeDao(Jdbi jdbi){
    try{
       return jdbi.onDemand(CorrectCodeDao.class);
    } catch (Exception e){
      log.error("Error while initializing CorrectCodeDao",e);
      throw new CodeHelpException(ReplyFailure.ERROR,"Error while initializing CorrectCodeDao");
    }
  }

  @Provides
  @Singleton
  public CorrectCodeService providesCorrectCodeService(CorrectCodeDao correctCodeDao,CachingService cachingService,CompilerFactory compilerFactory
                                                      ,ITestCaseService testCaseService){
    return new CorrectCodeService(correctCodeDao,cachingService,compilerFactory,testCaseService);
  }

  @Provides
  @Singleton
  public MongoClient providesMongoClient(CodeHelpConfig codeHelpConfig){
    try{
      return MongoClient.createShared(vertx, JsonObject.mapFrom(codeHelpConfig.getMongoConfig()));
    } catch (Exception e){
      log.error("Error while intializing mongo client",e);
      return null;
    }

  }
}
