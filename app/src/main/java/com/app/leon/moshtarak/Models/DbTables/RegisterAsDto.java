package com.app.leon.moshtarak.Models.DbTables;

import java.util.ArrayList;

public class RegisterAsDto {
    private String billId;
    private ArrayList<String> selectedServices;
    private String notificationMobile;

    public RegisterAsDto(String billId, ArrayList<String> selectedServices, String notificationMobile) {
        this.billId = billId;
        this.selectedServices = selectedServices;
        this.notificationMobile = notificationMobile;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public ArrayList<String> getSelectedServices() {
        return selectedServices;
    }

    public void setSelectedServices(ArrayList<String> selectedServices) {
        this.selectedServices = selectedServices;
    }

    public String getNotificationMobile() {
        return notificationMobile;
    }

    public void setNotificationMobile(String notificationMobile) {
        this.notificationMobile = notificationMobile;
    }
}