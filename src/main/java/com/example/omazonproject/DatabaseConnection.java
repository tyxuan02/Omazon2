package com.example.omazonproject;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    public Connection databaseLink;

    public Connection getConnection() {
        String databaseUser = "sql6455521";
        String databasePassword = "ZTxuMRZIFe";
        String url = "jdbc:mysql://sql6.freemysqlhosting.net/sql6455521";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

        return databaseLink;

    }
}