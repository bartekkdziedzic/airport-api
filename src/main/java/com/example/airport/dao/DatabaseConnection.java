package com.example.airport.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {  public static Connection getConnection() throws SQLException {
    String url = "jdbc:postgresql://localhost:5432/airportdb";
    String username = "postgres";
    String password = "password";

    return DriverManager.getConnection(url, username, password);
}
}
