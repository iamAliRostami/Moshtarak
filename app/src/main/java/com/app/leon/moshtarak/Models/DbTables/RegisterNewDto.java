package com.app.leon.moshtarak.Models.DbTables;

public class RegisterNewDto {
    private String neighbourBillId;
    private String firstName;
    private String sureName;
    private String fatherName;
    private String nationalId;
    private String [] selectedServices;
    private String mobile;
    private String phoneNumber;
    private String address;
    private String postalCode;

    public RegisterNewDto(String neighbourBillId, String firstName, String sureName,
                          String fatherName, String nationalId, String mobile, String phoneNumber,
                          String address, String postalCode, String requestOrigin) {
        this.neighbourBillId = neighbourBillId;
        this.firstName = firstName;
        this.sureName = sureName;
        this.fatherName = fatherName;
        this.nationalId = nationalId;
        this.mobile = mobile;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.postalCode = postalCode;
    }

    public String getNeighbourBillId() {
        return neighbourBillId;
    }

    public void setNeighbourBillId(String neighbourBillId) {
        this.neighbourBillId = neighbourBillId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSureName() {
        return sureName;
    }

    public void setSureName(String sureName) {
        this.sureName = sureName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String[] getSelectedServices() {
        return selectedServices;
    }

    public void setSelectedServices(String []selectedServices) {
        this.selectedServices = selectedServices;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
