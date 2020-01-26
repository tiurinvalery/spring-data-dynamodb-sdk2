package com.tiurinvalery.springdata.sdk2.model;

import com.tiurinvalery.springdata.sdk2.annotations.Key;
import com.tiurinvalery.springdata.sdk2.annotations.Table;
import com.tiurinvalery.springdata.sdk2.constants.KeyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(tableName = "USER")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Key(fieldName = "UUID", keyType = KeyType.HASH)
    public String uuid;
}
