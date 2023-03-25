package org.example.entity;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class SubscriberEntity {

    private String phoneNumber;
    private String callType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String tariff;
    private static final String FORMAT_TIME = "HH:mm:ss";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(FORMAT_TIME);

    public SubscriberEntity(
            String phoneNumber,
            String callType,
            LocalDateTime startTime,
            LocalDateTime endTime,
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public String getTariff() {
        return tariff;
    }

    public int getDurationToHours(){
        return Duration.between(startTime, endTime).toHoursPart();
    }

    public int getDurationToMinutes(){
        return Duration.between(startTime, endTime).toMinutesPart();
    }

    public int getDurationToSeconds(){
        return Duration.between(startTime, endTime).toSecondsPart();
    }

    public String getDurationToString(){
        LocalTime localTime = LocalTime.of(
                getDurationToHours(),
                getDurationToMinutes(),
                getDurationToSeconds()
        );
        return DATE_TIME_FORMATTER.format(localTime);
    }

}
