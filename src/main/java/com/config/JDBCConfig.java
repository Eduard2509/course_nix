package com.config;

import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;

public abstract class JDBCConfig {
    private static final String URL = "jdbc:postgresql://localhost:5432/course_nix";
    private static final String USER = "postgres";
    private static final String PASSWORD = "root";

    private static Connection connection = null;

    @SneakyThrows
    public static Connection getConnection() {
        if (connection == null) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }
}
