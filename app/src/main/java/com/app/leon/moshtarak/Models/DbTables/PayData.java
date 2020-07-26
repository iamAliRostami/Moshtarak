package com.app.leon.moshtarak.Models.DbTables;

public class PayData {
    short Status;
    int Message;
    String Token;

    public short getStatus() {
        return Status;
    }

    public void setStatus(short status) {
        Status = status;
    }

    public int getMessage() {
        return Message;
    }

    public void setMessage(int message) {
        Message = message;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }
}
