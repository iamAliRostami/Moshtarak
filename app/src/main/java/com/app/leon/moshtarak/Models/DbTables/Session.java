package com.app.leon.moshtarak.Models.DbTables;

public class Session {
    private String registerDayJalali;
    private String osVersion;
    private String phoneModel;

    public String getRegisterDayJalali() {
        return registerDayJalali;
    }

    public void setRegisterDayJalali(String registerDayJalali) {
        this.registerDayJalali = registerDayJalali;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getPhoneModel() {
        return phoneModel;
    }

    public void setPhoneModel(String phoneModel) {
        this.phoneModel = phoneModel;
    }
}