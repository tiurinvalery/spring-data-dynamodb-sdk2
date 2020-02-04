package com.tiurinvalery.springdata.sdk2.parser;

import com.tiurinvalery.springdata.sdk2.parser.data.Parseable;

public interface Parser {
    Parseable parse(Object object);
}
