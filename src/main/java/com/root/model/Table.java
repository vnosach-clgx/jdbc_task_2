package com.root.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Getter
public class Table {
    private final String name;
    private final Set<Column> columns;
    private final List<Row> rows;
}
