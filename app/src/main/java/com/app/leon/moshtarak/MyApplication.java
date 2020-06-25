package com.app.leon.moshtarak;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;


public class MyApplication extends Application {

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
}
