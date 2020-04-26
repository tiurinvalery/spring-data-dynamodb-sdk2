package com.tiurinvalery.springdata.sdk2.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Index {
    private String fieldName;
    private String dbAttributeName;
    private String secondaryIndexName;
}
