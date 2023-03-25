package org.example.entity;

public class SubscriberCallsInformation {

    private String callType;
    private String startTime;
    private String endTime;
    private String duration;
    private float cost;

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
