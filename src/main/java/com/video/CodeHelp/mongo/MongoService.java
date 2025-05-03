package com.video.CodeHelp.mongo;

import com.video.CodeHelp.Constants.DataConstants;
import com.video.CodeHelp.Constants.MongoConstants;
import com.video.CodeHelp.Enums.ApplicationErrorEnums;
import com.video.CodeHelp.Exception.CodeHelpException;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.BulkOperation;
import io.vertx.ext.mongo.BulkOperationType;
import io.vertx.ext.mongo.MongoClient;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Singleton
public class MongoService {
    private static final String TYPE = "type";
    private static final String MULTI = "multi";
    private static final String FILTER = "filter";
    private static final String DOCUMENT = "document";
    private final MongoClient mongoClient;

    @Inject
    public MongoService(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
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
        try {
            CompletableFuture<List<JsonObject>> future = new CompletableFuture<>();
            mongoClient.find(collectionName, filterObject, res -> {
                if (res.succeeded()) {
                    log.info("Result:{}",res.result());
                    future.complete(res.result());
                } else {
                    log.error("Mongo error for filter: {}", filterObject, res.cause());
                    future.complete(new ArrayList<>());
                }
            });
            return future.get(15, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Error while getting data from mongo for filter object: {}", filterObject, e);
            throw new CodeHelpException(ApplicationErrorEnums.MONGO_ERROR);
        }
    }



    private List<BulkOperation> convertDocumentsToBulkOperation(List<JsonObject> documents) {
        return documents.stream().filter(Objects::nonNull).map(BulkOperation::createInsert
        ).collect(Collectors.toList());
    }
}
