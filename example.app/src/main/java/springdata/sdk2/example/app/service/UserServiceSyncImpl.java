package springdata.sdk2.example.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;

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

}
