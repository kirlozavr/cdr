package org.example;

import org.example.database.DataBaseHelper;
import org.example.entity.SubscriberEntity;
import org.example.entity.SubscriberCallsInformation;
import org.example.entity.SubscriberEntityReport;
import org.example.file_handler.FileReaderHandler;
import org.example.file_handler.FileWriterHandler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ReportGenerator {

    private DataBaseHelper dataBaseHelper;
    private List<String> phoneNumberList;

    public ReportGenerator() throws SQLException, ClassNotFoundException {
        dataBaseHelper = new DataBaseHelper();
        dataBaseHelper.connection();
    }

    /** Метод для чтения файла и записи информации в БД **/
    public void readFileAndInsertToDataBase() throws IOException, SQLException, ClassNotFoundException {
        FileReaderHandler fileReaderHandler =
                new FileReaderHandler("C:\\Users\\kirlo\\IdeaProjects\\cdr\\src\\main\\resources\\cdr");
        fileReaderHandler.readAndInsertToDataBase();
    }

    /** Метод для записи отчетов в файлы **/
    public void writeReportFiles() throws IOException, SQLException {
        for(String phoneNumber : phoneNumberList){
            FileWriterHandler fileWriterHandler = new FileWriterHandler("report_" + phoneNumber);
            SubscriberEntityReport subscriberEntityReport = getSubscriberEntityReport(
                    dataBaseHelper.getEntitiesByPhoneNumber(phoneNumber)
            );
            fileWriterHandler.writeEntityReport(subscriberEntityReport);
        }
    }

    /** Метод получения уникальных номеров из БД, для дальнейшего формирования отчетов **/
    public void getDistinctionEntity() throws SQLException {
         phoneNumberList = dataBaseHelper.getDistinctPhoneNumbers();
    }

    public void closeDB() throws SQLException {
        dataBaseHelper.close();
    }

    /** Метод получения сущности для отчета, сущность формируется в зависимости от тарифа **/
    private SubscriberEntityReport getSubscriberEntityReport(List<SubscriberEntity> entityList){
        String phoneNumber = entityList.get(0).getPhoneNumber();
        String tariff = entityList.get(0).getTariff();
        if(tariff.equals("06")){
            return tariffUnlimited(phoneNumber, tariff, entityList);
        } else if (tariff.equals("03")) {
            return tariffPerMinute(phoneNumber, tariff, entityList);
        } else{
            return tariffOrdinary(phoneNumber, tariff, entityList);
        }
    }

    /** Метод расчета конечной стоимости для тарифа "Поминутный" **/
    private SubscriberEntityReport tariffPerMinute(
            String phoneNumber,
            String tariff,
            List<SubscriberEntity> entityList
    ){
        float totalCost = 0;
        List<SubscriberCallsInformation> callsInformationList = new ArrayList<>();
        for(SubscriberEntity entity : entityList){
            SubscriberCallsInformation callsInformation = new SubscriberCallsInformation(
                    entity.getCallType(),
                    entity.getStartTimeOutFormat(),
                    entity.getEndTimeOutFormat(),
                    entity.getDurationToString(),
                    (entity.getDurationToMinutes() * 1.5f)
            );
            callsInformationList.add(callsInformation);
            totalCost += callsInformation.getCost();
        }
        return new SubscriberEntityReport(
                phoneNumber,
                tariff,
                totalCost,
                callsInformationList
                        .stream()
                        .sorted(
                                Comparator.comparing(SubscriberCallsInformation::getCallType)
                                        .thenComparing(SubscriberCallsInformation::getStartTime)
                        ).toList()
        );
    }

    /** Метод расчета конечной стоимости для тарифа "Безлимитный" **/
    private SubscriberEntityReport tariffUnlimited(
            String phoneNumber,
            String tariff,
            List<SubscriberEntity> entityList
    ){
        int totalMinute = 300;
        int totalCost = 0;
        List<SubscriberCallsInformation> callsInformationList = new ArrayList<>();

        for(SubscriberEntity entity : entityList){
            float cost = 0;

            if(totalMinute < entity.getDurationToMinutes() && totalMinute > 0){
                cost = totalMinute - entity.getDurationToMinutes();
                totalCost += cost;
            } else if (totalMinute < entity.getDurationToMinutes() && totalMinute <= 0) {
                cost = entity.getDurationToMinutes();
                totalCost += cost;
            }
            totalMinute -= entity.getDurationToMinutes();

            SubscriberCallsInformation callsInformation = new SubscriberCallsInformation(
                    entity.getCallType(),
                    entity.getStartTimeOutFormat(),
                    entity.getEndTimeOutFormat(),
                    entity.getDurationToString(),
                    cost
            );
            callsInformationList.add(callsInformation);
        }

        if (totalCost > 0){
            totalCost += 100;
        }

        return new SubscriberEntityReport(
                phoneNumber,
                tariff,
                totalCost,
                callsInformationList
                        .stream()
                        .sorted(
                                Comparator.comparing(SubscriberCallsInformation::getCallType)
                                        .thenComparing(SubscriberCallsInformation::getStartTime)
                        ).toList()
        );
    }

    /** Метод расчета конечной стоимости для тарифа "Обычный" **/
    private SubscriberEntityReport tariffOrdinary(
            String phoneNumber,
            String tariff,
            List<SubscriberEntity> entityList
    ){
        float totalMinute = 100;
        float totalCost = 0;
        List<SubscriberCallsInformation> callsInformationList = new ArrayList<>();

        for(SubscriberEntity entity : entityList){

            float cost = 0;
            if(entity.getCallType().equals("01")){
                if(totalMinute < entity.getDurationToMinutes() && totalMinute > 0){
                    cost = (totalMinute - entity.getDurationToMinutes()) * 1.5f;
                } else if (totalMinute < entity.getDurationToMinutes() && totalMinute <= 0) {
                    cost = entity.getDurationToMinutes() * 1.5f;
                } else {
                    cost = entity.getDurationToMinutes() * 0.5f;
                }
                totalCost += cost;
            }

            SubscriberCallsInformation callsInformation = new SubscriberCallsInformation(
                    entity.getCallType(),
                    entity.getStartTimeOutFormat(),
                    entity.getEndTimeOutFormat(),
                    entity.getDurationToString(),
                    cost
            );
            callsInformationList.add(callsInformation);
        }

        return new SubscriberEntityReport(
                phoneNumber,
                tariff,
                totalCost,
                callsInformationList
                        .stream()
                        .sorted(
                                Comparator.comparing(SubscriberCallsInformation::getCallType)
                                        .thenComparing(SubscriberCallsInformation::getStartTime)
                        ).toList()
        );
    }
}
