package com.app.leon.moshtarak.Models.DbTables;

public class Request {
    private String airId;
    private String requestTitles;
    private String requestDateAsk;
    private String requestParNumber;
    private String requestDateRegister;

    public String getAirId() {
        return airId;
    }

    public void setAirId(String airId) {
        this.airId = airId;
    }

    public String getRequestTitles() {
        return requestTitles;
    }

    public void setRequestTitles(String requestTitles) {
        this.requestTitles = requestTitles;
    }

    public String getRequestDateAsk() {
        return requestDateAsk;
    }

    public void setRequestDateAsk(String requestDateAsk) {
        this.requestDateAsk = requestDateAsk;
    }

    public String getRequestParNumber() {
        return requestParNumber;
    }

    public void setRequestParNumber(String requestParNumber) {
        this.requestParNumber = requestParNumber;
    }

    public String getRequestDateRegister() {
        return requestDateRegister;
    }

    public void setRequestDateRegister(String requestDateRegister) {
        this.requestDateRegister = requestDateRegister;
    }
}
