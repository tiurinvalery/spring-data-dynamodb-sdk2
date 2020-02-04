package com.tiurinvalery.springdata.sdk2.example.app.service;

import com.tiurinvalery.springdata.sdk2.repository.DynamoDbCrudRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    @Autowired
    private DynamoDbCrudRepo dynamoDbCrudRepo;
}
