package org.example.entity;

import java.util.List;

public class SubscriberEntityReport {

    private String phoneNumber;
    private String tariff;
    private float totalCost;
    private List<SubscriberCallsInformation> callsInformationList;

    public SubscriberEntityReport(
            String phoneNumber,
            String tariff,
            float totalCost,
            List<SubscriberCallsInformation> callsInformationList
    ) {
        this.phoneNumber = phoneNumber;
        this.tariff = tariff;
        this.totalCost = totalCost;
        this.callsInformationList = callsInformationList;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getTariff() {
        return tariff;
    }

    public float getTotalCost() {
        return totalCost;
    }

    public List<SubscriberCallsInformation> getCallsInformationList() {
        return callsInformationList;
    }
}
