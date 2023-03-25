package org.example.mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateMapper {

    private static final String FORMAT_DATE_INPUT = "yyyyMMddHHmmss";
    private static final String FORMAT_DATE_OUTPUT = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter DATE_TIME_FORMATTER_INPUT = DateTimeFormatter.ofPattern(FORMAT_DATE_INPUT);
    private static final DateTimeFormatter DATE_TIME_FORMATTER_OUTPUT = DateTimeFormatter.ofPattern(FORMAT_DATE_OUTPUT);

    public static LocalDateTime getStringToLocaleDateTime(String dateTime){
        return LocalDateTime.parse(dateTime, DATE_TIME_FORMATTER_INPUT);
    }

    public static String getLocalDateTimeToString(LocalDateTime localDateTime){
        return DATE_TIME_FORMATTER_OUTPUT.format(localDateTime);
    }

}
