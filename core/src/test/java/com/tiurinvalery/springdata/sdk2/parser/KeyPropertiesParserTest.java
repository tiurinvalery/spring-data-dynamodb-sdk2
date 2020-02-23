package com.tiurinvalery.springdata.sdk2.parser;

import com.tiurinvalery.springdata.sdk2.model.User;
import com.tiurinvalery.springdata.sdk2.parser.data.KeyProperties;
import com.tiurinvalery.springdata.sdk2.parser.data.Parseable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import software.amazon.awssdk.services.dynamodb.model.KeyType;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class KeyPropertiesParserTest {

    @InjectMocks
    private KeyParser keyParser;

    @Test
    public void parse() {
        User user = new User();
        user.uuid = "s-123-dfsa";
        Parseable result = keyParser.parse(user);
        assertEquals("UUID", ((KeyProperties) result).getFieldName());
        assertEquals(KeyType.HASH, ((KeyProperties) result).getKeyType());
    }
}