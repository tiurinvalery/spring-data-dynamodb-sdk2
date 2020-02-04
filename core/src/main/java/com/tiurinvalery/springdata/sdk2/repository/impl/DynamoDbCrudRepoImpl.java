package com.tiurinvalery.springdata.sdk2.repository.impl;

import com.tiurinvalery.springdata.sdk2.repository.DynamoDbCrudRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.BatchGetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.BatchGetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemResponse;
import software.amazon.awssdk.services.dynamodb.model.KeysAndAttributes;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
public class DynamoDbCrudRepoImpl implements DynamoDbCrudRepo {

    @Autowired
    private DynamoDbAsyncClient dynamoDbAsyncClient;

    @Override
    public CompletableFuture<PutItemResponse> putItem() {
        Map<String, AttributeValue> item = Map.of("1", AttributeValue.builder()
                .s("2").build(), "2", AttributeValue.builder()
                .s("3").build());
        PutItemRequest itemRequest = PutItemRequest.builder()
                .tableName("tableName")
                .item(item)
                .build();
         return dynamoDbAsyncClient.putItem(itemRequest);
    }

    @Override
    public CompletableFuture<BatchGetItemResponse> findById(Object o, Boolean consistentRead) {
        String[] fields = {"a", "b"};

        KeysAndAttributes keysAndAttributes = KeysAndAttributes.builder()
                .consistentRead(consistentRead)
                .attributesToGet(fields)
                .keys(Map.of("1", AttributeValue.builder()
                        .s("2").build(), "2", AttributeValue.builder()
                        .s("3").build()))
                .build();
        BatchGetItemRequest getItemRequest = BatchGetItemRequest.builder()
                .requestItems(Map.of("tableName", keysAndAttributes))
                .build();
        return dynamoDbAsyncClient.batchGetItem(getItemRequest);
    }


    @Override
    public CompletableFuture<DeleteItemResponse> delete() {

        Map<String, AttributeValue> stringAttributeValueMap = Map.of("1", AttributeValue.builder()
                .s("2").build(), "2", AttributeValue.builder()
                .s("3").build());

        DeleteItemRequest deleteItemRequest = DeleteItemRequest.builder()
                .tableName("tableName")
                .key(stringAttributeValueMap)
                .build();
         return dynamoDbAsyncClient.deleteItem(deleteItemRequest);
    }
}
