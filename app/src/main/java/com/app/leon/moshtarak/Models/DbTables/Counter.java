package com.app.leon.moshtarak.Models.DbTables;

public class Counter {
    String billId;
    String counterclaim;
    String notificationMobile;
    int requestOrigin;

    public Counter(String billId, String counterclaim, String notificationMobile, int requestOrigin) {
        this.billId = billId;
        this.counterclaim = counterclaim;
        this.notificationMobile = notificationMobile;
        this.requestOrigin = requestOrigin;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getCounterclaim() {
        return counterclaim;
    }

    public void setCounterclaim(String counterclaim) {
        this.counterclaim = counterclaim;
    }

    public String getNotificationMobile() {
        return notificationMobile;
    }

    public void setNotificationMobile(String notificationMobile) {
        this.notificationMobile = notificationMobile;
    }

    public int getRequestOrigin() {
        return requestOrigin;
    }

    public void setRequestOrigin(int requestOrigin) {
        this.requestOrigin = requestOrigin;
    }
}
