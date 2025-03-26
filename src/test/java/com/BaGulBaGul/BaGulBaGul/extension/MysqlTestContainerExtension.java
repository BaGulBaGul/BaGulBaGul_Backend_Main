package com.BaGulBaGul.BaGulBaGul.extension;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.MySQLContainer;

public class MysqlTestContainerExtension implements BeforeAllCallback {
    private static final String MYSQL_USERNAME = System.getenv().get("MYSQL_USERNAME");
    private static final String MYSQL_PASSWORD = System.getenv().get("MYSQL_PASSWORD");
    private static MySQLContainer mysqlContainer = new MySQLContainer("mysql:8")
            .withDatabaseName("bagulbagul")
            .withUsername(MYSQL_USERNAME)
            .withPassword(MYSQL_PASSWORD);

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        if(!mysqlContainer.isRunning()) {
            mysqlContainer.start();
        }
        System.setProperty("spring.datasource.driver-class-name", mysqlContainer.getDriverClassName());
        System.setProperty("spring.datasource.url", mysqlContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", mysqlContainer.getUsername());
        System.setProperty("spring.datasource.password", mysqlContainer.getPassword());
    }
}
