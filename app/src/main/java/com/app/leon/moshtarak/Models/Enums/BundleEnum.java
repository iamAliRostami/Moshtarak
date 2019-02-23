package com.app.leon.moshtarak.Models.Enums;

/**
 * Created by Leon on 1/10/2018.
 */

public enum BundleEnum {
    TRACK_NUMBER("trackNumber"),
    DATA("data"),
    READ_STATUS("readStatus"),
    THEME("theme"),
    ACCOUNT("ACCOUNT"),
    TYPE("type"),
    ON_OFFLOAD("ON_OFFLOAD"),
    POSITION("position"),
    SPINNER_POSITION("spinner_position"),
    COUNTER_STATE_POSITION("counterStatePosition"),
    COUNTER_STATE_CODE("counterStatePosition"),
    NUMBER("counterStateCode"),
    ESHTERAK_OR_QERAAT("eshterak_or_qeraat"),
    CURRENT_PAGE("number"),

    BILL_ID("bill_id"),
    PAY_ID("pay_id"),
    NEW("new"),
    PRE("pre"),
    AB_BAHA("ab_baha"),
    TAX("tax"),
    DATE("date"),
    COST("cost");
    private final String value;

    BundleEnum(final String newValue) {
        value = newValue;
    }

    public String getValue() {
        return value;
    }
}
