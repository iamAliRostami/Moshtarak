package com.app.leon.moshtarak.Models.DbTables;

import java.util.ArrayList;

public class TrackingDto {
    String aidId;
    String billId;
    String dateJalali;
    String description;
    String hour;
    String id;
    String minute;
    String status;
    ArrayList<String> smsList;

    public String getAidId() {
        return aidId;
    }

    public void setAidId(String aidId) {
        this.aidId = aidId;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getDateJalali() {
        return dateJalali;
    }

    public void setDateJalali(String dateJalali) {
        this.dateJalali = dateJalali;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<String> getSmsList() {
        return smsList;
    }

    public void setSmsList(ArrayList<String> smsList) {
        this.smsList = smsList;
    }

    public String getTime() {
        return getHour() + ":" + getMinute();
    }
}
