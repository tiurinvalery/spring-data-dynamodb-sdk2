package com.tiurinvalery.springdata.sdk2.repository.impl;

import com.tiurinvalery.springdata.sdk2.parser.data.Attribute;
import com.tiurinvalery.springdata.sdk2.parser.data.KeyProperties;
import com.tiurinvalery.springdata.sdk2.repository.DSLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest;
import software.amazon.awssdk.services.dynamodb.model.CreateTableResponse;
import software.amazon.awssdk.services.dynamodb.model.DeleteTableRequest;
import software.amazon.awssdk.services.dynamodb.model.DeleteTableResponse;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableResponse;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement;
import software.amazon.awssdk.services.dynamodb.model.KeyType;
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughput;
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class DSLServiceImpl implements DSLService {

    @Autowired
    private DynamoDbAsyncClient dynamoDbAsyncClient;

    @Autowired
    private DynamoDbClient dynamoDbClient;
    @Override
    public CreateTableResponse createTable(Collection<KeyProperties> keys, Collection<Attribute> attributes, String tableName) {

//        List<AttributeDefinition> tableAttributes = attributes.stream()
//                .map(attribute ->
//                        AttributeDefinition.builder()
//                                .attributeName(attribute.getName())
//                                .attributeType(ScalarAttributeType.S)
//                                .build())
//                .collect(Collectors.toList());
//
//        List<KeySchemaElement> tableKeys = keys.stream()
//                .map(key -> KeySchemaElement.builder()
//                        .attributeName(key.getFieldName())
//                        .keyType(key.getKeyType())
//                        .build())
//                .collect(Collectors.toList());
//        CreateTableRequest request = CreateTableRequest.builder()
//                .attributeDefinitions(tableAttributes)
//                .keySchema(tableKeys)
//                .tableName(tableName)
//                .build();
//
//        return dynamoDbAsyncClient.createTable(request);

        CreateTableRequest request = CreateTableRequest.builder()
                .attributeDefinitions(AttributeDefinition.builder()
                        .attributeName("USER_ID")
                        .attributeType(ScalarAttributeType.S)
                        .build())
                .keySchema(KeySchemaElement.builder()
                        .attributeName("USER_ID")
                        .keyType(KeyType.HASH)
                        .build())
                .provisionedThroughput(ProvisionedThroughput.builder()
                        .readCapacityUnits(new Long(10))
                        .writeCapacityUnits(new Long(10))
                        .build())
                .tableName("table_name12110")
                .build();
        try {
            return dynamoDbClient.createTable(request);
        } catch (DynamoDbException e) {
            throw e;
        }
    }

    @Override
    public CompletableFuture<DeleteTableResponse> deleteTable(String tableName) {
        return dynamoDbAsyncClient.deleteTable(DeleteTableRequest.builder()
                .tableName(tableName)
                .build());
    }

    @Override
    public CompletableFuture<DescribeTableResponse> describeTable(String tableName) {
        return dynamoDbAsyncClient.describeTable(DescribeTableRequest.builder()
                .tableName(tableName)
                .build());
    }
}
