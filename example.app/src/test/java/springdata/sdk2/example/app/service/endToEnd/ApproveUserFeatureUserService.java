package springdata.sdk2.example.app.service.endToEnd;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import springdata.sdk2.example.app.model.User;
import springdata.sdk2.example.app.service.UserServiceImpl;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApproveUserFeatureUserService {

    @Autowired
    private UserServiceImpl userService;

    private Random random = new Random();

    private User user;

    @Before
    public void initUser() {
        this.user = User.builder()
                .UUID("uuid" + random.nextInt() + random.nextLong())
                .username("username" + random.nextLong())
                .build();
    }

    @After
    public void deleteTestUser() {
        userService.deleteUser(user.getUUID());
    }

    @Test
    public void approveUserTest() {
        iCreateUser(user);
        andIFoundThatUserExistWithoutApprove(user.getUUID(), user.getUsername());
        andIApproveUserBasedOnFakeThirdPartyResponse(user.getUUID());
        andIFoundThatUserApprovedStateEqualsToThirdPartyResponse(user.getUUID(), user.getUsername());
    }

    private void iCreateUser(User user) {
        userService.saveUser(user);
    }

    private void andIFoundThatUserExistWithoutApprove(String uuid, String usernameExpected) {
        GetItemResponse user = userService.getUser(uuid);
        String username = user.item().get("USERNAME").s();
        AttributeValue approved = user.item().get("APPROVED");

        assertEquals(usernameExpected, username);
        assertNull(approved);
    }

    private void andIApproveUserBasedOnFakeThirdPartyResponse(String uuid) {
        userService.approveUser(uuid);
    }

    private void andIFoundThatUserApprovedStateEqualsToThirdPartyResponse(String uuid, String usernameExpected) {
        String expected = usernameExpected.hashCode() % 2 == 0 ? "y" : "n";

        GetItemResponse user = userService.getUser(uuid);
        String username = user.item().get("USERNAME").s();
        AttributeValue approved = user.item().get("APPROVED");

        assertEquals(usernameExpected, username);
        assertEquals(expected, approved.s());
    }


}
