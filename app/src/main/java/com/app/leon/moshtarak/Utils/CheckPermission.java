package com.app.leon.moshtarak.Utils;


import android.Manifest;
import android.content.Context;
import android.widget.Toast;

import com.app.leon.moshtarak.Models.Enums.DialogType;
import com.app.leon.moshtarak.R;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

public class CheckPermission {
    private static void finishAffinity() {
        throw new RuntimeException("Stub!");
    }

    public void manage_permissions(final Context context) {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(context, "مجوز ها داده شده", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(context, "مجوز رد شد \n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                forceClose(context);
            }
        };

        new TedPermission(context)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("جهت استفاده بهتر از برنامه مجوز های پیشنهادی را قبول فرمایید")
                .setDeniedMessage("در صورت رد این مجوز قادر با استفاده از این دستگاه نخواهید بود" + "\n" +
                        "لطفا با فشار دادن دکمه" + " " + "اعطای دسترسی" + " " + "و سپس در بخش " + " دسترسی ها" + " " + " با این مجوز هاموافقت نمایید")
                .setGotoSettingButtonText("اعطای دسترسی")
                .setPermissions(
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.INTERNET
                        , Manifest.permission.VIBRATE
                )
                .check();
    }

    private void forceClose(Context context) {
        new CustomDialog(DialogType.Red, context,
                context.getString(R.string.permission_not_completed),
                context.getString(R.string.dear_user),
                context.getString(R.string.call_operator),
                context.getString(R.string.force_close));
        finishAffinity();
    }
}
