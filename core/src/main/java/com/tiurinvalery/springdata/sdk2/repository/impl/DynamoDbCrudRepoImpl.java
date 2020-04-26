package com.tiurinvalery.springdata.sdk2.repository.impl;

import com.tiurinvalery.springdata.sdk2.entities.Index;
import com.tiurinvalery.springdata.sdk2.entities.Item;
import com.tiurinvalery.springdata.sdk2.repository.DynamoDbCrudRepo;
import com.tiurinvalery.springdata.sdk2.service.TargetComponentsLoaderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ComparisonOperator;
import software.amazon.awssdk.services.dynamodb.model.Condition;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemResponse;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
public class DynamoDbCrudRepoImpl implements DynamoDbCrudRepo {

    @Autowired
    private DynamoDbAsyncClient dynamoDbAsyncClient;

    @Autowired
    private TargetComponentsLoaderServiceImpl classLoaderService;

    @Override
    public CompletableFuture<PutItemResponse> save(Object object) {
        Item targetItem = classLoaderService.getEntityItems().stream().filter(item -> item.getClazz().isInstance(object)).findFirst().orElse(null);

        if (null != targetItem) {

            List<Map<String, String>> collect = targetItem.getCodeAndDbFieldMapping().entrySet().stream()
                    .map(es -> {
                        try {
                            Field field = targetItem.getClazz().getDeclaredField(es.getKey());
                            field.setAccessible(true);
                            Object value = field.get(object);
                            if (null != value) {
                                return Map.of(es.getValue(), value.toString());
                            } else {
                                return Map.of(es.getValue(), "NOLL");
                            }
                        } catch (NoSuchFieldException fieldException) {
                            throw new RuntimeException("Error with field mapping");
                        } catch (IllegalAccessException ilegalAccess) {
                            throw new RuntimeException("Trouble with field access");
                        }
                    })
                    .filter(this::deleteEmptyValues)
                    .collect(Collectors.toList());
            Map<String, AttributeValue> requestMap = new HashMap<>();
            for (Map<String, String> map : collect) {
                map.forEach((key, value) -> requestMap.put(key, AttributeValue.builder().s(value).build()));
            }
            PutItemRequest itemRequest = PutItemRequest.builder()
                    .tableName(targetItem.getTableName())
                    .item(requestMap)
                    .build();

            return dynamoDbAsyncClient.putItem(itemRequest);
        } else {
            throw new RuntimeException("Unknown entity");
        }
    }

    @Override
    public CompletableFuture<GetItemResponse> findById(Object objectWithIds) {
        Item targetItem = classLoaderService.getEntityItems().stream().filter(item -> item.getClazz().isInstance(objectWithIds)).findFirst().orElse(null);
        if (null != targetItem) {
            Map<String, AttributeValue> keyToGet = new HashMap<>();
            targetItem.getKeys().forEach((key, value) -> {
                try {
                    Field declaredField = objectWithIds.getClass().getDeclaredField(key);
                    declaredField.setAccessible(true);
                    keyToGet.put(value.getFieldName(), AttributeValue.builder().s(declaredField.get(objectWithIds).toString()).build());
                } catch (NoSuchFieldException noFieldException) {
                    throw new RuntimeException("Problem with field pasrsing on get");
                } catch (IllegalAccessException ex) {
                    throw new RuntimeException("Can't access field on get");
                }
            });
            CompletableFuture<GetItemResponse> itemsFuture = dynamoDbAsyncClient.getItem(GetItemRequest.builder()
                    .tableName(targetItem.getTableName())
                    .key(keyToGet)
                    .build());
            return itemsFuture;
        }
        throw new RuntimeException("Class for getItem not found");
    }

