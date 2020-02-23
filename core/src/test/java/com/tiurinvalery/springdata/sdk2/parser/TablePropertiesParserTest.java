package com.tiurinvalery.springdata.sdk2.parser;

import com.tiurinvalery.springdata.sdk2.model.Car;
import com.tiurinvalery.springdata.sdk2.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class TablePropertiesParserTest {

    @InjectMocks
    private TableParser tableParser;

    @Test
    public void parseTest() {
        User user = new User();
        assertEquals("USER", tableParser.parse(user).getTableName());
    }

    @Test(expected = RuntimeException.class)
    public void parseEmptyNameTest() {
        Car car = new Car();
        tableParser.parse(car);
    }

}