package com.tiurinvalery.springdata.sdk2.parser;

import com.tiurinvalery.springdata.sdk2.annotations.Attribute;
import com.tiurinvalery.springdata.sdk2.annotations.Key;
import com.tiurinvalery.springdata.sdk2.annotations.Table;
import com.tiurinvalery.springdata.sdk2.entities.Item;
import com.tiurinvalery.springdata.sdk2.parser.data.KeyProperties;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EntityParser {

    private EntityParser() {

    }

    public static void defineKeyAndAttribute(List<Item> itemList, Field[] fields, Item item) {
        Map<String, String> fieldMappingMap = new HashMap<>();
        Key key = null;
        for (Field f : fields) {
            key = parseField(item, fieldMappingMap, key, f);
        }
        item.setCodeAndDbFieldMapping(fieldMappingMap);
        itemList.add(item);
    }

    public static Key parseField(Item item, Map<String, String> fieldMappingMap, Key key, Field f) {
        try {
            if (null == key) {
                Key[] annotationsByType = f.getAnnotationsByType(Key.class);
                if (annotationsByType.length == 1) {
                    key = annotationsByType[0];
                    item.setKeys(Map.of(f.getName(), KeyProperties.builder().fieldName(key.fieldName()).keyType(key.keyType()).build()));
                }
            } else {
//                throw new RuntimeException("Support for more than 1 Key per entity not implemented yet");
            }
        } catch (NullPointerException npe) {
            System.out.println(npe);
        }
        try {
            Attribute[] annotationsByType = f.getAnnotationsByType(Attribute.class);
            if (annotationsByType != null && annotationsByType.length > 0) {
                fieldMappingMap.put(f.getName(), annotationsByType[0].name());
            }
//            Attribute attribute = f.getAnnotationsByType(Attribute.class)[0];

        } catch (NullPointerException npe) {
            System.out.println(npe);
        }
        return key;
    }

    public static void defineTableName(Class c, Item item) {
        item.setClazz(c);
        try {
            Table table = (Table) c.getAnnotationsByType(Table.class)[0];
            item.setTableName(table.tableName());
        } catch (NullPointerException npe) {
            System.out.println(npe);
        }
    }
}
