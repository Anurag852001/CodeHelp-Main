package com.video.CodeHelp.mongo;

import com.video.CodeHelp.Constants.MongoConstants;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Singleton
public class MongoService {

    private final MongoClient mongoClient;

    @Inject
    public MongoService(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    public void insertDoc(String collectionName,JsonObject document){
        try {
            log.info("Going to insert document into collection:{},");
            mongoClient.insert(MongoConstants.CODE_HELP_DAY_WISE_QUESTIONS_TRACKING_COLLECTION, document,handler->{
                if(handler.succeeded()){
                    log.info("Inserted successfully document into collection:{}",collectionName);
                } else {
                    log.error("Error occured while inserting into mongo collection: {}, cause :{}",collectionName,handler.cause());
                }
            });
        } catch ( Exception e){
            log.error("Error while inserting into mongo collection",e);
        }
    }
}