    public CompletableFuture<QueryResponse> findBySecondaryIndexes(Object objectWithIndex) {
        Item item = classLoaderService.getEntityItems().stream().filter(item1 -> item1.getClazz().isInstance(objectWithIndex)).findFirst().orElse(null);
        if (null != item) {
            List<Index> indexes = item.getIndexes();

            String secondaryIndexName = null;
            Map<String, List<AttributeValue>> dbAttributeNameToValRestrictions = new HashMap<>();

            for (int i = 0; i < indexes.size(); i++) {
                Index index = indexes.get(i);
                try {
                    Field declaredField = objectWithIndex.getClass().getDeclaredField(index.getFieldName());
                    declaredField.setAccessible(true);
                    Object objVal = declaredField.get(objectWithIndex);
                    if (null != objVal) {
                        String secondaryIndexNameNext = index.getSecondaryIndexName();
                        if (secondaryIndexName == null) {
                            secondaryIndexName = secondaryIndexNameNext;
                        } else if (secondaryIndexName != null && !secondaryIndexName.equals(secondaryIndexNameNext)) {
                            throw new RuntimeException("This is impossible to search base on more than 1 index at the same time");
                        }

                        if (objVal instanceof Collection) {
                            List values = (List) objVal;
                            List<AttributeValue> vals = new ArrayList<>();
                            values.forEach(
                                    v -> vals.add(AttributeValue.builder()
                                            .s((String) v)
                                            .build()));
                            dbAttributeNameToValRestrictions.put(index.getDbAttributeName(), vals);
                        } else {
                            dbAttributeNameToValRestrictions.put(index.getDbAttributeName(), List.of(AttributeValue.builder()
                                    .s((String) objVal)
                                    .build()));
                        }

                    }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException("Troubles with field parsing via findBySecondaryIndexes");
                }
            }
            Map<String, Condition> conditions = new HashMap<>();
//            Map<String,String> expressionAttributeNames = new HashMap<>();
//            Map<String,String> expressionAttributeValues = new HashMap<>();

            dbAttributeNameToValRestrictions.forEach((key, value) -> {

                conditions.put(key, Condition.builder()
                        .attributeValueList(value)
                        .comparisonOperator(ComparisonOperator.EQ)
                        .build());

//                expressionAttributeNames.put("#" + key, key);
            });


            QueryRequest queryRequest = QueryRequest.builder()
                    .tableName(item.getTableName())
                    .indexName(secondaryIndexName)
                    .keyConditions(conditions)
//                    .expressionAttributeNames(expressionAttributeNames)
//                    .expressionAttributeValues()
                    .build();

            return dynamoDbAsyncClient.query(queryRequest);
        }
        throw new RuntimeException("Can't find metadata for provided object, looks like it not in provided for scan package.");
    }

    @Override
    public CompletableFuture<DeleteItemResponse> delete(Object objectWithIds) {
        Item targetItem = classLoaderService.getEntityItems().stream().filter(item -> item.getClazz().isInstance(objectWithIds)).findFirst().orElse(null);
        if (null != targetItem) {
            Map<String, AttributeValue> keyToDelete = new HashMap<>();
            targetItem.getKeys().forEach((key, value) -> {
                try {
                    Field declaredField = objectWithIds.getClass().getDeclaredField(key);
                    declaredField.setAccessible(true);
                    keyToDelete.put(value.getFieldName(), AttributeValue.builder().s(declaredField.get(objectWithIds).toString()).build());
                } catch (NoSuchFieldException noFieldException) {
                    throw new RuntimeException("Problem with field pasrsing on get");
                } catch (IllegalAccessException ex) {
                    throw new RuntimeException("Can't access field on get");
                }
            });
            return dynamoDbAsyncClient.deleteItem(DeleteItemRequest.builder()
                    .tableName(targetItem.getTableName())
                    .key(keyToDelete)
                    .build());
        }
        throw new RuntimeException("Class for getItem not found");
    }

    private boolean deleteEmptyValues(Map<String, String> originMap) {
        for (Map.Entry entry : originMap.entrySet()) {
            if (!"NOLL".equals(entry.getValue())) {
                return true;
            }
        }
        return false;
    }
}
