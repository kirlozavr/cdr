package org.example.entity;

/** Сущность с информацией о звонке для отчета **/
public class SubscriberCallsInformation {

    private String callType; // Тип вызова, 01 или 02
    private String startTime; // Время начала звонка
    private String endTime; // Время окончания звонка
    private String duration; // Продолжительность звонка
    private float cost; // Стоимость звонка

    public SubscriberCallsInformation(
            String callType,
            String startTime,
            String endTime,
            String duration,
            float cost
    ) {
        this.callType = callType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
        this.cost = cost;
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

    public String getDuration() {
        return duration;
    }

    public float getCost() {
        return cost;
    }
}
