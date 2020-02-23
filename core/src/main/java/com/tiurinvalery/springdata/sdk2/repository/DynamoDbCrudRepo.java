package com.tiurinvalery.springdata.sdk2.repository;

import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;

import java.util.concurrent.CompletableFuture;

public interface DynamoDbCrudRepo {
    CompletableFuture<PutItemResponse> putItem(Object object);
}
