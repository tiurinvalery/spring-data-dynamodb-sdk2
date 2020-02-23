package springdata.sdk2.example.app.service;

import com.tiurinvalery.springdata.sdk2.repository.DynamoDbCrudRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springdata.sdk2.example.app.model.User;

import javax.annotation.PostConstruct;


@Service
public class TestService {

    @Autowired
    private DynamoDbCrudRepo dynamoDbCrudRepo;

    @PostConstruct
    public void test() {
        User user = User.builder()
                .username("username")
                .UUID("swa-123-dfsas")
                .build();
//        dynamoDbCrudRepo.putItem(user).join();
        dynamoDbCrudRepo.findById(user).join();

    }
}
