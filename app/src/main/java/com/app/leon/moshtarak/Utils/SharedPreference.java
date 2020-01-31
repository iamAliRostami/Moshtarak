package com.app.leon.moshtarak.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.app.leon.moshtarak.Models.Enums.SharedReferenceKeys;

import static android.content.Context.MODE_PRIVATE;

public class SharedPreference {
    Context context;
    private SharedPreferences appPrefs;

    public SharedPreference(Context context) {
        this.context = context;
        appPrefs = this.context.getSharedPreferences("com.app.leon.moshtarak.user_preferences", MODE_PRIVATE);
    }

    private void putAccountNumber(String account_number) {
        SharedPreferences.Editor prefsEditor = appPrefs.edit();
        prefsEditor.putString(SharedReferenceKeys.ACCOUNT_NUMBER.getValue(), account_number);
        prefsEditor.apply();
    }

    private void putBillID(String bill_id) {
        SharedPreferences.Editor prefsEditor = appPrefs.edit();
        prefsEditor.putString(SharedReferenceKeys.BILL_ID.getValue(), bill_id);
        prefsEditor.apply();
    }

    private void putMobileNumber(String mobile_number) {
        SharedPreferences.Editor prefsEditor = appPrefs.edit();
        prefsEditor.putString(SharedReferenceKeys.MOBILE_NUMBER.getValue(), mobile_number);
        prefsEditor.apply();
    }

    private void putApiKey(String mobile_number) {
        SharedPreferences.Editor prefsEditor = appPrefs.edit();
        prefsEditor.putString(SharedReferenceKeys.API_KEY.getValue(), mobile_number);
        prefsEditor.apply();
    }

    public void putData(String bill_id, String mobile_number, String api_key) {
//        putAccountNumber(account_number);
        putBillID(bill_id);
        putMobileNumber(mobile_number);
        putApiKey(api_key);
    }

    public void putData(String bill_id) {
        putBillID(bill_id);
    }
    public boolean checkIsNotEmpty() {
        if (appPrefs == null) {
            return false;
        } else if ( //appPrefs.getString(SharedReferenceKeys.ACCOUNT_NUMBER.getValue(), "").isEmpty() ||
                appPrefs.getString(SharedReferenceKeys.BILL_ID.getValue(), "").isEmpty() ||
                appPrefs.getString(SharedReferenceKeys.MOBILE_NUMBER.getValue(), "").isEmpty() ||
                appPrefs.getString(SharedReferenceKeys.API_KEY.getValue(), "").isEmpty()

        ) {
            return false;
        } else
            return //appPrefs.getString(SharedReferenceKeys.ACCOUNT_NUMBER.getValue(), "").length() >= 1 &&
                    appPrefs.getString(SharedReferenceKeys.BILL_ID.getValue(), "").length() >= 1 &&
                    appPrefs.getString(SharedReferenceKeys.API_KEY.getValue(), "").length() >= 1 &&
                    appPrefs.getString(SharedReferenceKeys.MOBILE_NUMBER.getValue(), "").length() >= 1;
    }

    public String getMobileNumber() {
        return appPrefs.getString(SharedReferenceKeys.MOBILE_NUMBER.getValue(), "");
    }

    public String getAccountNumber() {
        return appPrefs.getString(SharedReferenceKeys.ACCOUNT_NUMBER.getValue(), "");
    }

    public String getBillID() {
        return appPrefs.getString(SharedReferenceKeys.BILL_ID.getValue(), "");
    }

    public String getApiKey() {
        return appPrefs.getString(SharedReferenceKeys.API_KEY.getValue(), "");
    }

}
