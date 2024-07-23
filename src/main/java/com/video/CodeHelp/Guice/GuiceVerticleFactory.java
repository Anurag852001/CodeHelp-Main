package com.video.CodeHelp.Guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.vertx.core.Promise;
import io.vertx.core.Verticle;
import io.vertx.core.spi.VerticleFactory;

import java.util.concurrent.Callable;

public class GuiceVerticleFactory implements VerticleFactory {
  public static final String PREFIX = "java-guice";
  public  Injector injector;

  public GuiceVerticleFactory(Injector injector) {
    this.injector = injector;
  }

  @Override
  public String prefix() {
    return PREFIX;
  }

  public Injector getInjector() {
    if(injector == null){
      injector = Guice.createInjector();
    }
    return injector;
  }

  @Override
  public void createVerticle(String verticleName, ClassLoader classLoader, Promise<Callable<Verticle>> promise) {
    // Assume verticleName is in the format "guice:com.example.MyVerticle"
    String className = verticleName.substring(6); // Remove "guice:" prefix

    try {
      // Load the verticle class
      Class<?> verticleClass = classLoader.loadClass(className);

      // Create a Callable for instantiating the verticle
      Callable<Verticle> verticleCallable = () -> {
        try {
          // Create an instance of the verticle using Guice
          return (Verticle) injector.getInstance(verticleClass);
        } catch (Exception e) {
          throw new RuntimeException("Failed to create verticle instance", e);
        }
      };

      // Complete the promise with the verticle Callable
      promise.complete(verticleCallable);
    } catch (ClassNotFoundException e) {
      promise.fail("Verticle class not found: " + className);
    }
  }

}
