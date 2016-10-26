package com.jeffersmv.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DaoFactory {
    public DaoFactory() throws DaoException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new DaoException(e);
        }
    }

    public static Connection getConnection() throws DaoException {
        Properties prop = new Properties();
        Connection connection;
        try {
//            Class.forName("com.mysql.jdbc.Driver");
            prop.load(DaoFactory.class.getClassLoader().getResourceAsStream("connect.properties"));
            connection = DriverManager.getConnection(prop.getProperty("dburl"), prop.getProperty("user"), prop.getProperty("password"));
        } catch (Exception e) {
            throw new DaoException(e);
        }
        return connection;
    }

    public static void closeConnection(Connection connection) throws DaoException {
        if (connection == null) return;
        try {
            connection.close();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
