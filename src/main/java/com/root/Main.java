package com.root;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public CommandLineRunner run(DbService service,
                                 SqlThreadExecutor sqlThreadExecutor) {
        return args -> {
            var db = service.generateDb();
            sqlThreadExecutor.execute(db);
        };
    }
}
