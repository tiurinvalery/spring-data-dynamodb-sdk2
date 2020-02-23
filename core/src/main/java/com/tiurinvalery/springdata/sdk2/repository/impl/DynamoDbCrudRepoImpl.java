package com.tiurinvalery.springdata.sdk2.repository.impl;

import com.tiurinvalery.springdata.sdk2.entities.Item;
import com.tiurinvalery.springdata.sdk2.repository.DynamoDbCrudRepo;
import com.tiurinvalery.springdata.sdk2.service.ClassLoaderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;

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
    private DynamoDbClient dynamoDbClient;

    @Autowired
    private ClassLoaderServiceImpl classLoaderService;

    @Override
    public CompletableFuture<PutItemResponse> putItem(Object object) {
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

//    @Override
//    public Collection<AttributeValue> findById(String hashId) {
//
//        Map<String, AttributeValue> keyToGet =
//                new HashMap<>();
//        keyToGet.put("USER_ID", AttributeValue.builder().s(hashId).build());
//        DescribeTableResponse table_name = dynamoDbAsyncClient.describeTable(DescribeTableRequest.builder()
//                .tableName("table_name")
//                .build()).join();
//        dynamoDbAsyncClient.putItem(PutItemRequest.builder()
//                .tableName("table_name")
//                .item(Map.of("USER_ID", AttributeValue.builder().s("1").build()))
//                .build());
//        CompletableFuture<GetItemResponse> itemsFuture = dynamoDbAsyncClient.getItem(GetItemRequest.builder()
//                .tableName("table_name")
//                .key(keyToGet)
//                .build());
//        return itemsFuture.join().item().values();
//    }
//
//
//    @Override
//    public CompletableFuture<DeleteItemResponse> delete() {
//
//        Map<String, AttributeValue> stringAttributeValueMap = Map.of("1", AttributeValue.builder()
//                .s("2").build(), "2", AttributeValue.builder()
//                .s("3").build());
//
//        DeleteItemRequest deleteItemRequest = DeleteItemRequest.builder()
//                .tableName("tableName")
//                .key(stringAttributeValueMap)
//                .build();
//        return dynamoDbAsyncClient.deleteItem(deleteItemRequest);
//    }
//
//    @Override
//    public Object save(Object o) {
//        return null;
//    }
//
//    @Override
//    public Iterable saveAll(Iterable iterable) {
//        return null;
//    }
//
//    @Override
//    public Optional findById(Object o) {
//        return Optional.empty();
//    }
//
//    @Override
//    public boolean existsById(Object o) {
//        return false;
//    }
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
//    @Override
//    public void deleteById(Object o) {
//
//    }
//
//    @Override
//    public void delete(Object o) {
//
//    }
//
//    @Override
//    public void deleteAll(Iterable iterable) {
//
//    }
//
//    @Override
//    public void deleteAll() {
//
//    }
}
