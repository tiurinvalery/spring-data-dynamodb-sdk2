package springdata.sdk2.example.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;
import springdata.sdk2.example.app.model.User;
import springdata.sdk2.example.app.service.GenerateUsersForLoadTestService;
import springdata.sdk2.example.app.service.UserServiceImpl;
import springdata.sdk2.example.app.service.UserServiceSyncImpl;

import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private UserServiceSyncImpl userServiceSync;
    @Autowired
    private GenerateUsersForLoadTestService generateUsersForLoadTestService;

    @GetMapping("/user/{user_id}")
    public ResponseEntity<GetItemResponse> getUser(@PathVariable(name = "user_id") String userId) {
        return ResponseEntity.ok(userService.getUser(userId).join());
    }

    @PostMapping("/user/create")
    public ResponseEntity<Boolean> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.saveUser(user).join().sdkHttpResponse().isSuccessful());
    }

    @GetMapping("/user/generate")
    public ResponseEntity<List<User>> generateUsersForLoadTest(@RequestParam("capacity") Integer capacity) {
        return ResponseEntity.ok(generateUsersForLoadTestService.getUsers(capacity));
    }

    @PostMapping("/user/sync/create")
    public ResponseEntity<Boolean> createUserSync(@RequestBody User user) {
        Map<String, AttributeValue> userAttributes = Map.of("UUID", AttributeValue.builder().s(user.getUuid()).build(),
                "USERNAME", AttributeValue.builder().s(user.getUsername()).build());

        return ResponseEntity.ok(userServiceSync.saveUserSyncClient(
                "PROD_USER", userAttributes).sdkHttpResponse().isSuccessful());
    }
}
