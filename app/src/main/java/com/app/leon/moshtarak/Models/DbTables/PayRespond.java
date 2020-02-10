package com.app.leon.moshtarak.Models.DbTables;

public class PayRespond {
    short Status;
    String Message;
    Data Data;

    public short getStatus() {
        return Status;
    }

    public void setStatus(short status) {
        Status = status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public com.app.leon.moshtarak.Models.DbTables.Data getData() {
        return Data;
    }

    public void setData(com.app.leon.moshtarak.Models.DbTables.Data data) {
        Data = data;
    }
}
