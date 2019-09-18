package com.app.leon.moshtarak.Models.DbTables;

public class LastBillInfo {
    private String preReadingDate;
    private String currentReadingDate;
    private String duration;
    private String preReadingNumber;
    private String currentReadingNumber;
    private String usageM3;
    private String usageLiter;
    private String rate;
    private String abBaha;
    private String karmozdFazelab;
    private String maliat;
    private String boodje;
    private String jam;
    private String preDebtOrOwe;
    private String payable;
    private String persianPayable;
    private String deadLine;
    private String bargeNumber;
    private String ehsterak;
    private String radif;
    private String billId;
    private String payId;
    private boolean isPayed;

    private String abBahaDetail;
    private String tabsare2;
    private String tabsare3Ab;
    private String tabsare3Fazelab;
    private String abonmanAb;
    private String abonmanFazelab;
    private String fasleGarm;
    private String mazadOlgoo;
    private String karmozdFazelabDetails;

    private String payableReadable;
    private String bankId;
    private String bankTitle;
    private String payDay;
    private String payTypeId;
    private String payTypeTitle;

    public LastBillInfo(String preReadingDate, String currentReadingDate, String duration,
                        String preReadingNumber, String currentReadingNumber, String usageM3,
                        String usageLiter, String rate, String abBaha, String karmozdFazelab,
                        String maliat, String boodje, String jam, String preDebtOrOwe,
                        String payable, String persianPayable, String deadLine, String bargeNumber,
                        String ehsterak, String radif, String billId, String payId, boolean isPayed) {
        this.preReadingDate = preReadingDate;
        this.currentReadingDate = currentReadingDate;
        this.duration = duration;
        this.preReadingNumber = preReadingNumber;
        this.currentReadingNumber = currentReadingNumber;
        this.usageM3 = usageM3;
        this.usageLiter = usageLiter;
        this.rate = rate;
        this.abBaha = abBaha;
        this.karmozdFazelab = karmozdFazelab;
        this.maliat = maliat;
        this.boodje = boodje;
        this.jam = jam;
        this.preDebtOrOwe = preDebtOrOwe;
        this.payable = payable;
        this.persianPayable = persianPayable;
        this.deadLine = deadLine;
        this.bargeNumber = bargeNumber;
        this.ehsterak = ehsterak;
        this.radif = radif;
        this.billId = billId;
        this.payId = payId;
        this.isPayed = isPayed;
    }

    public boolean isPayed() {
        return isPayed;
    }

    public void setPayed(boolean payed) {
        isPayed = payed;
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

    public String getUsageM3() {
        return usageM3;
    }

    public void setUsageM3(String usageM3) {
        this.usageM3 = usageM3;
    }

    public String getUsageLiter() {
        return usageLiter;
    }

    public void setUsageLiter(String usageLiter) {
        this.usageLiter = usageLiter;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getAbBaha() {
        return abBaha;
    }

    public void setAbBaha(String abBaha) {
        this.abBaha = abBaha;
    }

    public String getKarmozdFazelab() {
        return karmozdFazelab;
    }

    public void setKarmozdFazelab(String karmozdFazelab) {
        this.karmozdFazelab = karmozdFazelab;
    }

    public String getMaliat() {
        return maliat;
    }

    public void setMaliat(String maliat) {
        this.maliat = maliat;
    }

    public String getBoodje() {
        return boodje;
    }

    public void setBoodje(String boodje) {
        this.boodje = boodje;
    }

    public String getJam() {
        return jam;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }

    public String getPreDebtOrOwe() {
        return preDebtOrOwe;
    }

    public void setPreDebtOrOwe(String preDebtOrOwe) {
        this.preDebtOrOwe = preDebtOrOwe;
    }

    public String getPayable() {
        return payable;
    }

    public void setPayable(String payable) {
        this.payable = payable;
    }

    public String getPersianPayable() {
        return persianPayable;
    }

    public void setPersianPayable(String persianPayable) {
        this.persianPayable = persianPayable;
    }

    public String getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(String deadLine) {
        this.deadLine = deadLine;
    }

    public String getBargeNumber() {
        return bargeNumber;
    }

    public void setBargeNumber(String bargeNumber) {
        this.bargeNumber = bargeNumber;
    }

    public String getEhsterak() {
        return ehsterak;
    }

    public void setEhsterak(String ehsterak) {
        this.ehsterak = ehsterak;
    }

    public String getRadif() {
        return radif;
    }

    public void setRadif(String radif) {
        this.radif = radif;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public String getAbBahaDetail() {
        return abBahaDetail;
    }

    public void setAbBahaDetail(String abBahaDetail) {
        this.abBahaDetail = abBahaDetail;
    }

    public String getTabsare2() {
        return tabsare2;
    }

    public void setTabsare2(String tabsare2) {
        this.tabsare2 = tabsare2;
    }

    public String getTabsare3Ab() {
        return tabsare3Ab;
    }

    public void setTabsare3Ab(String tabsare3Ab) {
        this.tabsare3Ab = tabsare3Ab;
    }

    public String getTabsare3Fazelab() {
        return tabsare3Fazelab;
    }

    public void setTabsare3Fazelab(String tabsare3Fazelab) {
        this.tabsare3Fazelab = tabsare3Fazelab;
    }

    public String getAbonmanAb() {
        return abonmanAb;
    }

    public void setAbonmanAb(String abonmanAb) {
        this.abonmanAb = abonmanAb;
    }

    public String getAbonmanFazelab() {
        return abonmanFazelab;
    }

    public void setAbonmanFazelab(String abonmanFazelab) {
        this.abonmanFazelab = abonmanFazelab;
    }

    public String getFasleGarm() {
        return fasleGarm;
    }

    public void setFasleGarm(String fasleGarm) {
        this.fasleGarm = fasleGarm;
    }

    public String getMazadOlgoo() {
        return mazadOlgoo;
    }

    public void setMazadOlgoo(String mazadOlgoo) {
        this.mazadOlgoo = mazadOlgoo;
    }

    public String getKarmozdFazelabDetails() {
        return karmozdFazelabDetails;
    }

    public void setKarmozdFazelabDetails(String karmozdFazelabDetails) {
        this.karmozdFazelabDetails = karmozdFazelabDetails;
    }

    public String getPayableReadable() {
        return payableReadable;
    }

    public void setPayableReadable(String payableReadable) {
        this.payableReadable = payableReadable;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getBankTitle() {
        return bankTitle;
    }

    public void setBankTitle(String bankTitle) {
        this.bankTitle = bankTitle;
    }

    public String getPayDay() {
        return payDay;
    }

    public void setPayDay(String payDay) {
        this.payDay = payDay;
    }

    public String getPayTypeId() {
        return payTypeId;
    }

    public void setPayTypeId(String payTypeId) {
        this.payTypeId = payTypeId;
    }

    public String getPayTypeTitle() {
        return payTypeTitle;
    }

    public void setPayTypeTitle(String payTypeTitle) {
        this.payTypeTitle = payTypeTitle;
    }
}
