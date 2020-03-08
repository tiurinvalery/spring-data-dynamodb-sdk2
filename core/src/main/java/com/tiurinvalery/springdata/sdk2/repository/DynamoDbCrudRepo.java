package com.tiurinvalery.springdata.sdk2.repository;

import software.amazon.awssdk.services.dynamodb.model.DeleteItemResponse;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;

import java.util.concurrent.CompletableFuture;

public interface DynamoDbCrudRepo {
    CompletableFuture<PutItemResponse> save(Object object);



    CompletableFuture<GetItemResponse> findById(Object objectWithIds);

    CompletableFuture<DeleteItemResponse> delete(Object objectWithIds);
}
