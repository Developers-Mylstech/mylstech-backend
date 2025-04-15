package com.mylstech.product;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnectionTest {
    public static void main(String[] args) {
        String url = "jdbc:mysql://root:xIeJtYLzIeOIIXWPPNolLUHpJAwIFjGE@shuttle.proxy.rlwy.net:50712/railway";
        String username = "root";
        String password = "xIeJtYLzIeOIIXWPPNolLUHpJAwIFjGE";

        try {
            System.out.println("Attempting to connect to database...");
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("Database connected!");
            connection.close();
        } catch (Exception e) {
            System.out.println("Connection failed! Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}