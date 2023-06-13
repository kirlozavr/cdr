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

    /**
     * Метод для запуска программы
     **/
    public void run() throws SQLException, IOException, ClassNotFoundException {
        readFileAndInsertToDataBase();
        writeReportFiles();
        closeDB();
    }

    /**
     * Метод для чтения файла cdr и записи информации в БД
     **/
    private void readFileAndInsertToDataBase() throws IOException, SQLException, ClassNotFoundException {
        FileReaderHandler fileReaderHandler =
                new FileReaderHandler();
        fileReaderHandler.readAndInsertToDataBase();
    }

    /**
     * Метод для записи отчетов в файлы
     **/
    private void writeReportFiles() throws IOException, SQLException {
        getDistinctionEntity();

        for (String phoneNumber : phoneNumberList) {
            FileWriterHandler fileWriterHandler = new FileWriterHandler("report_" + phoneNumber);
            SubscriberEntityReport subscriberEntityReport = getSubscriberEntityReport(
                    dataBaseHelper.getEntitiesByPhoneNumber(phoneNumber)
            );
            fileWriterHandler.writeEntityReport(subscriberEntityReport);
        }
    }

    /**
     * Метод получения уникальных номеров из БД, для дальнейшего формирования отчетов
     **/
    private void getDistinctionEntity() throws SQLException {
        phoneNumberList = dataBaseHelper.getDistinctPhoneNumbers();
    }

    /**
     * Метод закрывает соединение с БД
     **/
    private void closeDB() throws SQLException {
        dataBaseHelper.close();
    }

    /**
     * Метод получения сущности для отчета, сущность формируется в зависимости от тарифа
     **/
    private SubscriberEntityReport getSubscriberEntityReport(List<SubscriberEntity> entityList) {
        String phoneNumber = entityList.get(0).getPhoneNumber();
        String tariff = entityList.get(0).getTariff();
        if (tariff.equals("06")) {
            return tariffUnlimited(phoneNumber, tariff, entityList);
        } else if (tariff.equals("03")) {
            return tariffPerMinute(phoneNumber, tariff, entityList);
        } else {
            return tariffOrdinary(phoneNumber, tariff, entityList);
        }
    }

    /**
     * Метод расчета конечной стоимости для тарифа "Поминутный".
     * На вход в метод приходит номер телефона абонента, его тариф и список всех звонков которые он совершил.
     **/
    private SubscriberEntityReport tariffPerMinute(
            String phoneNumber,
            String tariff,
            List<SubscriberEntity> entityList // Список объектов с информацией о звонке из CDR файла
    ) {
        // Полная стоимость услуг
        List<SubscriberCallsInformation> callsInformationList = new ArrayList<>(); // Список с информацией о звонках совершенных этим пользователем
        // В цикле происходит перебор объектов из CDR и формируется новый список с информацией нужного формата для отчета
        for (SubscriberEntity entity : entityList) {
            SubscriberCallsInformation callsInformation = new SubscriberCallsInformation(
                    // Тип вызова
                    entity.getStartTimeOutFormat(), // Начало звонка в нужном формате
                    entity.getEndTimeOutFormat(), // Конец звонка в нужном формате
                    entity.getDurationToString() // Продолжительность звонка в нужном формате
                    // Стоимость звонка
            );

            totalCost += callsInformation // Считается полная стоимость услуг
        }
        // Результатом работы метода явлеяется объект SubscriberEntityReport
        return new SubscriberEntityReport(
                phoneNumber, // Номер телефона
                tariff, // Индекс тарифа
                totalCost, // Полная стоимость услуг
                callsInformationList.stream() // Список звонков совершенных данным абонентом, список сортируется по типу вызова и дате начала звонка
                        .sorted(
                                Comparator.comparing(SubscriberCallsInformation::getCallType)
                                        .thenComparing(SubscriberCallsInformation::getStartTime)
                        ).toList()
        );
    }

    /**
     * Метод расчета конечной стоимости для тарифа "Безлимитный".
     * На вход в метод приходит номер телефона абонента, его тариф и список всех звонков которые он совершил.
     **/
    private SubscriberEntityReport tariffUnlimited(
            String phoneNumber,
            String tariff,
            List<SubscriberEntity> entityList
    ) {
         // Лимит минут для тарифа
        int totalCost = 0; // Полная стоимость услуг
        List<SubscriberCallsInformation> callsInformationList = new ArrayList<>(); // Список с информацией о звонках совершенных этим пользователем

        // В цикле происходит перебор объектов из CDR и формируется новый список с информацией нужного формата для отчета
        for (SubscriberEntity entity : entityList) {
            float cost = 0; // Стоимость звонка

            // Если лимит минут меньше продолжительности звонка и лимит больше 0
            if ( < entity.getDurationToMinutes() &&  > 0) {
                cost =  - entity.getDurationToMinutes();
                totalCost += cost;
                // Если лимит минут меньше продолжительности звонка и лимит меньше либо равен 0
            } else if ( < entity.getDurationToMinutes() &&  <= 0) {
                cost = entity.getDurationToMinutes();
                totalCost += cost;
            }
             -= entity.getDurationToMinutes(); // С каждой итерацией уменьшается лимит минут

            SubscriberCallsInformation callsInformation = new SubscriberCallsInformation(
                    entity.getCallType(), // Тип звонка
                    entity.getStartTimeOutFormat(), // Начало звонка в нужном формате
                    entity.getEndTimeOutFormat(), // Конец звонка в нужном формате
                    entity.getDurationToString() // Продолжительность звонка в нужном формате
                     // Стоимость звонка
            );
            callsInformationList.add(callsInformation);
        }

         // К полной стоимости услуг прибавляется абонентская плата

        return new SubscriberEntityReport(
                phoneNumber, // Номер телефона
                tariff, // Индекс тарифа
                totalCost, // Полная стоимость услуг
                callsInformationList.stream() // Список звонков совершенных данным абонентом
                        .sorted(
                                Comparator.comparing(SubscriberCallsInformation::getCallType)
                                        .thenComparing(SubscriberCallsInformation::getStartTime)
                        ).toList()
        );
    }

    /**
     * Метод расчета конечной стоимости для тарифа "Обычный".
     * На вход в метод приходит номер телефона абонента, его тариф и список всех звонков которые он совершил.
     **/
    private SubscriberEntityReport tariffOrdinary(
            String phoneNumber,
            String tariff,
            List<SubscriberEntity> entityList
    ) {
         // Лимит минут
        float totalCost = 0; // Полная стоимость услуг
        List<SubscriberCallsInformation> callsInformationList = new ArrayList<>(); // Список звонков

        // В цикле перебираются звонки совершенные этим абонентом из CDR файла
        for (SubscriberEntity entity : entityList) {

            float cost = 0; // Стоимость звонка
            if (entity.getCallType().equals("01")) { // Если звонок исходящий
                if ( < entity.getDurationToMinutes() &&  > 0) { // Если лимит минут меньше продолжительности звонка и больше 0
                    cost = ( - entity.getDurationToMinutes()) * 1.5f;
                } else if ( < entity.getDurationToMinutes() &&  <= 0) { // Если лимит минут меньше продолжительности звонка и меньше либо равен 0
                    cost = entity.getDurationToMinutes() * 1.5f;
                } else {
                    cost = entity.getDurationToMinutes()
                }
                totalCost += cost;
                 -= entity.getDurationToMinutes();
            }

            SubscriberCallsInformation callsInformation = new SubscriberCallsInformation(
                    entity.getCallType(), // Тип вызова
                    entity.getStartTimeOutFormat(), // Время начала звонка в нужном формате
                    entity.getEndTimeOutFormat(), // Время окончания звонка в нужном формате
                    entity.getDurationToString(), // Продолжительность звонка в нужном формате
                    cost // Стоимость звонка
            );
            callsInformationList.add(callsInformation);
        }

        return new SubscriberEntityReport(
                phoneNumber, // Номер телефона
                tariff, // Индекс тарифа
                totalCost, // Полная стоимость услуг
                callsInformationList.stream() // Список всех звонков совершенных данным абонентом
                        .sorted(
                                Comparator.comparing(SubscriberCallsInformation::getCallType)
                                        .thenComparing(SubscriberCallsInformation::getStartTime)
                        ).toList()
        );
    }
}
