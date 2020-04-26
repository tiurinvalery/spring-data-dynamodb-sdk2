package com.tiurinvalery.springdata.sdk2.repository.impl;


import com.tiurinvalery.springdata.sdk2.entities.Index;
import com.tiurinvalery.springdata.sdk2.model.TestObjectContainsCollection;
import com.tiurinvalery.springdata.sdk2.model.User;
import com.tiurinvalery.springdata.sdk2.service.TargetComponentsLoaderServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class DynamoDbCrudRepoImplTest {

    @Mock
    private DynamoDbAsyncClient dynamoDbAsyncClient;

    @Mock
    private TargetComponentsLoaderServiceImpl targetComponentsLoaderService;

    @InjectMocks
    private DynamoDbCrudRepoImpl dynamoDbCrudRepo;

    @Before
    public void before() {
        dynamoDbCrudRepo = new DynamoDbCrudRepoImpl();
    }

    @Test
    public void processIndexWithNullFieldValue() throws IllegalAccessException, NoSuchFieldException {
        User user = User.builder().build();

        String expected = "expected";

        String secondaryIndexName = dynamoDbCrudRepo.processIndex(user, List.of(Index.builder().fieldName("username").build()), expected, null, 0);

        assertEquals(expected, secondaryIndexName);
    }

    @Test
    public void checkThatAllKeysForSameIndexFirstTime() {
        String expected = "name";
        Index index = Index.builder().secondaryIndexName(expected).build();
        String recieved = dynamoDbCrudRepo.checkThatAllKeysForSameIndex(null, index);

        assertEquals(expected, recieved);
    }

    @Test
    public void checkThatAllKeysForSameIndexTheSame() {
        String expected = "name";
        Index index = Index.builder().secondaryIndexName(expected).build();
        String recieved = dynamoDbCrudRepo.checkThatAllKeysForSameIndex(expected, index);

        assertEquals(expected, recieved);
    }

    @Test(expected = RuntimeException.class)
    public void checkThatAllKeysForSameIndexNotTheSame() {
        String index1 = "index1";
        Index indexWithAnotherName = Index.builder().secondaryIndexName("another_name").build();
        dynamoDbCrudRepo.checkThatAllKeysForSameIndex(index1, indexWithAnotherName);
    }

    @Test
    public void getFieldValue() throws NoSuchFieldException, IllegalAccessException {
        String expected = "y";

        User user = User.builder()
                .approvedUser(expected)
                .build();

        Index index = Index.builder()
                .fieldName("approvedUser")
                .build();

        Object fieldValue = dynamoDbCrudRepo.getFieldValue(user, index);

        assertEquals(expected, fieldValue);
    }

    @Test
    public void getFieldValueReturnNull() throws NoSuchFieldException, IllegalAccessException {
        String expected = null;

        User user = User.builder()
                .approvedUser(expected)
                .build();

        Index index = Index.builder()
                .fieldName("approvedUser")
                .build();

        Object fieldValue = dynamoDbCrudRepo.getFieldValue(user, index);

        assertEquals(expected, fieldValue);
    }

    @Test
    public void parseCollectionValues() throws IllegalAccessException, NoSuchFieldException {
        List<String> expected = List.of("test1", "test2");
        TestObjectContainsCollection testObject = TestObjectContainsCollection.builder()
                .testCollection(expected)
                .build();

        Field testCollection = testObject.getClass().getDeclaredField("testCollection");
        testCollection.setAccessible(true);
        List val = (List) testCollection.get(testObject);

        List<AttributeValue> attributeValues = dynamoDbCrudRepo.parseCollectionValues(val);

        attributeValues.forEach(av -> assertTrue(expected.contains(av.s())));
    }
}