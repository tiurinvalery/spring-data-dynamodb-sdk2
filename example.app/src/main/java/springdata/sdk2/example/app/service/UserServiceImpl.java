package springdata.sdk2.example.app.service;

import com.tiurinvalery.springdata.sdk2.repository.DynamoDbCrudRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemResponse;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;
import springdata.sdk2.example.app.exception.InvalidRequiredAttributeException;
import springdata.sdk2.example.app.model.User;

import java.util.Map;
import java.util.concurrent.CompletableFuture;


@Service
public class UserServiceImpl {

    @Autowired
    private DynamoDbCrudRepo dynamoDbCrudRepo;

    @Autowired
    private DynamoDbClient dynamoDbClient;

//    @PostConstruct
//    public void test() {
//        User user = User.builder()
//                .username("username")
//                .uuid("swa-123-dfsas")
//                .build();
//        dynamoDbCrudRepo.findById(user).join();
//        dynamoDbCrudRepo.save(user).join();
//        dynamoDbCrudRepo.findById(user).join();
//        dynamoDbCrudRepo.delete(user).join();
//        dynamoDbCrudRepo.findById(user).join();
//    }

    public CompletableFuture<GetItemResponse> getUser(String uuid) {
        return dynamoDbCrudRepo.findById(User.builder().uuid(uuid).build());
    }

    public CompletableFuture<PutItemResponse> saveUser(User user) {
        if (null == user || null == user.getUuid() || !user.getUuid().contains("uuid")) {
            throw new InvalidRequiredAttributeException();
        }
        return dynamoDbCrudRepo.save(user);
    }

    public PutItemResponse saveUserSyncClient(String tableName, Map<String, AttributeValue> fields) {
        return dynamoDbClient.putItem(PutItemRequest.builder()
                .tableName(tableName)
                .item(fields)
                .build());
    }

    public DeleteItemResponse deleteUser(String uuid) {
        return dynamoDbCrudRepo.delete(User.builder().uuid(uuid).build()).join();
    }

    public void approveUser(String uuid) {
        GetItemResponse userCreated = getUser(uuid).join();

        if (userCreated.item().size() > 0) {
            String username = userCreated.item().get("USERNAME").s();
            if (checkUserEmailApprovedFromThirdParty(username)) {
                saveUser(User.builder()
                        .username(username)
                        .uuid(uuid)
                        .approvedUser("y")
                        .build());
            } else {
                saveUser(User.builder()
                        .username(username)
                        .uuid(uuid)
                        .approvedUser("n")
                        .build());
            }
        }
    }


    private boolean checkUserEmailApprovedFromThirdParty(String username) {
        if (username.hashCode() % 2 == 0) {
            return true;
        } else {
            return false;
        }
    }
}
