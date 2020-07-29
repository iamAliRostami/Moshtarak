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
//        appPrefs = this.context.getSharedPreferences("com.app.leon.moshtarak.user_preferences", MODE_PRIVATE);
        appPrefs = this.context.getSharedPreferences("com.app.leon.moshtarak.users_preferences", MODE_PRIVATE);
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

    private void putName(String name) {
        SharedPreferences.Editor prefsEditor = appPrefs.edit();
        prefsEditor.putString(SharedReferenceKeys.NAME.getValue(), name);
        prefsEditor.apply();
    }

    private void putLength(int length) {
        SharedPreferences.Editor prefsEditor = appPrefs.edit();
        prefsEditor.putInt(SharedReferenceKeys.LENGTH.getValue(), length);
        prefsEditor.apply();
    }

    public void putIndex(int index) {
        SharedPreferences.Editor prefsEditor = appPrefs.edit();
        prefsEditor.putInt(SharedReferenceKeys.INDEX.getValue(), index);
        prefsEditor.apply();
    }

    public void putTheme(Boolean theme) {
        SharedPreferences.Editor prefsEditor = appPrefs.edit();
        prefsEditor.putBoolean(SharedReferenceKeys.THEME.getValue(), theme);
        prefsEditor.apply();
    }

    public void putCache(Boolean cache) {
        SharedPreferences.Editor prefsEditor = appPrefs.edit();
        prefsEditor.putBoolean(SharedReferenceKeys.CACHE.getValue(), cache);
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
        } else {
            return appPrefs.getInt(SharedReferenceKeys.LENGTH.getValue(), 0) != 0;
        }
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

    public String getName() {
        return appPrefs.getString(SharedReferenceKeys.NAME.getValue(), "");
    }

    public int getLength() {
        return appPrefs.getInt(SharedReferenceKeys.LENGTH.getValue(), 0);
    }

    public int getIndex() {
        return appPrefs.getInt(SharedReferenceKeys.INDEX.getValue(), 0);
    }

    public boolean getTheme() {
        return appPrefs.getBoolean(SharedReferenceKeys.THEME.getValue(), false);
    }

    public boolean getCache() {
        return appPrefs.getBoolean(SharedReferenceKeys.CACHE.getValue(), false);
    }

    public void removeItem(int index) {
        int length = getLength();
        if (index < length) {
            ArrayList<String> billIds = getArrayList(SharedReferenceKeys.BILL_ID.getValue());
            ArrayList<String> apiKeys = getArrayList(SharedReferenceKeys.API_KEY.getValue());
            ArrayList<String> mobileNumbers = getArrayList(SharedReferenceKeys.MOBILE_NUMBER.getValue());
            ArrayList<String> name = getArrayList(SharedReferenceKeys.NAME.getValue());

            billIds.remove(index);
            mobileNumbers.remove(index);
            apiKeys.remove(index);
            name.remove(index);

            saveArrayList(billIds, SharedReferenceKeys.BILL_ID.getValue());
            saveArrayList(mobileNumbers, SharedReferenceKeys.MOBILE_NUMBER.getValue());
            saveArrayList(apiKeys, SharedReferenceKeys.API_KEY.getValue());
            saveArrayList(name, SharedReferenceKeys.NAME.getValue());

            length = length - 1;
            index = length - 1;

            putIndex(index);
            putLength(length);
        }
    }

    private void saveArrayList(ArrayList<String> list, String key) {
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

    public void putDataArray(String mobileNumber, String billId, String apiKey, String name) {
        int length = getLength();
        ArrayList<String> billIds = new ArrayList<>();
        ArrayList<String> apiKeys = new ArrayList<>();
        ArrayList<String> mobileNumbers = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        if (length > 0) {
            billIds = getArrayList(SharedReferenceKeys.BILL_ID.getValue());
            apiKeys = getArrayList(SharedReferenceKeys.API_KEY.getValue());
            mobileNumbers = getArrayList(SharedReferenceKeys.MOBILE_NUMBER.getValue());
            names = getArrayList(SharedReferenceKeys.NAME.getValue());
        }
        billIds.add(billId);
        apiKeys.add(apiKey);
        mobileNumbers.add(mobileNumber);
        names.add(name);

        saveArrayList(billIds, SharedReferenceKeys.BILL_ID.getValue());
        saveArrayList(mobileNumbers, SharedReferenceKeys.MOBILE_NUMBER.getValue());
        saveArrayList(apiKeys, SharedReferenceKeys.API_KEY.getValue());
        saveArrayList(names, SharedReferenceKeys.NAME.getValue());
        putIndex(length);
        length = length + 1;
        putLength(length);
    }
}
