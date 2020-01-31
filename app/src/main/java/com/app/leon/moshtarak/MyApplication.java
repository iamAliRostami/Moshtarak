package com.app.leon.moshtarak;

import androidx.multidex.MultiDexApplication;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class MyApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
//        MultiDex.install(getApplicationContext());
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("font/BYekan_3.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            return;
//        }
//        LeakCanary.install(this);
    }
//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        MultiDex.install(this);
//    }
}
