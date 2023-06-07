package org.example.entity;

import org.example.mapper.DateMapper;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class SubscriberEntity {

    private String phoneNumber;
    private String callType;
    private String startTime;
    private String endTime;
    private String tariff;
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

    public String getStartTimeOutFormat() {
        return DateMapper.getLocalDateTimeToStringInput(
                DateMapper.getStringToLocaleDateTime(
                        getStartTime()
                )
        );
    }

    public String getEndTimeOutFormat() {
        return DateMapper.getLocalDateTimeToStringInput(
                DateMapper.getStringToLocaleDateTime(
                        getEndTime()
                )
        );
    }

    public int getDurationToHours() {
        return Duration.between(
                DateMapper.getStringToLocaleDateTime(startTime),
                DateMapper.getStringToLocaleDateTime(endTime)
        ).toHoursPart();
    }

    public int getDurationToMinutes() {
        return Duration.between(
                DateMapper.getStringToLocaleDateTime(startTime),
                DateMapper.getStringToLocaleDateTime(endTime)
        ).toMinutesPart();
    }

    public int getDurationToSeconds() {
        return Duration.between(
                DateMapper.getStringToLocaleDateTime(startTime),
                DateMapper.getStringToLocaleDateTime(endTime)
        ).toSecondsPart();
    }

    public String getDurationToString() {
        LocalTime localTime = LocalTime.of(
                getDurationToHours(),
                getDurationToMinutes(),
                getDurationToSeconds()
        );
        return DATE_TIME_FORMATTER.format(localTime);
    }

}
