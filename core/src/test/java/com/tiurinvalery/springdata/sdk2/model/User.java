package com.tiurinvalery.springdata.sdk2.model;

import com.tiurinvalery.springdata.sdk2.annotations.Attribute;
import com.tiurinvalery.springdata.sdk2.annotations.Key;
import com.tiurinvalery.springdata.sdk2.annotations.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.services.dynamodb.model.KeyType;

@Table(tableName = "USER", clazz = User.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Key(fieldName = "UUID", keyType = KeyType.HASH)
    public String uuid;

    @Attribute(name = "ATTRIBUTE")
    public String attribute;
}
