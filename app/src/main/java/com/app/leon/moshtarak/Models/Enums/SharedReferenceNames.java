package com.app.leon.moshtarak.Models.Enums;

/**
 * Created by Leon on 1/10/2018.
 */

public enum SharedReferenceNames {
    ESHTERAK_OR_QERAAT("com.app.leon.moshtarak.eshterak_or_qeraat"),
    ACCOUNT("com.app.leon.moshtarak.user_preferences");

    private final String value;

    SharedReferenceNames(final String newValue) {
        value = newValue;
    }

    public String getValue() {
        return value;
    }
}
