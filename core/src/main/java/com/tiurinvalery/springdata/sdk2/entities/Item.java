package com.tiurinvalery.springdata.sdk2.entities;

import com.tiurinvalery.springdata.sdk2.parser.data.KeyProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Item {
    private Class clazz;
    private Map<String, String> codeAndDbFieldMapping;
    private Map<String, KeyProperties> keys;
    private String tableName;
}
