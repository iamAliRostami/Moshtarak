package com.app.leon.moshtarak;

import android.app.Application;

import androidx.multidex.MultiDex;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(getApplicationContext());
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("font/BYekan_3.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

}
