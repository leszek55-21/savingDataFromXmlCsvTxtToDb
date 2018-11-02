package com.leszekszymaszek.dbConnectionSetup;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionManager {

    // == CONSTANTS ==
    private static final String serverName = "localhost";
    private static final String databaseName ="customer_data";
    private static final int portNumber = 3306;
    private static final String user = "britenet";
    private static final String password = "britenet";

    // == PUBLIC METHODS ==
    public static Connection getConnection() {

        MysqlDataSource dataSource = new MysqlDataSource();

        try {
            dataSource.setUseSSL(false);
            dataSource.setServerName(serverName);
            dataSource.setDatabaseName(databaseName);
            dataSource.setPortNumber(portNumber);
            dataSource.setUser(user);
            dataSource.setPassword(password);

            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

