package com.tiurinvalery.springdata.sdk2.repository;

import com.tiurinvalery.springdata.sdk2.parser.data.Attribute;
import com.tiurinvalery.springdata.sdk2.parser.data.KeyProperties;
import software.amazon.awssdk.services.dynamodb.model.CreateTableResponse;
import software.amazon.awssdk.services.dynamodb.model.DeleteTableResponse;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableResponse;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface DSLService {

    CreateTableResponse createTable(Collection<KeyProperties> keys, Collection<Attribute> attributes, String tableName);

    CompletableFuture<DeleteTableResponse> deleteTable(String tableName);

    CompletableFuture<DescribeTableResponse> describeTable(String tableName);
}
