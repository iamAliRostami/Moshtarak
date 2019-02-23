package com.app.leon.moshtarak.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SharedPreference {
    Context context;
    SharedPreferences appPrefs;

    public SharedPreference(Context context) {
        this.context = context;
        appPrefs = this.context.getSharedPreferences("com.app.leon.moshtarak.user_preferences", MODE_PRIVATE);
    }

    public void saveData(String account_number, String bill_id) {
        putBillID(bill_id);
        putAccountNumber(account_number);
    }

    private void putAccountNumber(String account_number) {
        SharedPreferences.Editor prefsEditor = appPrefs.edit();
        prefsEditor.putString("account_number", account_number);
        prefsEditor.apply();
    }

    private void putBillID(String bill_id) {
        SharedPreferences.Editor prefsEditor = appPrefs.edit();
        prefsEditor.putString("bill_id", bill_id);
        prefsEditor.apply();
    }

    public void putData(String account_number, String bill_id) {
        putAccountNumber(account_number);
        putBillID(bill_id);

    }

    public boolean checkIsNotEmpty() {
        if (appPrefs == null) {
            return false;
        } else if (appPrefs.getString("account_number", "").isEmpty() ||
                appPrefs.getString("bill_id", "").isEmpty()
                ) {
            return false;
        } else if (appPrefs.getString("account_number", "").length() < 1 ||
                appPrefs.getString("bill_id", "").length() < 1
                ) {
            return false;
        }
        return true;
    }

    public String getFileNumber() {
        return appPrefs.getString("file_number", "");
    }

    public String getAccountNumber() {
        return appPrefs.getString("account_number", "");
    }

    public String getBillID() {
        return appPrefs.getString("bill_id", "");
    }

    public interface accessData {
        void AccessData();
    }
}
