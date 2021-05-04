package com.root.model;

import lombok.Getter;

import java.util.concurrent.ThreadLocalRandom;

@Getter
public enum SqlType {
    VARCHAR("varchar(50)"),
    TIME("time"),
    DATE("date"),
    BOOLEAN("boolean"),
    INTEGER("int");

    private final String type;

    SqlType(String type) {
        this.type = type;
    }

    public static SqlType getRandom() {
        var i = ThreadLocalRandom.current().nextInt(0, values().length);
        return values()[i];
    }
}
