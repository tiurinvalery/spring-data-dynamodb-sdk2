package com.tiurinvalery.springdata.sdk2.repository.impl;

import com.tiurinvalery.springdata.sdk2.repository.DSLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest;
import software.amazon.awssdk.services.dynamodb.model.CreateTableResponse;
import software.amazon.awssdk.services.dynamodb.model.DeleteTableRequest;
import software.amazon.awssdk.services.dynamodb.model.DeleteTableResponse;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableResponse;
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement;
import software.amazon.awssdk.services.dynamodb.model.KeyType;
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughput;
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class DSLServiceImpl implements DSLService {

    @Autowired
    private DynamoDbAsyncClient dynamoDbAsyncClient;

    @Override
    public CompletableFuture<CreateTableResponse> createTable() {
        CreateTableRequest request = CreateTableRequest.builder()
                .attributeDefinitions(List.of(AttributeDefinition.builder()
                                .attributeName("1")
                                .attributeType(ScalarAttributeType.S)
                                .build()
                ))
                .keySchema(KeySchemaElement.builder()
                                .attributeName("1")
                                .keyType(KeyType.HASH)
                                .build())

                .provisionedThroughput(ProvisionedThroughput.builder()
                        .readCapacityUnits(new Long(10))
                        .writeCapacityUnits(new Long(10))
                        .build())
                .tableName("tableName")
                .build();

        return dynamoDbAsyncClient.createTable(request);
    }

    @Override
    public CompletableFuture<DeleteTableResponse> deleteTable() {
        return dynamoDbAsyncClient.deleteTable(DeleteTableRequest.builder()
                .tableName("tableName")
                .build());
    }

    @Override
    public CompletableFuture<DescribeTableResponse> describeTable() {
        return dynamoDbAsyncClient.describeTable(DescribeTableRequest.builder()
                .tableName("tableName")
                .build());
    }
}
