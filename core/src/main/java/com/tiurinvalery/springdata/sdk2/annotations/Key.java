package com.tiurinvalery.springdata.sdk2.annotations;


import software.amazon.awssdk.services.dynamodb.model.KeyType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Key {

    String fieldName();

    KeyType keyType();
}
