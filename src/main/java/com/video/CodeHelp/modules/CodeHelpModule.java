package com.video.CodeHelp.modules;


import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.video.CodeHelp.Service.WelcomeService;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.file.FileSystem;
import io.vertx.core.shareddata.SharedData;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Singleton
@Slf4j
public class CodeHelpModule extends AbstractModule {

  private final Vertx vertx;

  public CodeHelpModule(Vertx vertx) {
    log.info("Starting the codeHelp module");
    this.vertx = vertx;
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

}
