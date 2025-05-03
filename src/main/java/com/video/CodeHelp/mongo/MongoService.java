package com.video.CodeHelp.mongo;

import com.video.CodeHelp.Constants.MongoConstants;
import com.video.CodeHelp.Enums.PoolEnums;
import com.video.CodeHelp.Service.CommonPoolFactory;
import io.vertx.core.*;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.BulkOperation;
import io.vertx.ext.mongo.MongoClient;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@Singleton
public class MongoService {
    private static final String TYPE = "type";
    private static final String MULTI = "multi";
    private static final String FILTER = "filter";
    private static final String DOCUMENT = "document";
    private final MongoClient mongoClient;
    private final Vertx vertx;

    @Inject
    public MongoService(MongoClient mongoClient,Vertx vertx) {
        this.mongoClient = mongoClient;
        this.vertx = vertx;
    }

    public void insertDoc(String collectionName,JsonObject document){
        try {
            log.info("Going to insert document into collection:{},",collectionName);
            mongoClient.insert(MongoConstants.CODE_HELP_DAY_WISE_QUESTIONS_TRACKING_COLLECTION, document,handler->{
                if(handler.succeeded()){
                    log.info("Inserted successfully document into collection:{}",collectionName);
                } else {
                    log.error("Error occurred while inserting into mongo collection: {}, cause :",collectionName,handler.cause());
                }
            });
        } catch ( Exception e){
            log.error("Error while inserting into mongo collection",e);
        }
    }

    public void insertMultiple(String collectionName, List<JsonObject> documents){
        try{
            List<BulkOperation> bulkOperations = convertDocumentsToBulkOperation(documents);
            long startTime = System.currentTimeMillis();
            mongoClient.bulkWrite(collectionName,bulkOperations).onComplete(result->{
                if(result.succeeded()){
                    log.info("Successfully inserted bulk documents into collection:{},number:{}",collectionName,documents.size());
                } else{
                    log.error("Error occurred while inserting into collections:{},cause:",collectionName,result.cause());
                }
            });
            log.info("Time taken to insert into mongo:{}",System.currentTimeMillis()-startTime);
        } catch (Exception e){
            log.error("Error while inserting multiple documents into mongo for collection:{}",collectionName);
        }
    }

    public List<JsonObject> getMultiple(String collectionName, JsonObject filterObject) {
        Promise<List<JsonObject>> promise = Promise.promise();
        CompletableFuture.runAsync(()->
        mongoClient.find(collectionName, filterObject, res -> {
            if (res.succeeded()) {
                List<JsonObject> result = res.result() != null ? res.result() : new ArrayList<>();
                promise.complete(result);
            } else {
                log.error("Mongo error", res.cause());
                promise.complete(new ArrayList<>());
            }
        }), CommonPoolFactory.getForkJoinPool(PoolEnums.MONGO_POOL));
        log.info("Here");
        return promise.future().toCompletionStage().toCompletableFuture().join();  // This will block
    }


    private List<BulkOperation> convertDocumentsToBulkOperation(List<JsonObject> documents) {
        return documents.stream().filter(Objects::nonNull).map(BulkOperation::createInsert
        ).collect(Collectors.toList());
    }
}
