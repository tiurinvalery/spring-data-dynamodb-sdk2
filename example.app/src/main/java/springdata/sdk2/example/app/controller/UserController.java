package springdata.sdk2.example.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;
import springdata.sdk2.example.app.model.User;
import springdata.sdk2.example.app.service.GenerateUsersForLoadTestService;
import springdata.sdk2.example.app.service.UserServiceImpl;
import springdata.sdk2.example.app.service.UserServiceSyncImpl;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RestController
public class UserController {

    private ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private UserServiceSyncImpl userServiceSync;
    @Autowired
    private GenerateUsersForLoadTestService generateUsersForLoadTestService;

    @GetMapping("/user/{user_id}")
    public ResponseEntity<Long> getUser(@PathVariable(name = "user_id") String userId) throws InterruptedException, ExecutionException {
        long start = System.currentTimeMillis();
        userService.getUser(userId).get();
        long end = System.currentTimeMillis();
        return ResponseEntity.ok(end - start);
    }

    @GetMapping("/user/sync/{user_id}")
    public ResponseEntity<Long> getUserSync(@PathVariable(name = "user_id") String userId) {
        long start = System.currentTimeMillis();
        Map<String, String> uuid = Map.of("UUID", userId);
        userServiceSync.getUserSync("PROD_USER", uuid);
        long end = System.currentTimeMillis();
        return ResponseEntity.ok(end - start);

    }


    @GetMapping("/user/username/{username}")
    public ResponseEntity<QueryResponse> getUserViaUsername(@PathVariable(name = "username") String username) {
        return ResponseEntity.ok(userService.findUserByUsername(username).join());
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

    @PostMapping("/user/fullsycle")
    public ResponseEntity<Long> startFullFlow(@RequestBody User user) throws InterruptedException, ExecutionException, TimeoutException {
        Long startTime = System.currentTimeMillis();
        CompletableFuture<GetItemResponse> checkThatUserAbsent = userService.getUser(user.getUuid());
        checkThatUserAbsent
                .thenApplyAsync(fn -> fn.item().get("USERNAME").s().isEmpty());
        checkThatUserAbsent.get(1200, TimeUnit.MILLISECONDS);

        CompletableFuture<PutItemResponse> putItemResponseCompletableFuture = userService.saveUser(user);

        putItemResponseCompletableFuture
                .thenAcceptAsync(fn -> {
                    try {
                        userService.getUser(user.getUuid()).get(1200, TimeUnit.MILLISECONDS);
                    } catch (InterruptedException | ExecutionException | TimeoutException e) {

                    }
                    return;
                });

        Long endTime = System.currentTimeMillis();
        return ResponseEntity.ok(endTime - startTime);
    }

    @PostMapping("/user/sync/fullsycle")
    public ResponseEntity<Long> startFullFlowSync(@RequestBody User user) {
        Long startTime = System.currentTimeMillis();

        Map<String, String> uuid = Map.of("UUID", user.getUuid());
        GetItemResponse prodUser = userServiceSync.getUserSync("PROD_USER", uuid);

        if (prodUser.getValueForField("USERNAME", String.class).isEmpty()) {

            Map<String, AttributeValue> userAttributes = Map.of("UUID", AttributeValue.builder().s(user.getUuid()).build(),
                    "USERNAME", AttributeValue.builder().s(user.getUsername()).build());

            userServiceSync.saveUserSyncClient("PROD_USER", userAttributes);

            userServiceSync.getUserSync("PROD_USER", uuid);

            Long endTime = System.currentTimeMillis();
            return ResponseEntity.ok(endTime - startTime);
        }
        return ResponseEntity.ok(null);
    }

    @PostMapping("/user/create/operations")
    public ResponseEntity<Long> asyncCreateAndLogic(@RequestBody User user) throws InterruptedException, ExecutionException, TimeoutException {
        Long startTime = System.currentTimeMillis();
        CompletableFuture<PutItemResponse> putItemResponseCompletableFuture = userService.saveUser(user);
        putItemResponseCompletableFuture
                .thenApplyAsync(fn -> users.putIfAbsent(user.getUsername(), user))
                .thenApplyAsync(Objects::nonNull)
                .thenApply(fn -> user.getUsername().hashCode());
        putItemResponseCompletableFuture.get(400, TimeUnit.MILLISECONDS);
        Long endTime = System.currentTimeMillis();
        return ResponseEntity.ok(endTime - startTime);
    }

    @PostMapping("/user/sync/create/operations")
    public ResponseEntity<Long> syncCreateAndLogic(@RequestBody User user) {
        Long startTime = System.currentTimeMillis();
        Map<String, AttributeValue> userAttributes = Map.of("UUID", AttributeValue.builder().s(user.getUuid()).build(),
                "USERNAME", AttributeValue.builder().s(user.getUsername()).build());

        userServiceSync.saveUserSyncClient("PROD_USER", userAttributes);

        User existUser = users.putIfAbsent(user.getUsername(), user);
        if (null != existUser) {
            user.getUsername().hashCode();
        }
        Long endTime = System.currentTimeMillis();
        if ((endTime - startTime) < 400L) {
            return ResponseEntity.ok(endTime - startTime);
        } else {
            return new ResponseEntity<>(endTime - startTime, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
