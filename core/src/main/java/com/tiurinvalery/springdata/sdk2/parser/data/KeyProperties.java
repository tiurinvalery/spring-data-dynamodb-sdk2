package com.tiurinvalery.springdata.sdk2.parser.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.services.dynamodb.model.KeyType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KeyProperties implements Parseable {

    private String fieldName;
    private KeyType keyType;
}
