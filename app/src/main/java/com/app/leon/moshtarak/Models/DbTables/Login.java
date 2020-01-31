package com.app.leon.moshtarak.Models.DbTables;

public class Login {
    String billId;
    String billIdCoded;
    String eshterak;
    String nationalId;
    String buildSerial;
    String appVersion;
    String osVersion;
    String mobile;
    String phoneModel;
    String apiKey;
    String nameAndFamily;
    String message;
    int errorCode;


    public Login(String billId, String billIdCoded, String buildSerial, String appVersion,
                 String osVersion, String mobile, String phoneModel) {
        this.billId = billId;
        this.apiKey = billIdCoded;
        this.buildSerial = buildSerial;
        this.appVersion = appVersion;
        this.osVersion = osVersion;
        this.mobile = mobile;
        this.phoneModel = phoneModel;
    }

    public Login(String billId, String eshterak, String nationalId, String buildSerial,
                 String appVersion, String osVersion, String mobile, String phoneModel) {
        this.billId = billId;
        this.eshterak = eshterak;
        this.nationalId = nationalId;
        this.buildSerial = buildSerial;
        this.appVersion = appVersion;
        this.osVersion = osVersion;
        this.mobile = mobile;
        this.phoneModel = phoneModel;
    }

    public Login(String apiKey, String nameAndFamily, String message) {
        this.apiKey = apiKey;
        this.nameAndFamily = nameAndFamily;
        this.message = message;
    }

    public String getBillIdCoded() {
        return billIdCoded;
    }

    public void setBillIdCoded(String billIdCoded) {
        this.billIdCoded = billIdCoded;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getNameAndFamily() {
        return nameAndFamily;
    }

    public void setNameAndFamily(String nameAndFamily) {
        this.nameAndFamily = nameAndFamily;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getEshterak() {
        return eshterak;
    }

    public void setEshterak(String eshterak) {
        this.eshterak = eshterak;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getBuildSerial() {
        return buildSerial;
    }

    public void setBuildSerial(String buildSerial) {
        this.buildSerial = buildSerial;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhoneModel() {
        return phoneModel;
    }

    public void setPhoneModel(String phoneModel) {
        this.phoneModel = phoneModel;
    }
}
