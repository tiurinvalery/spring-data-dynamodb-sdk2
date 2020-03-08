package com.tiurinvalery.springdata.sdk2.repository.impl;

import com.tiurinvalery.springdata.sdk2.entities.Item;
import com.tiurinvalery.springdata.sdk2.repository.DynamoDbCrudRepo;
import com.tiurinvalery.springdata.sdk2.service.ClassLoaderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.BatchGetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.BatchWriteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemResponse;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.KeysAndAttributes;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutRequest;
import software.amazon.awssdk.services.dynamodb.model.WriteRequest;

import java.lang.reflect.Field;
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
    private ClassLoaderServiceImpl classLoaderService;

    @Override
    public CompletableFuture<PutItemResponse> save(Object object) {
        Item targetItem = classLoaderService.getEntityItems().stream().filter(item -> item.getClazz().isInstance(object)).findFirst().orElse(null);

        if (null != targetItem) {

            List<Map<String, String>> collect = targetItem.getCodeAndDbFieldMapping().entrySet().stream()
                    .map(es -> {
                        try {
                            Field field = targetItem.getClazz().getDeclaredField(es.getKey());
                            field.setAccessible(true);
                            return Map.of(es.getValue(), field.get(object).toString());
                        } catch (NoSuchFieldException fieldException) {
                            throw new RuntimeException("Error with field mapping");
                        } catch (IllegalAccessException ilegalAccess) {
                            throw new RuntimeException("Trouble with field access");
                        }
                    })
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

    //
//
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
//
//    @Override
//    public Iterable saveAll(List<Object> entities) {
//        List<Item> itemList = entities.stream().map(entity -> classLoaderService.getEntityItems().stream().filter(item -> item.getClazz().isInstance(entity)).findFirst().orElse(null))
//                .filter(item -> null != item)
//                .collect(Collectors.toList());
//
//        for (Item targetItem : itemList) {
//            List<Map<String, String>> collect = targetItem.getCodeAndDbFieldMapping().entrySet().stream()
//                    .map(es -> {
//                        try {
//                            Field field = targetItem.getClazz().getDeclaredField(es.getKey());
//                            field.setAccessible(true);
//                            return Map.of(es.getValue(), field.get(object).toString());
//                        } catch (NoSuchFieldException fieldException) {
//                            throw new RuntimeException("Error with field mapping");
//                        } catch (IllegalAccessException ilegalAccess) {
//                            throw new RuntimeException("Trouble with field access");
//                        }
//                    })
//                    .collect(Collectors.toList());
//            Map<String, AttributeValue> requestMap = new HashMap<>();
//            for (Map<String, String> map : collect) {
//                map.forEach((key, value) -> requestMap.put(key, AttributeValue.builder().s(value).build()));
//            }
//        }
//        WriteRequest.builder()
//                .putRequest()
//
//        BatchGetItemRequest.builder()
//                .requestItems();
//        dynamoDbAsyncClient.batchWriteItem()
//    }
//
//    private void createBatchItemsMap(Object object, Map<String,KeysAndAttributes> ref) {
//        Item targetItem = classLoaderService.getEntityItems().stream().filter(item -> item.getClazz().isInstance(object)).findFirst().orElse(null);
//
//        if (null != targetItem) {
//
//            List<Map<String, String>> collect = targetItem.getCodeAndDbFieldMapping().entrySet().stream()
//                    .map(es -> {
//                        try {
//                            Field field = targetItem.getClazz().getDeclaredField(es.getKey());
//                            field.setAccessible(true);
//                            return Map.of(es.getValue(), field.get(object).toString());
//                        } catch (NoSuchFieldException fieldException) {
//                            throw new RuntimeException("Error with field mapping");
//                        } catch (IllegalAccessException ilegalAccess) {
//                            throw new RuntimeException("Trouble with field access");
//                        }
//                    })
//                    .collect(Collectors.toList());
//            Map<String, AttributeValue> requestMap = new HashMap<>();
//            for (Map<String, String> map : collect) {
//                map.forEach((key, value) -> requestMap.put(key, AttributeValue.builder().s(value).build()));
//            }
//
//            KeysAndAttributes.builder()
//                    .keys()
//            ref.put(targetItem.getTableName(), requestMap)
//    }

//
//
//
//    @Override
//    public Iterable findAll() {
//        return null;
//    }
//
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
//
//
//    @Override
//    public void deleteAll(Iterable iterable) {
//
//    }
//
}
