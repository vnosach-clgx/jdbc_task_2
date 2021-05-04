package com.root;

import com.google.common.collect.Iterables;
import com.root.model.Db;
import com.root.model.Table;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SqlThreadExecutor {
    @Value("${db.thread-count}")
    private int threadCount;

    private final SqlQueryService sqlQueryService;

    private final ObjectFactory<Connection> connectionObjectFactory;

    public void execute(Db db) {
        var executorService = Executors.newFixedThreadPool(threadCount);

        Iterables.partition(db.getTables(), threadCount).forEach(
                tables -> executorService.submit(new SqlRunner(tables, connectionObjectFactory.getObject(), sqlQueryService))
        );
        executorService.shutdown();
    }

    @RequiredArgsConstructor
    private static class SqlRunner implements Runnable {

        private final List<Table> tables;
        private final Connection connection;
        private final SqlQueryService sqlQueryService;

        @Override
        public void run() {
            tables.forEach(t -> {
                try {
                    var statement = connection.createStatement();
                    statement.execute(sqlQueryService.generateTableCreatingQuery(t));
                    statement.execute(sqlQueryService.generateRowsQuery(t));
                } catch (SQLException throwables) {
                    log.error("ERROR EXECUTE STATEMENT {}", throwables);
                }
            });
        }
    }

}
