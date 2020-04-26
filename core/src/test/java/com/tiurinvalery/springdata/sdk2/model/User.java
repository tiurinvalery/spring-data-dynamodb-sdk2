package com.tiurinvalery.springdata.sdk2.model;

import com.tiurinvalery.springdata.sdk2.annotations.Attribute;
import com.tiurinvalery.springdata.sdk2.annotations.DynamoDB2IndexHashKey;
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
    @Attribute(name = "UUID")
    private String uuid;

    @Attribute(name = "USERNAME")
    @DynamoDB2IndexHashKey(attributeName = "USERNAME", globalSecondaryIndexName = "idndx_username")
    private String username;

    @Attribute(name = "APPROVED")
    private String approvedUser;

    @Attribute(name = "ATTRIBUTE")
    public String attribute;
}
