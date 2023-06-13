package org.example.entity;

import java.util.List;

/** Сущность для формирования отчета **/
public class SubscriberEntityReport {

    private String phoneNumber; // Номер телефона
    private String tariff; // Индекс тарифа
    private float totalCost; // Полная стоимость по итогам тарификации
    private List<SubscriberCallsInformation> callsInformationList; // Список сущностей звонков совершенных данным абонентом

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
