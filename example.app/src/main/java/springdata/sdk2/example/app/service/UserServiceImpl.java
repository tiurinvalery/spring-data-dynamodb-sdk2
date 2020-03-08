package springdata.sdk2.example.app.service;

import com.tiurinvalery.springdata.sdk2.repository.DynamoDbCrudRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemResponse;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;
import springdata.sdk2.example.app.exception.InvalidRequiredAttributeException;
import springdata.sdk2.example.app.model.User;


@Service
public class UserServiceImpl {

    @Autowired
    private DynamoDbCrudRepo dynamoDbCrudRepo;

//    @PostConstruct
//    public void test() {
//        User user = User.builder()
//                .username("username")
//                .UUID("swa-123-dfsas")
//                .build();
//        dynamoDbCrudRepo.findById(user).join();
//        dynamoDbCrudRepo.save(user).join();
//        dynamoDbCrudRepo.findById(user).join();
//        dynamoDbCrudRepo.delete(user).join();
//        dynamoDbCrudRepo.findById(user).join();
//    }

    public GetItemResponse getUser(String uuid) {
        return dynamoDbCrudRepo.findById(User.builder().UUID(uuid).build()).join();
    }

    public PutItemResponse saveUser(User user) {
        if (null == user || null == user.getUUID() || !user.getUUID().contains("uuid")) {
            throw new InvalidRequiredAttributeException();
        }
        return dynamoDbCrudRepo.save(user).join();
    }

    public DeleteItemResponse deleteUser(String uuid) {
        return dynamoDbCrudRepo.delete(User.builder().UUID(uuid).build()).join();
    }


}
