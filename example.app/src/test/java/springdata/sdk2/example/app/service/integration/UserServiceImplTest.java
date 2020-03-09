package springdata.sdk2.example.app.service.integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemResponse;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;
import springdata.sdk2.example.app.exception.InvalidRequiredAttributeException;
import springdata.sdk2.example.app.model.User;
import springdata.sdk2.example.app.service.UserServiceImpl;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;

    private Random random = new Random();

    @Test
    public void saveUserTest() {
        String randomUsername = "username" + random.nextLong() + random.nextLong();
        String UUID = "uuid-" + random.nextInt() + random.nextInt();
        PutItemResponse putItemResponse = userService.saveUser(User.builder().UUID(UUID).username(randomUsername).build());

        assertNotNull(putItemResponse);
        assertTrue(putItemResponse.sdkHttpResponse().isSuccessful());
    }

    @Test(expected = InvalidRequiredAttributeException.class)
    public void saveUserTestUUIDNull() {
        String randomUsername = "username" + random.nextLong() + random.nextLong();

        userService.saveUser(User.builder().UUID(null).username(randomUsername).build());
    }

    @Test(expected = InvalidRequiredAttributeException.class)
    public void saveUserTestUUIDInvalid() {

        String randomUsername = "username" + random.nextLong() + random.nextLong();

        userService.saveUser(User.builder().UUID("trash_value").username(randomUsername).build());
    }

    @Test
    public void getUserTest() {
        String randomUsername = "username" + random.nextLong() + random.nextLong();
        String UUID = "uuid-" + random.nextInt() + random.nextInt();
        userService.saveUser(User.builder().UUID(UUID).username(randomUsername).build());

        GetItemResponse user = userService.getUser(UUID);

        assertNotNull(user);
        
        String username = user.item().get("USERNAME").s();
        assertEquals(randomUsername, username);
    }
    
    @Test
    public void deleteUserTest() {
        String randomUsername = "username" + random.nextLong() + random.nextLong();
        String UUID = "uuid-" + random.nextInt() + random.nextInt();
        userService.saveUser(User.builder().UUID(UUID).username(randomUsername).build());

        GetItemResponse user = userService.getUser(UUID);

        assertNotNull(user);

        DeleteItemResponse deleteItemResponse = userService.deleteUser(UUID);
        
        assertTrue(deleteItemResponse.sdkHttpResponse().isSuccessful());

        GetItemResponse mustBeEmpty = userService.getUser(UUID);

        assertTrue(mustBeEmpty.item().isEmpty());
    }
}