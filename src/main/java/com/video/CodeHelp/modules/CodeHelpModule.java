package com.video.CodeHelp.modules;


import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.video.CodeHelp.Service.WelcomeService;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.file.FileSystem;
import io.vertx.core.shareddata.SharedData;
import jakarta.inject.Singleton;
import jdk.jfr.Percentage;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;

import javax.sql.DataSource;
import java.util.Properties;

@Singleton
@Slf4j
public class CodeHelpModule extends AbstractModule {

  private final Vertx vertx;

  public CodeHelpModule(Vertx vertx) {
    log.info("Starting the codeHelp module");
    this.vertx = vertx;
    provideJdbi();
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
  public Jdbi provideJdbi(){
    Properties properties = new Properties();
    properties.setProperty("username","root");
    properties.setProperty("password","12345678");
    Jdbi jdbi = Jdbi.create("jdbc:localhost:3306",properties);
    log.info("Connecting to db was successful");
    return jdbi;
  }
}
