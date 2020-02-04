package com.tiurinvalery.springdata.sdk2.repository.impl;

import com.tiurinvalery.springdata.sdk2.repository.DynamoDbCrudRepo;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import software.amazon.awssdk.services.dynamodb.model.BatchGetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.CreateTableResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;

import java.util.concurrent.CompletableFuture;


@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
public class DynamoDbCrudRepoImplTest {

    @MockBean
    private DSLServiceImpl dslService;

    @Autowired
    DynamoDbCrudRepo dynamoDbCrudRepo;

    @Test
    public void putItemTest() throws Exception {
        CompletableFuture<CreateTableResponse> table = dslService.createTable();
        System.out.println(table.get().tableDescription().toString());
        try {
            CompletableFuture<PutItemResponse> putItemResponseCompletableFuture = dynamoDbCrudRepo.putItem();
            putItemResponseCompletableFuture.thenApply(result -> {
                System.out.println("HERE WE GO");
                System.out.println(result);
                return result;
            });
        } finally {
            dslService.deleteTable();
        }
    }

    @Test
    public void getItemTest() throws Exception {
        CompletableFuture<CreateTableResponse> table = dslService.createTable();
        System.out.println(table.get().tableDescription().toString());
        try {
            CompletableFuture<PutItemResponse> putItemResponseCompletableFuture = dynamoDbCrudRepo.putItem();
            putItemResponseCompletableFuture.thenApply(result -> {
                System.out.println("HERE WE GO");
                System.out.println(result);
                return result;
            });
            CompletableFuture<BatchGetItemResponse> byId = dynamoDbCrudRepo.findById(new Object(), true);
            System.out.println(byId.thenApply(result ->
            {
                System.out.println(result);
                return result;
            }));
        } finally {
            dslService.deleteTable();
        }
    }

//    @Test
//    public void deleteItemTest() throws Exception {
//        CompletableFuture<CreateTableResponse> table = dslService.createTable();
//        System.out.println(table.get().tableDescription().toString());
//        CompletableFuture<BatchWriteItemResponse> save = dslService.save(new Object());
//        save.whenComplete((saved, error) ->{
//            try {
//                if(saved!= null) {
//                    System.out.println(saved.toString());
//                } else {
//                    System.out.println(error.getStackTrace());
//                }
//            } catch (Exception ex) {
//                System.out.println(ex.getStackTrace());
//            }
//        });
//        CompletableFuture<BatchGetItemResponse> responseCompletableFuture = dynamoDbCrudRepo.findById(new Object(), true);
//        assertNotNull(responseCompletableFuture.get().responses());
//        CompletableFuture<BatchGetItemResponse> responseCompletableFuture1 = dynamoDbCrudRepo.findById(new Object(), true);
//
//        responseCompletableFuture1.whenComplete((items, error) ->
//        {
//          try {
//              if(items != null) {
//                  String value = items.getValueForField("1", String.class).orElse(null);
//                  System.out.println(value);
//                  assertNull(value);
//              } else {
//                  System.out.println(error.getStackTrace());
//              }
//          } catch (Exception ex) {
//              System.out.println(ex.getStackTrace());
//          }
//          finally {
//              dynamoDbCrudRepo.deleteTable();
//          }
//        });
//    }
}