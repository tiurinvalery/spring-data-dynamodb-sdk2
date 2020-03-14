package springdata.sdk2.example.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import springdata.sdk2.example.app.service.UserServiceImpl;

@RestController("/user")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @Value("test.method")
    private String testMethod;

    @GetMapping("/{user_id}")
    public ResponseEntity<GetItemResponse> getUser(@PathVariable(name = "user_id") String userId) {
        return ResponseEntity.ok(userService.getUser(userId).join());
    }
}
