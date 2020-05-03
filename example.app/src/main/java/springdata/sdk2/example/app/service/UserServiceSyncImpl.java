package springdata.sdk2.example.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ComparisonOperator;
import software.amazon.awssdk.services.dynamodb.model.Condition;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceSyncImpl {

    @Autowired
    private DynamoDbClient dynamoDbClient;

    public PutItemResponse saveUserSyncClient(String tableName, Map<String, AttributeValue> fields) {
        return dynamoDbClient.putItem(PutItemRequest.builder()
                .tableName(tableName)
                .item(fields)
                .build());
    }

    public GetItemResponse getUserSync(String tableName, Map<String, String> keys) {
        Map<String, AttributeValue> keyCollection = new HashMap<>();

        keys.forEach((key, value) -> keyCollection.put(key, AttributeValue.builder().s(value).build()));

        return dynamoDbClient.getItem(GetItemRequest.builder()
                .tableName(tableName)
                .key(keyCollection)
                .build());
    }

    public QueryResponse getUserByUsername(String username) {
        Map<String, Condition> conditions = new HashMap<>();

        conditions.put("USERNAME", Condition.builder()
                .attributeValueList(List.of(AttributeValue.builder().s(username).build()))
                .comparisonOperator(ComparisonOperator.EQ)
                .build());

        return dynamoDbClient.query(QueryRequest.builder()
                .tableName("PROD_USER")
                .indexName("idndx_username")
                .keyConditions(conditions)
                .build());
    }

}
