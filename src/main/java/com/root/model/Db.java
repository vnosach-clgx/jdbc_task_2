package com.root.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
@Getter
public class Db {
    private final String name;
    private final Set<Table> tables;
}
