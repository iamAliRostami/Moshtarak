package com.app.leon.moshtarak;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;


public class MyApplication extends MultiDexApplication {

    public static int position = 0;
    @SuppressLint("StaticFieldLeak")
    private static Context sContext;
    public static final String fontName = "font/my_font.ttf";

    public static String getFontName() {
        return fontName;
    }

    public static Context getContext() {
        return sContext;
    }

    @Override
    public void onCreate() {
        sContext = getApplicationContext();
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }
}
