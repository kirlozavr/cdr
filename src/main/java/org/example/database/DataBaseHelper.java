package org.example.database;

import java.sql.*;

public class DataBaseHelper {
    private static final String CONNECT_DB = "jdbc:sqlite:C:/sqlite/db/subscriber.db";

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    public void connection() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection(CONNECT_DB);
    }

    public void close() throws SQLException {
        connection.close();
    }

    public void crateTable(String tableName) throws SQLException {
        statement = connection.createStatement();

    }
}
