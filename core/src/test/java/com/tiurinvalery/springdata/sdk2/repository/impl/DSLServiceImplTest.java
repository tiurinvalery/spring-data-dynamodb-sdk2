package com.tiurinvalery.springdata.sdk2.repository.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import software.amazon.awssdk.services.dynamodb.model.CreateTableResponse;
import software.amazon.awssdk.services.dynamodb.model.DeleteTableResponse;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableResponse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DSLServiceImplTest {

    @Autowired
    DSLServiceImpl dslService;

    @Test
    public void createTableTest() throws Exception {
        CompletableFuture<CreateTableResponse> table = dslService.createTable();
        System.out.println(table.get().tableDescription().toString());
        assertNotNull(table.get().tableDescription().toString());
        dslService.deleteTable();
    }

    @Test(expected = ExecutionException.class)
    public void deleteTableTest() throws Exception {
        CompletableFuture<DeleteTableResponse> deleteTableResponseCompletableFuture = dslService.deleteTable();
        System.out.println(deleteTableResponseCompletableFuture.get().toString());
        CompletableFuture<DescribeTableResponse> describeTableResponseCompletableFuture = dslService.describeTable();
        System.out.println(describeTableResponseCompletableFuture.get().toString());
    }

}