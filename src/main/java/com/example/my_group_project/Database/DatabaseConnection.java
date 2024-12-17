package com.example.my_group_project.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String DATABASE_URL = "jdbc:mysql://127.0.0.1:3306/final";
    private static final String DATABASE_USER = "root";
    private static final String DATABASE_PASSWORD = "bunnoob2005";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
    }
}
