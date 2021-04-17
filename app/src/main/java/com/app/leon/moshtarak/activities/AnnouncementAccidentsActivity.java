package com.app.leon.moshtarak.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Debug;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.app.leon.moshtarak.databinding.AnnouncementAccidentsActivityBinding;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.jetbrains.annotations.NotNull;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.app.ActivityCompat;
import androidx.core.location.LocationManagerCompat;

import org.jetbrains.annotations.NotNull;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.MapTileIndex;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import com.app.leon.moshtarak.Models.Enums.SharedReferenceKeys;
import com.app.leon.moshtarak.MyApplication;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.Utils.GPSTracker;
import com.app.leon.moshtarak.Utils.SharedPreference;

import java.util.ArrayList;
import java.util.Objects;

public class AnnouncementAccidentsActivity extends AppCompatActivity {
    AnnouncementAccidentsActivityBinding binding;
    SharedPreference sharedPreference;
    Activity activity;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        binding = AnnouncementAccidentsActivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        activity = this;
        initialize();
        if (gpsEnabled())
            if (checkLocationPermission(activity)) askLocationPermission();
            else initializeMap();
    }

    void initializeMap() {
        Configuration.getInstance().load(activity,
                PreferenceManager.getDefaultSharedPreferences(activity));
        int height = Resources.getSystem().getDisplayMetrics().heightPixels;
        binding.mapView.setMinimumHeight(height / 2);

        binding.mapView.getZoomController().
                setVisibility(CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT);
        binding.mapView.setMultiTouchControls(true);
        IMapController mapController = binding.mapView.getController();
        mapController.setZoom(19.0);
        GPSTracker gpsTracker = new GPSTracker(activity);
        double latitude = gpsTracker.getLatitude();
        double longitude = gpsTracker.getLongitude();
        if (latitude == 0 || longitude == 0) {
            latitude = 32.65;
            longitude = 51.66;
        }
        GeoPoint startPoint = new GeoPoint(latitude, longitude);
        mapController.setCenter(startPoint);
        MyLocationNewOverlay locationOverlay =
                new MyLocationNewOverlay(new GpsMyLocationProvider(activity), binding.mapView);
        locationOverlay.enableMyLocation();
        binding.mapView.getOverlays().add(locationOverlay);
    }

    @SuppressLint("SetTextI18n")
    void initialize() {
        sharedPreference = new SharedPreference(activity);
        if (sharedPreference.getArrayList(
                SharedReferenceKeys.MOBILE_NUMBER.getValue()) != null)
            binding.editTextMobile.setText(sharedPreference.getArrayList(
                    SharedReferenceKeys.MOBILE_NUMBER.getValue()).
                    get(sharedPreference.getIndex()).replaceFirst(getString(R.string._09), ""));
        binding.radioGroup11.setVisibility(View.GONE);
        binding.radioGroup21.setVisibility(View.GONE);
        binding.radioGroup41.setVisibility(View.GONE);
        binding.radioGroup111.setVisibility(View.GONE);
        binding.textViewMessage.setVisibility(View.GONE);
        binding.textViewMessage1.setVisibility(View.GONE);
        binding.buttonSubmit.setVisibility(View.GONE);
        binding.mapView.setVisibility(View.GONE);
        //حادثه آب
        binding.radioButton1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.radioGroup11.setVisibility(View.VISIBLE);
                binding.radioGroup41.setVisibility(View.GONE);
                binding.radioGroup21.setVisibility(View.GONE);
                binding.buttonSubmit.setVisibility(View.GONE);
                binding.mapView.setVisibility(View.GONE);
                binding.textViewMessage.setVisibility(View.GONE);
            } else {
                binding.radioGroup11.clearCheck();
                binding.radioGroup111.clearCheck();
            }
        });
        //حادثه آب داخل منزل
        binding.radioButton11.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.textViewMessage1.setVisibility(View.VISIBLE);
                binding.radioGroup111.setVisibility(View.VISIBLE);
                binding.textViewMessage.setVisibility(View.GONE);
                binding.buttonSubmit.setVisibility(View.GONE);
                binding.mapView.setVisibility(View.GONE);
            } else {
                binding.radioGroup111.clearCheck();
            }
        });
        //خیر
        binding.radioButton111.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.buttonSubmit.setVisibility(View.VISIBLE);
                binding.mapView.setVisibility(View.VISIBLE);
                binding.textViewMessage.setVisibility(View.GONE);
            }
        });
        //بله
        binding.radioButton112.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.buttonSubmit.setVisibility(View.GONE);
                binding.mapView.setVisibility(View.GONE);
                binding.textViewMessage.setVisibility(View.VISIBLE);
                binding.textViewMessage.setText("رفع حادثه بر عهده مشترک می باشد.");
            }
        });
        //حادثه آب خارج از منزل
        binding.radioButton12.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.buttonSubmit.setVisibility(View.VISIBLE);
                binding.mapView.setVisibility(View.VISIBLE);
                binding.textViewMessage.setVisibility(View.VISIBLE);
                binding.textViewMessage1.setVisibility(View.GONE);
                binding.radioGroup111.setVisibility(View.GONE);
                binding.textViewMessage.setText("در صورت مشاهده نشتی آب در داخل فضای سبز با شماره تلفن 137 تماس بگیرید.");
            }
        });
        //فاضلاب
        binding.radioButton2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.textViewMessage1.setVisibility(View.GONE);
                binding.textViewMessage.setVisibility(View.GONE);
                binding.buttonSubmit.setVisibility(View.GONE);
                binding.mapView.setVisibility(View.GONE);
                binding.radioGroup111.setVisibility(View.GONE);
                binding.radioGroup11.setVisibility(View.GONE);
                binding.radioGroup41.setVisibility(View.GONE);
                binding.radioGroup21.setVisibility(View.VISIBLE);
            } else {
                binding.radioGroup21.clearCheck();
            }
        });
        binding.radioButton21.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.buttonSubmit.setVisibility(View.VISIBLE);
                binding.mapView.setVisibility(View.VISIBLE);
                binding.textViewMessage.setVisibility(View.VISIBLE);
                binding.textViewMessage.setText("دریچه های مربوط به شرکت آب  و فاضلاب از نوع دایره ای و نصب شده بر روی منهول های فاضلاب می باشد.");
            }
        });
        binding.radioButton22.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.buttonSubmit.setVisibility(View.VISIBLE);
                binding.mapView.setVisibility(View.VISIBLE);
                binding.textViewMessage.setVisibility(View.GONE);
            }
        });
        binding.radioButton23.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.buttonSubmit.setVisibility(View.VISIBLE);
                binding.mapView.setVisibility(View.VISIBLE);
                binding.textViewMessage.setVisibility(View.VISIBLE);
                binding.textViewMessage.setText("مشترک گرامی در کنار حوضچه کنتور آب شما دریچه ای دایره ای شکل قرار دارد که این دریچه مربوط به سیفون فاضلاب شما می باشد.\nدر صورت مشاهده هر گونه پس زدگی فاضلاب ابتدا دریچه ذکر شده را باز نموده و از باز بودن مسیر فاضلاب مطمئن شوید، در صورت مشاهده پس زدگی از این محل، حادثه خود را ثبت نمایید. در غیر این صورت رفع پس زدگی به عهده مشترک می باشد.");
            }
        });
        binding.radioButton3.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.mapView.setVisibility(View.VISIBLE);
                binding.buttonSubmit.setVisibility(View.VISIBLE);
                binding.radioGroup41.setVisibility(View.GONE);
                binding.radioGroup21.setVisibility(View.GONE);
                binding.radioGroup11.setVisibility(View.GONE);
                binding.radioGroup111.setVisibility(View.GONE);
                binding.textViewMessage1.setVisibility(View.GONE);
                binding.textViewMessage.setVisibility(View.VISIBLE);
                binding.textViewMessage.setText("مشترک گرامی در صورتی که با افت فشار یا قطعی آب مواجه شده اید و مورد شما جزء موارد ذکر شده بالا نمی باشد، ابتدا از آب دار بودن همسایه های مجاور منزل خود (خارج از مجتمع) یا طبقه همکف مطمئن شده و در صورت اطمینان از عدم بدهی قبض مورد خود را اعلام نمایید.");
            }
        });

        binding.radioButton4.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.mapView.setVisibility(View.GONE);
                binding.buttonSubmit.setVisibility(View.GONE);
                binding.textViewMessage1.setVisibility(View.GONE);
                binding.radioGroup111.setVisibility(View.GONE);
                binding.radioGroup21.setVisibility(View.GONE);
                binding.radioGroup11.setVisibility(View.GONE);
                binding.radioGroup41.setVisibility(View.VISIBLE);
                binding.textViewMessage.setVisibility(View.VISIBLE);
                binding.textViewMessage.setText("مشترک گرامی خواهشمند است قبل از اعلام موضوع کیفیت آب، موارد ذیل را بررسی نمایید:\n1. از تمیز بودن آب داخل منزل خود مطمئن شوید.\n2. آب منزل های مجاور را بررسی و مقایسه نمایید.\n3. در صورت وجود کدورت یا گل آلود بودن آب، شیر آب را باز کرده و به مدت پنج تا ده دقیقه صبر نمایید. در صورت مداوم بودن ثبت حادثه نمایید.");
            } else {
                binding.radioGroup41.clearCheck();
            }
        });
        binding.radioButton41.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.buttonSubmit.setVisibility(View.VISIBLE);
                binding.mapView.setVisibility(View.VISIBLE);
            }
        });
        binding.radioButton42.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.buttonSubmit.setVisibility(View.VISIBLE);
                binding.mapView.setVisibility(View.VISIBLE);
            }
        });
        binding.radioButton43.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.buttonSubmit.setVisibility(View.VISIBLE);
                binding.mapView.setVisibility(View.VISIBLE);
            }
        });
    }

    boolean gpsEnabled() {
        LocationManager locationManager = (LocationManager)
                activity.getSystemService(Context.LOCATION_SERVICE);
        boolean enabled =
                LocationManagerCompat.isLocationEnabled(Objects.requireNonNull(locationManager));
        androidx.appcompat.app.AlertDialog.Builder alertDialog = new androidx.appcompat.app.AlertDialog.Builder(new ContextThemeWrapper(activity, R.style.AlertDialogCustom));
        if (!enabled) {
            alertDialog.setCancelable(false);
            alertDialog.setTitle(activity.getString(R.string.gps_setting));
            alertDialog.setMessage(R.string.active_gps);
            alertDialog.setPositiveButton(R.string.setting, (dialog, which) -> {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                activity.startActivityForResult(intent, MyApplication.GPS_CODE);
            });
            alertDialog.setNegativeButton(R.string.exit, (dialog, which) -> {
                dialog.cancel();
                activity.finishAffinity();
            });
            alertDialog.show();
        }
        return enabled;
    }

    boolean checkLocationPermission(Context context) {
        return ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;
    }

    void askLocationPermission() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(activity, R.string.access_granted, Toast.LENGTH_LONG).show();
                initializeMap();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                finishAffinity();
            }
        };
        new TedPermission(this)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage(getString(R.string.confirm_permission))
                .setRationaleConfirmText(getString(R.string.allow_permission))
                .setDeniedMessage(getString(R.string.if_reject_permission))
                .setDeniedCloseButtonText(getString(R.string.exit))
                .setGotoSettingButtonText(getString(R.string.allow_permission))
                .setPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ).check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MyApplication.GPS_CODE) {
            if (resultCode == PackageManager.PERMISSION_GRANTED) {
                if (checkLocationPermission(activity)) askLocationPermission();
                else initializeMap();
            }
        }
    }

    @Override
    protected void onDestroy() {
        Debug.getNativeHeapAllocatedSize();
        System.runFinalization();
        Runtime.getRuntime().totalMemory();
        Runtime.getRuntime().freeMemory();
        Runtime.getRuntime().maxMemory();
        Runtime.getRuntime().gc();
        System.gc();
        android.os.Process.killProcess(android.os.Process.myPid());
        super.onDestroy();
    }
}