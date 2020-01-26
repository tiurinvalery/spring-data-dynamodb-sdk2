package com.tiurinvalery.springdata.sdk2.parser;

import com.tiurinvalery.springdata.sdk2.constants.KeyType;
import com.tiurinvalery.springdata.sdk2.model.User;
import com.tiurinvalery.springdata.sdk2.parser.data.KeyProperties;
import com.tiurinvalery.springdata.sdk2.parser.data.Parseable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class KeyPropertiesParserTest {

    @Autowired
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