package com.tiurinvalery.springdata.sdk2.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TargetComponentsLoaderServiceImpl.class})
class TargetComponentsLoaderServiceImplTest {

    @Autowired
    private TargetComponentsLoaderServiceImpl classLoaderService;

    @Test
    void createItemList() {
        assertEquals(2, classLoaderService.getEntityItems().size());
    }

}