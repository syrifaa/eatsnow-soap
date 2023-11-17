package com.eatsnowsoap.core;

import java.sql.*;

import io.github.cdimascio.dotenv.Dotenv;

public class Database {
    private Connection connection;
    private Dotenv dotenv = Dotenv.load();
    private String DB_URL = dotenv.get("DB_URL");
    private String DB_USER = dotenv.get("MYSQL_USER");
    private String DB_PASS = dotenv.get("MYSQL_PASSWORD");

    public Database(){
        try{
            this.connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            System.out.println("Connected to the database!");
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("Failed to connect to the database!");
        }
    }

    public Connection getConnection() {
        return this.connection;
    }

    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}