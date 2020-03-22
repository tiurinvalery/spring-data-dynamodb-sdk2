package springdata.sdk2.example.app.service;

import org.springframework.stereotype.Service;
import springdata.sdk2.example.app.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class GenerateUsersForLoadTestService {

    private Random random = new Random();

    public List<User> getUsers(Integer capacity) {
        List<User> users = new ArrayList<>(capacity);

        for (int i = 0; i < capacity; i++) {
            String uuid = "uuid-"+i+"_"+random.nextLong();
            String username = "username-"+i+"_"+random.nextLong();
            users.add(User.builder()
                    .uuid(uuid)
                    .username(username)
                    .build());
        }
        return users;
    }
}
