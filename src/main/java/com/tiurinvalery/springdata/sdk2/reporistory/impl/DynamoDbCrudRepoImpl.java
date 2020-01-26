package com.tiurinvalery.springdata.sdk2.reporistory.impl;

import com.tiurinvalery.springdata.sdk2.reporistory.DynamoDbCrudRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.BatchGetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.BatchGetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.BatchWriteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.BatchWriteItemResponse;
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest;
import software.amazon.awssdk.services.dynamodb.model.CreateTableResponse;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemResponse;
import software.amazon.awssdk.services.dynamodb.model.DeleteTableRequest;
import software.amazon.awssdk.services.dynamodb.model.DeleteTableResponse;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableResponse;
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement;
import software.amazon.awssdk.services.dynamodb.model.KeyType;
import software.amazon.awssdk.services.dynamodb.model.KeysAndAttributes;
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughput;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutRequest;
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType;
import software.amazon.awssdk.services.dynamodb.model.WriteRequest;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
public class DynamoDbCrudRepoImpl implements DynamoDbCrudRepo {

    @Autowired
    private DynamoDbAsyncClient dynamoDbAsyncClient;

//    @Override
//    public CompletableFuture<BatchWriteItemResponse> save(Object entity) {
//        List<WriteRequest> writeRequests = List.of(WriteRequest.builder()
//                .putRequest(PutRequest.builder()
//                        .item(Map.of("1", AttributeValue.builder()
//                                .s("2").build(), "2", AttributeValue.builder()
//                                .s("3").build()))
//                        .build())
//                .build());
//
//        BatchWriteItemRequest batchedWriteRequest = BatchWriteItemRequest.builder()
//                .requestItems(Map.of("tableName", writeRequests))
//                .build();
//        return dynamoDbAsyncClient.batchWriteItem(batchedWriteRequest);
//    }

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


//    @Override
//    public boolean existsById(Object o) {
//        return false;
//    }
//
//    @Override
//    public Object findAll() {
//        dynamoDbAsyncClient.
//        return null;
//    }

//    @Override
//    public Iterable findAllById(Iterable iterable) {
//        return null;
//    }
//
//    @Override
//    public long count() {
//        return 0;
//    }
//
//    @Override
//    public void deleteById(Object o) {
//
//    }
//
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
//
//    @Override
//    public void deleteAll(Iterable entities) {
//
//    }
//
//    @Override
//    public void deleteAll() {
//
//    }
}
