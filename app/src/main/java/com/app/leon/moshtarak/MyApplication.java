package com.app.leon.moshtarak;

import android.app.Application;
import android.content.Context;


public class MyApplication extends Application {

    private static Context sContext;

    public static Context getContext() {
        return sContext;
    }

    @Override
    public void onCreate() {
        sContext = getApplicationContext();
        super.onCreate();
//        ViewPump.init(ViewPump.builder()
//                .addInterceptor(new CalligraphyInterceptor(
//                        new CalligraphyConfig.Builder()
//                                .setDefaultFontPath("font/my_font.ttf")
//                                .setFontAttrId(R.attr.fontPath)
//                                .build()))
//                .build());
    }
}
