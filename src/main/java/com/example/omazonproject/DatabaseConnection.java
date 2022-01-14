package com.example.omazonproject;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * This class is used to connect our project to database
 */
public class DatabaseConnection {

    /**
     * Connection is the session between java application and database
     */
    public Connection databaseLink;

    /**
     * This method is used to get connection from database
     * @return
     */
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