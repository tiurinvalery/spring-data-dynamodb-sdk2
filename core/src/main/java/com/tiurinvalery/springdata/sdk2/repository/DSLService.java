package com.tiurinvalery.springdata.sdk2.repository;

import software.amazon.awssdk.services.dynamodb.model.CreateTableResponse;
import software.amazon.awssdk.services.dynamodb.model.DeleteTableResponse;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableResponse;

import java.util.concurrent.CompletableFuture;

public interface DSLService {

    CompletableFuture<CreateTableResponse> createTable();

    CompletableFuture<DeleteTableResponse> deleteTable();

    CompletableFuture<DescribeTableResponse> describeTable();
}
