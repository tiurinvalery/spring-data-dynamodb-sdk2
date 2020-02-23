package com.tiurinvalery.springdata.sdk2;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(value = {"com.tiurinvalery.springdata.sdk2.repository", "com.tiurinvalery.springdata.sdk2.parser",
        "com.tiurinvalery.springdata.sdk2.service"})
public class StartDynamoDb2 {
}
