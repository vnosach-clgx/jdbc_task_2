package com.root;

import com.github.javafaker.Faker;
import com.root.model.SqlType;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Supplier;

@Configuration
@PropertySource("classpath:sql.properties")
public class Config {

    @Bean(value = "customProperties")
    @SneakyThrows
    public Properties property() {
        var path1 = Optional.ofNullable(System.getProperty("path")).orElse("sql.properties");
        var path = new ClassPathResource(path1);
        var properties = new Properties();
        properties.load(path.getInputStream());
        return properties;
    }

    @Bean
    @Scope("prototype")
    @SneakyThrows
    public Connection getConnection() {
        return DriverManager.getConnection(property().getProperty("jdbc.url"),
                property().getProperty("jdbc.username"), property().getProperty("jdbc.password"));
    }

    @Bean
    public Map<SqlType, Supplier<Object>> typeSupplierMap() {
        var faker = new Faker();
        var simpleTimeFormat = new SimpleDateFormat("HH:mm:ss");
        var simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return Map.of(
                SqlType.DATE, () -> "'" + simpleDateFormat.format(faker.date().birthday()) + "'",
                SqlType.TIME, () -> "'" + simpleTimeFormat.format(faker.date().birthday()) + "'",
                SqlType.VARCHAR, () -> "'" + faker.lorem().word() + "'",
                SqlType.BOOLEAN, () -> faker.bool().bool(),
                SqlType.INTEGER, () -> faker.number().randomDigit()
        );
    }

}
