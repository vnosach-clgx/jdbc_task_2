package com.root;

import com.root.model.Column;
import com.root.model.Db;
import com.root.model.Table;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Set;

import static java.util.stream.Collectors.joining;

@Service
public class SqlQueryService {
    @Value("${create.schema}")
    private String CREATE_SCHEMA;
    @Value("${create.table.postfix}")
    private String CREATE_TABLE_POSTFIX;
    @Value("${create.table.attribute}")
    private String CREATE_TABLE_ATTRIBUTE;
    @Value("${create.table.suffix}")
    private String CREATE_TABLE_SUFFIX;
    @Value("${insert.rows.sql}")
    private String INSERT_ROWS_SQL;

    public String generateSchemaCreatingQuery(Db db) {
        return String.format(CREATE_SCHEMA, db.getName());
    }

    public String[] generateTablesCreatingQuery(Db db) {
        return db.getTables().stream()
                .map(this::generateTableCreatingQuery)
                .toArray(String[]::new);
    }

    public String generateTableCreatingQuery(Table table) {
        return String.format(CREATE_TABLE_POSTFIX, table.getName()) + generateColumnQuery(table.getColumns())
                + CREATE_TABLE_SUFFIX;
    }

    private String generateColumnQuery(Set<Column> columns) {
        return columns.stream()
                .map(column -> String.format(CREATE_TABLE_ATTRIBUTE, column.getName(), column.getType()))
                .collect(joining(","));
    }

    public String generateRowsQuery(Table table) {
        var name = table.getName();
        var columns = table.getColumns().stream().map(Column::getName).collect(joining(","));
        var data = table.getRows().stream()
                .map(row -> row.getData().stream().map(Object::toString).collect(joining(",", "(", ")")))
                .collect(joining(","));
        return String.format(INSERT_ROWS_SQL, name, columns, data);
    }
}
