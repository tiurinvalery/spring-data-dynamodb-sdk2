package com.tiurinvalery.springdata.sdk2.parser;

import com.tiurinvalery.springdata.sdk2.annotations.Key;
import com.tiurinvalery.springdata.sdk2.parser.data.KeyProperties;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

@Service
public class KeyParser  implements Parser{

    @Override
    public KeyProperties parse(Object object) {
        Field[] fields = object.getClass().getFields();

        for(Field f : fields) {
            Annotation[] annotations = f.getAnnotations();
            for(Annotation annotation: annotations) {
                if(annotation instanceof Key) {
                    return KeyProperties.builder()
                            .fieldName(((Key) annotation).fieldName())
                            .keyType(((Key) annotation).keyType())
                            .build();
                }
            }
        }
        return null;
    }
}
