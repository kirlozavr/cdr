package org.example.database;

import org.example.entity.SubscriberEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper {

    private static final String DATA_BASE_NAME = "subscriber.db";
    private static final String CONNECT_DB = "jdbc:sqlite:C:\\Users\\kirlo\\IdeaProjects\\cdr\\src\\main\\resources\\database\\";
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
    private PreparedStatement preparedStatement;

    public void connection() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection(CONNECT_DB + DATA_BASE_NAME);
    }

    public void close() throws SQLException {
        connection.close();
        if(statement != null){
            statement.close();
        }
        if(resultSet != null){
            resultSet.close();
        }
        if(preparedStatement != null){
            preparedStatement.close();
        }
    }

    public void crateTable(String query) throws SQLException {
        statement = connection.createStatement();
        statement.execute(query);
    }

    public void insert(SubscriberEntity subscriberEntity) throws SQLException {
        preparedStatement = connection.prepareStatement(
                "INSERT INTO " + TABLE_NAME + " ("
                + COLUMN_PHONE_NUMBER + ", "
                + COLUMN_CALL_TYPE + ", "
                + COLUMN_START_TIME + ", "
                + COLUMN_END_TIME + ", "
                + COLUMN_TARIFF + ") VALUES (?, ?, ?, ?, ?);");
        preparedStatement.setString(1, subscriberEntity.getPhoneNumber());
        preparedStatement.setString(2, subscriberEntity.getCallType());
        preparedStatement.setString(3, subscriberEntity.getStartTime());
        preparedStatement.setString(4, subscriberEntity.getEndTime());
        preparedStatement.setString(5, subscriberEntity.getTariff());
        preparedStatement.executeUpdate();
    }

    public List<SubscriberEntity> getAllEntity() throws SQLException {
        statement = connection.createStatement();
        resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME + ";");
        return getEntityToList();
    }

    public List<String> getDistinctPhoneNumbers() throws SQLException {
        List<String> phoneNumberList = new ArrayList<>();
        statement = connection.createStatement();
        resultSet = statement.executeQuery("SELECT DISTINCT " + COLUMN_PHONE_NUMBER + " FROM " + TABLE_NAME + ";");
        while (resultSet.next()){
            phoneNumberList.add(resultSet.getString(COLUMN_PHONE_NUMBER));
        }
        return phoneNumberList;
    }

    public List<SubscriberEntity> getEntitiesByPhoneNumber(String phoneNumber) throws SQLException {
        preparedStatement = connection.prepareStatement(
                "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_PHONE_NUMBER + " = ?;"
        );
        preparedStatement.setString(1, phoneNumber);
        resultSet = preparedStatement.executeQuery();
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
        return entityList;
    }
}
