package org.example.entity;

import org.example.mapper.DateMapper;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/** Сущность с информацией из CDR **/
public class SubscriberEntity {

    private String phoneNumber; // Номер телефона
    private String callType; // Тип вызова
    private String startTime; // Время начала звонка
    private String endTime; // Время конца звонка
    private String tariff; // Индекс тарифа
    private static final String FORMAT_TIME = "HH:mm:ss";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(FORMAT_TIME);

    public SubscriberEntity(
            String phoneNumber,
            String callType,
            String startTime,
            String endTime,
            String tariff
    ) {
        this.phoneNumber = phoneNumber;
        this.callType = callType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.tariff = tariff;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCallType() {
        return callType;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getTariff() {
        return tariff;
    }

    public String getStartTimeOutFormat() { // Метод возвращает время начала звонка в нужном формате для отчета
        return DateMapper.getLocalDateTimeToStringInput(
                DateMapper.getStringToLocaleDateTime(
                        getStartTime()
                )
        );
    }

    public String getEndTimeOutFormat() { // Метод возвращает время конца звонка в нужном формате для отчета
        return DateMapper.getLocalDateTimeToStringInput(
                DateMapper.getStringToLocaleDateTime(
                        getEndTime()
                )
        );
    }

    public int getDurationToHours() { // Метод возвращает продолжительность звонка в часах
        return Duration.between(
                DateMapper.getStringToLocaleDateTime(startTime),
                DateMapper.getStringToLocaleDateTime(endTime)
        ).toHoursPart();
    }

    public int getDurationToMinutes() { // Метод возвращает продолжительность звонка в минутах
        return Duration.between(
                DateMapper.getStringToLocaleDateTime(startTime),
                DateMapper.getStringToLocaleDateTime(endTime)
        ).toMinutesPart();
    }

    public int getDurationToSeconds() { // Метод возвращает продолжительность звонка в секундах
        return Duration.between(
                DateMapper.getStringToLocaleDateTime(startTime),
                DateMapper.getStringToLocaleDateTime(endTime)
        ).toSecondsPart();
    }

    public String getDurationToString() { // Метод возвращает продолжительность звонка для отчета
        LocalTime localTime = LocalTime.of(
                getDurationToHours(),
                getDurationToMinutes(),
                getDurationToSeconds()
        );
        return DATE_TIME_FORMATTER.format(localTime);
    }

}
