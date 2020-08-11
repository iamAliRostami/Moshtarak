package com.app.leon.moshtarak;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;


public class MyApplication extends MultiDexApplication {

    public static final String fontName = "font/my_font.ttf";
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
