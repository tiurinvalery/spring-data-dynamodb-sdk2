package com.tiurinvalery.springdata.sdk2.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DynamoDB2IndexHashKey {
    String attributeName() default "";
    String globalSecondaryIndexName() default "";
}
