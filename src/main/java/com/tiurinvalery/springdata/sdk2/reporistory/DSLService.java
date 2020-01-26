package com.tiurinvalery.springdata.sdk2.reporistory;

import software.amazon.awssdk.services.dynamodb.model.CreateTableResponse;
import software.amazon.awssdk.services.dynamodb.model.DeleteTableResponse;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableResponse;

import java.util.concurrent.CompletableFuture;

public interface DSLService {

    CompletableFuture<CreateTableResponse> createTable();

    CompletableFuture<DeleteTableResponse> deleteTable();

    CompletableFuture<DescribeTableResponse> describeTable();
}
