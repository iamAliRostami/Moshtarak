package com.app.leon.moshtarak.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.app.leon.moshtarak.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.osmdroid.config.Configuration;

import static com.app.leon.moshtarak.MyApplication.FASTEST_INTERVAL;
import static com.app.leon.moshtarak.MyApplication.MIN_DISTANCE_CHANGE_FOR_UPDATES;
import static com.app.leon.moshtarak.MyApplication.MIN_TIME_BW_UPDATES;

public class GPSTracker extends Service {
    static double latitude;
    static double longitude;
    final Activity activity;
    boolean canGetLocation = false;
    double accuracy;
    final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                addLocation(location);
            }
        }
    };
    final OnSuccessListener<Location> onSuccessListener = this::addLocation;
    boolean checkGPS = false;
    boolean checkNetwork = false;
    Location location;
    LocationManager locationManager;
    final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(@NonNull Location location) {
            if (locationManager != null)
                locationManager.removeUpdates(locationListener);
            addLocation(location);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(@NonNull String provider) {
        }

        public void onProviderDisabled(@NonNull String provider) {
        }
    };
    FusedLocationProviderClient fusedLocationClient;
    LocationRequest locationRequest;

    public GPSTracker(Activity activity) {
        this.activity = activity;
        Configuration.getInstance().load(activity,
                PreferenceManager.getDefaultSharedPreferences(activity));
        if (checkGooglePlayServices()) {
            startFusedLocation();
        } else {
            getLocation();
        }
    }

    void addLocation(Location location) {
        if (location != null && (location.getLatitude() != 0 || location.getLongitude() != 0)) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            accuracy = location.getAccuracy();
        }
    }

    @SuppressLint("MissingPermission")
    void getLocation() {
        try {
            locationManager = (LocationManager) activity
                    .getSystemService(LOCATION_SERVICE);
            // get GPS status
            checkGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            // get network provider status
            checkNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!checkGPS && !checkNetwork) {
                Toast.makeText(activity, getString(R.string.services_is_not_available), Toast.LENGTH_LONG).show();
            } else {
                this.canGetLocation = true;
                if (checkNetwork) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);//TODO
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            accuracy = location.getAccuracy();
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                if (checkGPS && location == null) {
                    if (locationManager != null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);//TODO
                    }
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            accuracy = location.getAccuracy();
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("error on location", e.toString());
        }
        new Handler().postDelayed(this::getLocation, MIN_TIME_BW_UPDATES);
    }

    boolean checkGooglePlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        String message;
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                message = activity.getString(R.string.google_is_available_but_not_installed);
            } else {
                message = activity.getString(R.string.google_is_not_available);
            }
            Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    void startFusedLocation() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(MIN_TIME_BW_UPDATES);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        registerRequestUpdateGoogle();
    }

    @SuppressLint("MissingPermission")
    void registerRequestUpdateGoogle() {
        fusedLocationClient.getLastLocation().addOnSuccessListener(activity, onSuccessListener);
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    public double getAccuracy() {
        return accuracy;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    void stopFusedLocation() {
        if (fusedLocationClient != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    public void stopListener() {
        if (locationManager != null)
            locationManager.removeUpdates(locationListener);
    }

    @Override
    public IBinder onBind(Intent intent) {
        stopFusedLocation();
        stopListener();
        return null;
    }

    @Override
    public void onDestroy() {
        stopFusedLocation();
        stopListener();
        super.onDestroy();
    }
}