package org.example.file_handler;

import org.example.database.DataBaseHelper;
import org.example.entity.SubscriberEntity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

public class FileReaderHandler {

    private static final String FILE_PATH = ".\\src\\main\\resources\\cdr";
    private FileReader fileReader;
    private BufferedReader bufferedReader;
    private DataBaseHelper dataBaseHelper;

    public FileReaderHandler() throws FileNotFoundException {
        fileReader = new FileReader(FILE_PATH);
        bufferedReader = new BufferedReader(fileReader);
        dataBaseHelper = new DataBaseHelper();
    }

    public void readAndInsertToDataBase() throws SQLException, ClassNotFoundException, IOException {
        dataBaseHelper.connection();
        dataBaseHelper.crateTable(DataBaseHelper.CREATE_TABLE_SUBSCRIBER);

        String line;
        while ((line = bufferedReader.readLine()) != null ){
            String[] lineArr = line.split(", ");
            SubscriberEntity subscriberEntity = new SubscriberEntity(
                    lineArr[1],
                    lineArr[0],
                    lineArr[2],
                    lineArr[3],
                    lineArr[4]
            );
            dataBaseHelper.insert(subscriberEntity);
        }
        fileReader.close();
        bufferedReader.close();
        dataBaseHelper.close();
    }
}
