package com.tiurinvalery.springdata.sdk2.model;

import com.tiurinvalery.springdata.sdk2.annotations.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(tableName = "", clazz = Car.class)
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Car {

    public String test;
}
