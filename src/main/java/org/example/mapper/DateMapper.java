package org.example.mapper;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateMapper {

    private static final String FORMAT_TIME = "HH:mm:ss";
    private static final String FORMAT_DATE_INPUT = "yyyyMMddHHmmss";
    private static final String FORMAT_DATE_AND_TIME_OUTPUT = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(FORMAT_TIME);
    private static final DateTimeFormatter DATE_TIME_FORMATTER_INPUT = DateTimeFormatter.ofPattern(FORMAT_DATE_INPUT);
    private static final DateTimeFormatter DATE_TIME_FORMATTER_OUTPUT = DateTimeFormatter.ofPattern(FORMAT_DATE_AND_TIME_OUTPUT);

    /** Получить LocalDateTime из текстового формата **/
    public static LocalDateTime getStringToLocaleDateTime(String dateTime) {
        return LocalDateTime.parse(dateTime, DATE_TIME_FORMATTER_INPUT);
    }

    /** Текстовый формат из LocalDateTime **/
    public static String getLocalDateTimeToStringInput(LocalDateTime localDateTime) {
        return DATE_TIME_FORMATTER_INPUT.format(localDateTime);
    }

    public static String getLocalDateTimeToStringOutput(LocalDateTime localDateTime) {
        return DATE_TIME_FORMATTER_OUTPUT.format(localDateTime);
    }

    /** Получить текстовый формат yyyy-MM-dd HH:mm:ss из yyyyMMddHHmmss **/
    public static String getStringToDateTimeString(String dateTime){
        return DateMapper.getLocalDateTimeToStringOutput(
                DateMapper.getStringToLocaleDateTime(dateTime)
        );
    }

    /** Получить продолжительность звонка в минутах, каждая последующая секунда считается как новая минута **/
    public static int getDurationToMinute(String startTime, String endTime){
        final long durationSeconds = DateMapper.getDurationSeconds(startTime, endTime);
        final int minutes = (int) ((durationSeconds % 3600) / 60);
        final int seconds = (int) (durationSeconds % 60);
        return seconds > 0 ? minutes + 1 : minutes;
    }

    /** Получить продолжительность звонка в текстовом формате **/
    public static String getDurationToString(String startTime, String endTime) {
        final long durationSeconds = DateMapper.getDurationSeconds(startTime, endTime);

        final int hours = (int) (durationSeconds / 3600);
        final int minutes = (int) ((durationSeconds % 3600) / 60);
        final int seconds = (int) (durationSeconds % 60);
        LocalTime localTime = LocalTime.of(hours, minutes, seconds);
        return TIME_FORMATTER.format(localTime);
    }

    /** Получить общее количество продолжительности звонка в секундах **/
    private static long getDurationSeconds(String startTime, String endTime) {
        return Duration.between(
                DateMapper.getStringToLocaleDateTime(startTime),
                DateMapper.getStringToLocaleDateTime(endTime)
        ).toSeconds();
    }

}
