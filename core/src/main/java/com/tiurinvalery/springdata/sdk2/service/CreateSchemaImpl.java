package com.tiurinvalery.springdata.sdk2.service;

import com.tiurinvalery.springdata.sdk2.annotations.Table;
import com.tiurinvalery.springdata.sdk2.entities.Item;
import com.tiurinvalery.springdata.sdk2.parser.data.Attribute;
import com.tiurinvalery.springdata.sdk2.repository.DSLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.model.CreateTableResponse;

import javax.annotation.PostConstruct;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class CreateSchemaImpl {

    @Value("${spring.data.dynamodb.sdkv2.schema.create}")
    private Boolean create;

    @Autowired
    private ClassLoaderServiceImpl classLoaderService;

    @Autowired
    private DSLService dslService;

//    @PostConstruct
//    public void init() {
//        if (create) {
//            List<Item> itemList = classLoaderService.getEntityItems();
//            for(Item item : itemList) {
//                String tableName = null;
//                Annotation[] annotations = item.getClazz().getAnnotationsByType(Table.class);
//                for (Annotation a : annotations) {
//                    if (a instanceof Table) {
//                        tableName = ((Table) a).tableName();
//                    }
//                }
//                List<Attribute> attributes = item.getCodeAndDbFieldMapping().values().stream()
//                        .map(value -> Attribute.builder().name(value).build())
//                        .collect(Collectors.toList());
//                CompletableFuture<CreateTableResponse> table = dslService.createTable(item.getKeys().values(), attributes, tableName);
//                table.thenAccept(t -> System.out.println(t.tableDescription()));
//            }
//        }
//    }
}
