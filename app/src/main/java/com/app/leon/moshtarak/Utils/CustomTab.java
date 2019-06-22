package com.app.leon.moshtarak.Utils;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.browser.customtabs.CustomTabsCallback;
import androidx.browser.customtabs.CustomTabsClient;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.browser.customtabs.CustomTabsServiceConnection;
import androidx.browser.customtabs.CustomTabsSession;

import com.app.leon.moshtarak.R;

public class CustomTab {
    private CustomTabsClient mClient;
    private String url;
    private Context context;

    public CustomTab(String url, Context context) {
        this.url = url;
        this.context = context;
        ShowCustomTab();
    }

    private void ShowCustomTab() {
        CustomTabsClient.bindCustomTabsService(context, "com.android.chrome", new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName name, CustomTabsClient client) {
                mClient = client;
                mClient.warmup(0);
                CustomTabsSession session = mClient.newSession(new CustomTabsCallback());
                session.mayLaunchUrl(Uri.parse(url), null, null);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mClient = null;
            }
        });
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(context.getResources().getColor(R.color.colorPrimaryDark));
        builder.setStartAnimations(context, R.anim.slide_up_info, R.anim.no_change);
        builder.setExitAnimations(context, R.anim.no_change, R.anim.slide_down_info);
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.intent.setPackage("com.android.chrome");
        customTabsIntent.intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        customTabsIntent.launchUrl(context, Uri.parse(url));
    }
}
