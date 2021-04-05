package com.app.leon.moshtarak;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;


public class MyApplication extends MultiDexApplication {

    public static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    public static final long MIN_TIME_BW_UPDATES = 10000;
    public static final long FASTEST_INTERVAL = 5;
    public static final int GPS_CODE = 1231;
    public static final String fontName = "font/my_font.ttf";
    public static boolean isHome = false;
    public static boolean doubleBackToExitPressedOnce = false;
    public static int position = 0;
    @SuppressLint("StaticFieldLeak")
    private static Context sContext;
//    private RefWatcher refWatcher;

    public static String getFontName() {
        return fontName;
    }

    public static Context getContext() {
        return sContext;
    }

//    public static RefWatcher getRefWatcher(Context context) {
//        MyApplication application = (MyApplication) context.getApplicationContext();
//        return application.refWatcher;
//    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
//        if (BuildConfig.DEBUG) {
//            if (LeakCanary.isInAnalyzerProcess(this)) {
//                return;
//            }
//            refWatcher = LeakCanary.install(this);
//        }
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }
}
