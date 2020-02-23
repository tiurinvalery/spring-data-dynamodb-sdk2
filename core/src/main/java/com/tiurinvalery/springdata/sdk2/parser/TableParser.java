package com.tiurinvalery.springdata.sdk2.parser;

import com.tiurinvalery.springdata.sdk2.annotations.Table;
import com.tiurinvalery.springdata.sdk2.parser.data.TableProperties;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;

@Service
public class TableParser implements Parser {

    @Override
    public TableProperties parse(Object object) {
        Annotation[] annotations = object.getClass().getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof Table) {
                String tableName = ((Table) annotation).tableName();
                if (!tableName.isBlank()) {
                    return TableProperties.builder()
                            .tableName(tableName)
                            .build();
                } else {
                    throw new RuntimeException("Name for table cannot be empty");
                }
            }
        }
        return null;
    }
}
