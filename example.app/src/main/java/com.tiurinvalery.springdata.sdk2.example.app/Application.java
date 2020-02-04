package com.tiurinvalery.springdata.sdk2.example.app;

import com.tiurinvalery.springdata.sdk2.StartDynamoDb2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(value = {StartDynamoDb2.class})
public class Application {

    public static void main(String [] args) {
        SpringApplication.run(Application.class);
    }
}
