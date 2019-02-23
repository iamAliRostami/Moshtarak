package com.app.leon.moshtarak.Models.DbTables;

public class WaterBillLight {
    private int AbonmanFazelab;
    private int KarmozdFazelab;
    private int AbBaha;
    private int Maliat;
    private String Tarix;
    private int Jam;
    private int AbonmanAb;
    private int Tabsare2;
    private int Tabsare3Ab;
    private int Tabsare3Fazelab;
    private int Boodje;
    private float Rate;

    public WaterBillLight(int abonmanFazelab, int karmozdFazelab, int abBaha, int maliat,
                          String tarix, int jam, int abonmanAb, int tabsare2, int tabsare3Ab,
                          int tabsare3Fazelab, int boodje, float rate) {
        AbonmanFazelab = abonmanFazelab;
        KarmozdFazelab = karmozdFazelab;
        AbBaha = abBaha;
        Maliat = maliat;
        Tarix = tarix;
        Jam = jam;
        AbonmanAb = abonmanAb;
        Tabsare2 = tabsare2;
        Tabsare3Ab = tabsare3Ab;
        Tabsare3Fazelab = tabsare3Fazelab;
        Boodje = boodje;
        Rate = rate;
    }

    public int getAbonmanFazelab() {

        return AbonmanFazelab;
    }

    public void setAbonmanFazelab(int abonmanFazelab) {
        AbonmanFazelab = abonmanFazelab;
    }

    public int getKarmozdFazelab() {
        return KarmozdFazelab;
    }

    public void setKarmozdFazelab(int karmozdFazelab) {
        KarmozdFazelab = karmozdFazelab;
    }

    public int getAbBaha() {
        return AbBaha;
    }

    public void setAbBaha(int abBaha) {
        AbBaha = abBaha;
    }

    public int getMaliat() {
        return Maliat;
    }

    public void setMaliat(int maliat) {
        Maliat = maliat;
    }

    public String getTarix() {
        return Tarix;
    }

    public void setTarix(String tarix) {
        Tarix = tarix;
    }

    public int getJam() {
        return Jam;
    }

    public void setJam(int jam) {
        Jam = jam;
    }

    public int getAbonmanAb() {
        return AbonmanAb;
    }

    public void setAbonmanAb(int abonmanAb) {
        AbonmanAb = abonmanAb;
    }

    public int getTabsare2() {
        return Tabsare2;
    }

    public void setTabsare2(int tabsare2) {
        Tabsare2 = tabsare2;
    }

    public int getTabsare3Ab() {
        return Tabsare3Ab;
    }

    public void setTabsare3Ab(int tabsare3Ab) {
        Tabsare3Ab = tabsare3Ab;
    }

    public int getTabsare3Fazelab() {
        return Tabsare3Fazelab;
    }

    public void setTabsare3Fazelab(int tabsare3Fazelab) {
        Tabsare3Fazelab = tabsare3Fazelab;
    }

    public int getBoodje() {
        return Boodje;
    }

    public void setBoodje(int boodje) {
        Boodje = boodje;
    }

    public float getRate() {
        return Rate;
    }

    public void setRate(float rate) {
        Rate = rate;
    }
}
