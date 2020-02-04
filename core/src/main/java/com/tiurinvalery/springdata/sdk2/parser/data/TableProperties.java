package com.tiurinvalery.springdata.sdk2.parser.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TableProperties implements Parseable {

    private String tableName;
}
