package com.app.leon.moshtarak.Models.Enums;

/**
 * Created by Leon on 1/10/2018.
 */

public enum BundleEnum {
    TRACK_NUMBER("trackNumber"),
    READ_STATUS("readStatus"),
    ACCOUNT("ACCOUNT"),
    TYPE("type"),
    ON_OFFLOAD("ON_OFFLOAD"),
    POSITION("position"),
    SPINNER_POSITION("spinner_position"),
    COUNTER_STATE_POSITION("counterStatePosition"),
    COUNTER_STATE_CODE("counterStatePosition"),
    LAST_BILL_FROM_COUNTER("last_bill_from_counter"),
    LAST_BILL_TO_FILE("last_bill_from_counter"),
    ID("id"),
    IS_PAYED("is_payed"),
    ZONE_ID("zone_id"),
    THIS_BILL("this_bill"),
    THIS_BILL_PAYED("this_bill_payed"),
    USE_AVERAGE("use_average"),
    USE_LENGTH("use_length"),
    USE("use"),
    USAGE_LITER("usage_liter"),
    CURRENT_READING_DATE("current_reading_date"),
    PRE_READING_DATE("pre_reading_date"),
    DATA("data"),
    BILL_ID("bill_id"),
    PAY_ID("pay_id"),
    PAY_VIEW_MODEL("pay_view_model"),
    NEW("new"),
    PRE("pre"),
    AB_BAHA("ab_baha"),
    TAX("tax"),
    DATE("date"),
    PRE_DEBT_OR_OWE("pre_debt_or_owe"),
    TAKALIF_BOODJE("takalif_boodje"),
    KARMOZDE_FAZELAB("karmozde_fazelab"),
    COST("cost");
    private final String value;

    BundleEnum(final String newValue) {
        value = newValue;
    }

    public String getValue() {
        return value;
    }
}
