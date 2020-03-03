package com.app.leon.moshtarak.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.app.leon.moshtarak.Models.Enums.SharedReferenceKeys;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class SharedPreference {
    Context context;
    private SharedPreferences appPrefs;

    public SharedPreference(Context context) {
        this.context = context;
        appPrefs = this.context.getSharedPreferences("com.app.leon.moshtarak.user_preferences", MODE_PRIVATE);
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

    private void putLength(int index) {
        SharedPreferences.Editor prefsEditor = appPrefs.edit();
        prefsEditor.putInt(SharedReferenceKeys.LENGTH.getValue(), index);
        prefsEditor.apply();
    }

    public void putData(String bill_id, String mobile_number, String api_key) {
        putBillID(bill_id);
        putMobileNumber(mobile_number);
        putApiKey(api_key);
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

    public String getBillID() {
        return appPrefs.getString(SharedReferenceKeys.BILL_ID.getValue(), "");
    }

    public String getApiKey() {
        return appPrefs.getString(SharedReferenceKeys.API_KEY.getValue(), "");
    }

    public int getLength() {
        return appPrefs.getInt(SharedReferenceKeys.LENGTH.getValue(), 0);
    }

    public void saveArrayList(ArrayList<String> list, String key) {
        SharedPreferences.Editor editor = appPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();
    }

    public ArrayList<String> getArrayList(String key) {
        Gson gson = new Gson();
        String json = appPrefs.getString(key, null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    void putDataArray(String mobileNumber, String billId, String apiKey) {
        ArrayList<String> billIds = getArrayList(SharedReferenceKeys.BILL_ID.getValue());
        ArrayList<String> apiKeys = getArrayList(SharedReferenceKeys.API_KEY.getValue());
        ArrayList<String> mobileNumbers = getArrayList(SharedReferenceKeys.MOBILE_NUMBER.getValue());
        int length = getLength();

        billIds.add(billId);
        apiKeys.add(apiKey);
        mobileNumbers.add(mobileNumber);

        saveArrayList(billIds, SharedReferenceKeys.BILL_ID.getValue());
        saveArrayList(mobileNumbers, SharedReferenceKeys.MOBILE_NUMBER.getValue());
        saveArrayList(apiKeys, SharedReferenceKeys.API_KEY.getValue());
        length = length + 1;
        putLength(length);
    }
}
