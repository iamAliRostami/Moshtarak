package com.app.leon.moshtarak.Models.DbTables;

public class PayInfo {
    long RRN;
    int TrcNo;
    String CardNo;
    String PayDTime;

    public long getRRN() {
        return RRN;
    }

    public void setRRN(long RRN) {
        this.RRN = RRN;
    }

    public int getTrcNo() {
        return TrcNo;
    }

    public void setTrcNo(int trcNo) {
        TrcNo = trcNo;
    }

    public String getCardNo() {
        return CardNo;
    }

    public void setCardNo(String cardNo) {
        CardNo = cardNo;
    }

    public String getPayDTime() {
        return PayDTime;
    }

    public void setPayDTime(String payDTime) {
        PayDTime = payDTime;
    }
}
