package com.tiurinvalery.springdata.sdk2.parser;

import com.tiurinvalery.springdata.sdk2.annotations.Key;
import com.tiurinvalery.springdata.sdk2.entities.Item;
import com.tiurinvalery.springdata.sdk2.model.User;
import com.tiurinvalery.springdata.sdk2.parser.data.KeyProperties;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.dynamodb.model.KeyType;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EntityParserTest {

    @Test
    void parseField() throws NoSuchFieldException {
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
    void defineTableName() {
        Item ref = Item.builder().build();
        String expected = "USER";
        EntityParser.defineTableName(User.class, ref);
        assertEquals(expected, ref.getTableName());
    }
}