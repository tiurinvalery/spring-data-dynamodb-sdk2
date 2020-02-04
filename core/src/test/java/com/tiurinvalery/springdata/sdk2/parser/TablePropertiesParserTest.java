package com.tiurinvalery.springdata.sdk2.parser;

import com.tiurinvalery.springdata.sdk2.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TablePropertiesParserTest {

    @Autowired
    private TableParser tableParser;

    @Test
    public void parseTest() {
        User user = new User();
        assertEquals("USER", tableParser.parse(user).getTableName());
    }

}