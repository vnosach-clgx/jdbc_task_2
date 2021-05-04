package com.root;

import com.github.javafaker.Faker;
import com.root.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Service
@RequiredArgsConstructor
public class DbService {

    @Value("${db.tables-count}")
    private int tablesCount;
    @Value("${db.columns-count}")
    private int columnsCount;
    @Value("${db.rows-count}")
    private int rowsCount;

    private final Map<SqlType, Supplier<Object>> typeSupplierMap;
    Faker faker = new Faker();

    public Db generateDb() {
        return new Db(generate(), generateTables());
    }

    private Set<Table> generateTables() {
        return IntStream.range(0, tablesCount)
                .mapToObj(f -> generateTable())
                .collect(toSet());
    }

    private Table generateTable() {
        var columns = generateColumns();
        var collect = columns.stream().map(Column::getType).collect(toList());
        return new Table(generate(), columns, generateRows(collect));
    }

    private List<Row> generateRows(List<SqlType> columns) {
        return IntStream.range(0, rowsCount)
                .mapToObj(f -> generateRow(columns))
                .collect(toList());
    }

    private Row generateRow(List<SqlType> sqlTypes) {
        var objectList = sqlTypes.stream()
                .map(t -> typeSupplierMap.getOrDefault(t, () -> faker.lorem().word()))
                .map(Supplier::get)
                .collect(toList());
        return new Row(objectList);
    }

    private Set<Column> generateColumns() {
        return IntStream.range(0, columnsCount)
                .mapToObj(f -> generateColumn())
                .collect(toSet());
    }

    private Column generateColumn() {
        var random = SqlType.getRandom();
        return new Column(generate(), random, random.getType());
    }

    private String generate() {
        return String.join("_", faker.lorem().word())
                + ThreadLocalRandom.current().nextInt(0, 10000);
    }
}
