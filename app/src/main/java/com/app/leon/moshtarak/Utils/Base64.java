package com.app.leon.moshtarak.Utils;

import java.nio.charset.StandardCharsets;

public class Base64 {
    public Base64() {
    }

    public String encoder(String date) {
        byte[] encrypt = new byte[0];
        encrypt = date.getBytes(StandardCharsets.UTF_8);
        return android.util.Base64.encodeToString(encrypt, android.util.Base64.DEFAULT);
    }

    public String decoder(String date) {
        String decode = null;
        byte[] decrypt = android.util.Base64.decode(date, android.util.Base64.DEFAULT);
        decode = new String(decrypt, StandardCharsets.UTF_8);
        return decode;
    }
}
