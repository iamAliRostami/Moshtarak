package com.app.leon.moshtarak.Models.DbTables;

public class Kardex {
    private String id;
    private String zoneId;
    private String preReadingDate;
    private String currentReadingDate;
    private String duration;
    private String preReadingNumber;
    private String currentReadingNumber;
    private String usage;
    private String oweDate;
    private String amount;
    private String deadLine;
    private String creditorDate;
    private String creditorAmount;
    private String description;
    private String payId;
    private String date;
    private boolean isBill;
    private boolean isPay;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPreReadingDate() {
        return preReadingDate;
    }

    public void setPreReadingDate(String preReadingDate) {
        this.preReadingDate = preReadingDate;
    }

    public String getCurrentReadingDate() {
        return currentReadingDate;
    }

    public void setCurrentReadingDate(String currentReadingDate) {
        this.currentReadingDate = currentReadingDate;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPreReadingNumber() {
        return preReadingNumber;
    }

    public void setPreReadingNumber(String preReadingNumber) {
        this.preReadingNumber = preReadingNumber;
    }

    public String getCurrentReadingNumber() {
        return currentReadingNumber;
    }

    public void setCurrentReadingNumber(String currentReadingNumber) {
        this.currentReadingNumber = currentReadingNumber;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getOweDate() {
        return oweDate;
    }

    public void setOweDate(String oweDate) {
        this.oweDate = oweDate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(String deadLine) {
        this.deadLine = deadLine;
    }

    public String getCreditorDate() {
        return creditorDate;
    }

    public void setCreditorDate(String creditorDate) {
        this.creditorDate = creditorDate;
    }

    public String getCreditorAmount() {
        return creditorAmount;
    }

    public void setCreditorAmount(String creditorAmount) {
        this.creditorAmount = creditorAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isBill() {
        return isBill;
    }

    public void setBill(boolean bill) {
        isBill = bill;
    }

    public boolean isPay() {
        return isPay;
    }

    public void setPay(boolean pay) {
        isPay = pay;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }
}
