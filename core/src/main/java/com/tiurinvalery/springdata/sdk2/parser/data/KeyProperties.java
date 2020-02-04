package com.tiurinvalery.springdata.sdk2.parser.data;

import com.tiurinvalery.springdata.sdk2.constants.KeyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KeyProperties implements Parseable{

    private String fieldName;
    private KeyType keyType;
}
