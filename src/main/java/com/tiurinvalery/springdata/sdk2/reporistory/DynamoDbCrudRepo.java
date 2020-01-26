package com.tiurinvalery.springdata.sdk2.reporistory;

import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.model.BatchGetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;

import java.util.concurrent.CompletableFuture;

@Repository
public interface DynamoDbCrudRepo  {

    CompletableFuture<BatchGetItemResponse> findById(Object o, Boolean consistentRead);

//    CompletableFuture<BatchWriteItemResponse> save(Object entity);

    CompletableFuture<DeleteItemResponse> delete();

    CompletableFuture<PutItemResponse> putItem();
}
