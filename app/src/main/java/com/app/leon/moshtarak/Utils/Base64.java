package com.app.leon.moshtarak.Utils;

import android.annotation.SuppressLint;

import java.nio.charset.StandardCharsets;

public class Base64 {
    public Base64() {
    }

    @SuppressLint("NewApi")
    public String encoder(String date) {
        byte[] encrypt;
        encrypt = date.getBytes(StandardCharsets.UTF_8);
        return android.util.Base64.encodeToString(encrypt, android.util.Base64.DEFAULT);
    }

    @SuppressLint("NewApi")
    public String decoder(String date) {
        String decode;
        byte[] decrypt = android.util.Base64.decode(date, android.util.Base64.DEFAULT);
        decode = new String(decrypt, StandardCharsets.UTF_8);
        return decode;
    }
}
