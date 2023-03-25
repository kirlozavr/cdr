package org.example.database;

import org.example.entity.SubscriberEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper {
    private static final String CONNECT_DB = "jdbc:sqlite:C:/sqlite/db/subscriber.db";
    public static final String TABLE_NAME = "subscriber";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PHONE_NUMBER = "phone_number";
    public static final String COLUMN_CALL_TYPE = "call_type";
    public static final String COLUMN_START_TIME = "start_time";
    public static final String COLUMN_END_TIME = "end_time";
    public static final String COLUMN_TARIFF = "tariff";
    public static final String CREATE_TABLE_SUBSCRIBER =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_PHONE_NUMBER + " TEXT, "
                    + COLUMN_CALL_TYPE + " TEXT, "
                    + COLUMN_START_TIME + " TEXT, "
                    + COLUMN_END_TIME + " TEXT, "
                    + COLUMN_TARIFF + " TEXT);";

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

    public void crateTable(String query) throws SQLException {
        statement = connection.createStatement();
        statement.execute(query);
        statement.close();
    }

    public void insert(SubscriberEntity subscriberEntity) throws SQLException {
        statement = connection.createStatement();
        statement.execute(
                "INSERT INTO " + TABLE_NAME + " ("
                        + COLUMN_PHONE_NUMBER + ", "
                        + COLUMN_CALL_TYPE + ", "
                        + COLUMN_START_TIME + ", "
                        + COLUMN_END_TIME + ", "
                        + COLUMN_TARIFF + ") VALUES ("
                        + subscriberEntity.getPhoneNumber() + ", "
                        + subscriberEntity.getCallType() + ", "
                        + subscriberEntity.getStartTime() + ", "
                        + subscriberEntity.getEndTime() + ", "
                        + subscriberEntity.getTariff() + ");");
        statement.close();
    }

    public List<SubscriberEntity> getAllEntity() throws SQLException {
        statement = connection.createStatement();
        resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME + ";");
        return getEntityToList();
    }

    public List<SubscriberEntity> getDistinctPhoneNumbers() throws SQLException {
        statement = connection.createStatement();
        resultSet = statement.executeQuery("SELECT DISTINCT " + COLUMN_PHONE_NUMBER + " FROM " + TABLE_NAME + ";");
        return getEntityToList();
    }

    public List<SubscriberEntity> getEntitiesByPhoneNumber(String phoneNumber) throws SQLException {
        statement = connection.createStatement();
        resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME
                + "WHERE " + COLUMN_PHONE_NUMBER + " = " + phoneNumber + ";");
        return getEntityToList();
    }

    private List<SubscriberEntity> getEntityToList() throws SQLException {
        List<SubscriberEntity> entityList = new ArrayList<>();
        while (resultSet.next()){
            SubscriberEntity entity = new SubscriberEntity(
                    resultSet.getString(COLUMN_PHONE_NUMBER),
                    resultSet.getString(COLUMN_CALL_TYPE),
                    resultSet.getString(COLUMN_START_TIME),
                    resultSet.getString(COLUMN_END_TIME),
                    resultSet.getString(COLUMN_TARIFF)
            );
            entityList.add(entity);
        }
        resultSet.close();
        statement.close();
        return entityList;
    }
}
