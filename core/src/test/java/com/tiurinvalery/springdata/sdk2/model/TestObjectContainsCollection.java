package com.tiurinvalery.springdata.sdk2.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestObjectContainsCollection {

    private List<String> testCollection;
}
