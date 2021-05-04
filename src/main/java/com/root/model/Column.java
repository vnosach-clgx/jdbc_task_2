package com.root.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Column {
    private final String name;
    private final SqlType type;
    private final String stringType;
}
