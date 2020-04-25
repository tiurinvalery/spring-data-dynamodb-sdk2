package com.tiurinvalery.springdata.sdk2.parser;

import com.tiurinvalery.springdata.sdk2.annotations.Key;
import com.tiurinvalery.springdata.sdk2.entities.Item;
import com.tiurinvalery.springdata.sdk2.model.User;
import com.tiurinvalery.springdata.sdk2.parser.data.KeyProperties;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import software.amazon.awssdk.services.dynamodb.model.KeyType;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class EntityParserTest {

    @Test
    public void parseFieldTest() throws NoSuchFieldException {
        Item ref = Item.builder().build();
        Map<String, String> fieldMapping = new HashMap<>();
        Field uuid = User.class.getDeclaredField("uuid");

        KeyProperties expected = KeyProperties.builder()
                .keyType(KeyType.HASH)
                .fieldName("UUID")
                .build();

        Key key = EntityParser.parseField(ref, fieldMapping, null, uuid);

        assertNotNull(key);
        assertEquals(1, ref.getKeys().size());
        assertEquals(expected, ref.getKeys().get("uuid"));
    }

    @Test
    public void defineTableNameTest() {
        Item ref = Item.builder().build();
        String expected = "USER";
        EntityParser.defineTableName(User.class, ref);
        assertEquals(expected, ref.getTableName());
    }

    //Check that we can't have multiple keys
    @Test(expected = RuntimeException.class)
    @Ignore
    public void parseFieldWithKeyExist() {
        Key keyExist = mock(Key.class);
        EntityParser.parseKey(null,keyExist, null); //other attributes doesn't matter if keyExist
    }

    @Test(expected = RuntimeException.class)
    public void parseFieldWithMultipleKeyAnnotationOnOneEntity() {
        Key [] keys = mock(Key[].class);
        keys [0] = mock(Key.class);
        keys [1] = mock(Key.class);
        Field fieldWith2Keys = mock(Field.class);
        doReturn(keys).when(fieldWith2Keys).getAnnotationsByType(Key.class);
        EntityParser.parseKey(null, null, fieldWith2Keys);
    }
}