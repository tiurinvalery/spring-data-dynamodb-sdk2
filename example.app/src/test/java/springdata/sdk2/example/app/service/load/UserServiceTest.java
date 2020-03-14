package springdata.sdk2.example.app.service.load;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;
import springdata.sdk2.example.app.model.User;
import springdata.sdk2.example.app.service.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserServiceImpl userService;

    private Random random = new Random();

    private int threadPoolCapacity = 500;

    //TODO: futures run just ones after future creation;
    @Test
    public void putItemAsyncTest() throws InterruptedException, ExecutionException {

        ExecutorService threadPool =
                Executors.newFixedThreadPool(threadPoolCapacity);

        List<Future<CompletableFuture<PutItemResponse>>> threads = new ArrayList<>();

        for (int t = 0; t < threadPoolCapacity; t++) {
            String username = "username" + random.nextLong() + random.nextLong();
            String uuid = "uuid-" + random.nextInt() + random.nextInt();
            threads.add(threadPool.submit(() -> userService.saveUser(User.builder()
                    .username(username)
                    .uuid(uuid)
                    .build())));
        }

        List<PutItemResponse> responses = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            long startAsync = System.currentTimeMillis();
            for (Future<CompletableFuture<PutItemResponse>> future : threads) {
                responses.add(future.get().join());
            }
            long endAsync = System.currentTimeMillis();
            System.out.println("Async run number " + i + " :" + (endAsync - startAsync));
        }
        assertEquals(responses.size(), 50 * threadPoolCapacity);

        ExecutorService threadPoolSync =
                Executors.newFixedThreadPool(threadPoolCapacity);

        List<Future<PutItemResponse>> threadsForSync = new ArrayList<>();

        for (int t = 0; t < threadPoolCapacity; t++) {
            String username = "username" + random.nextLong() + random.nextLong();
            String uuid = "uuid-" + random.nextInt() + random.nextInt();
            String tableName = "PROD_USER";
            threadsForSync.add(threadPoolSync.submit(() ->
                    userService.saveUserSyncClient(tableName, Map.of("UUID", AttributeValue.builder()
                            .s(uuid).build(), "USERNAME", AttributeValue.builder().s(username).build()))));
        }

        List<PutItemResponse> responsesSync = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            long startSync = System.currentTimeMillis();
            for (Future<PutItemResponse> future : threadsForSync) {
                responsesSync.add(future.get());
            }
            long endSync = System.currentTimeMillis();
            System.out.println("Sync run number " + i + " :" + (endSync - startSync));
        }
        assertEquals(responsesSync.size(), 50 * threadPoolCapacity);
    }
}
