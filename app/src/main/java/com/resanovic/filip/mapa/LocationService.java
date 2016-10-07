package com.resanovic.filip.mapa;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.resanovic.filip.mapa.database.WalkDatabaseHelper;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LocationService extends Service implements LocationListener, SensorEventListener {
    private LocationsStorage storage;
    private LocationManager locationManager;
    private Criteria criteria;
    private SensorManager manager;
    private Sensor sensor;
    private String provider;
    private Location previous;
    private SharedPreferences preferences;
    private boolean firstLocation;
    private WalkDatabaseHelper helper;
    private PowerManager.WakeLock wakeLock;
    private int weight;
    int stepCounter;
    private String date;
    private double latStart;
    private double lonStart;
    private double latCurr;
    private double lonCurr;

    public LocationService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        storage = new LocationsStorage(getApplicationContext());
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = manager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        provider = locationManager.getBestProvider(criteria, false);
        date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        helper = new WalkDatabaseHelper(this);
        preferences = getSharedPreferences("PreferenceData", Context.MODE_PRIVATE);
        PowerManager mgr = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock");
        wakeLock.acquire();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this, "gps not turned", Toast.LENGTH_SHORT).show();
        }
        locationManager.requestLocationUpdates(provider, 1000, 5, this);
        manager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        weight = intent.getIntExtra("weight", 0);
        firstLocation = true;
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationManager.removeUpdates(this);
        }
        if (firstLocation == false) {
            double distance = storage.getDistance();
            DecimalFormat df = new DecimalFormat("#.##");
            df.setRoundingMode(RoundingMode.CEILING);
            distance = Double.valueOf(df.format(distance));
            int calories = (int) (distance * 0.000621371 * weight * 2.205 * 0.57);
            helper.insertData(date, distance, stepCounter, calories, latStart, lonStart, latCurr, lonCurr, storage.getId());
            wakeLock.release();
        } else {
            wakeLock.release();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location.getLatitude() == 0.0 || location.getLongitude() == 0.0) {
            return;
        }
        if (firstLocation == true) {
            previous = location;
            storage.writeData(location.getLatitude(), location.getLongitude(), true);
            latStart = location.getLatitude();
            lonStart = location.getLongitude();
            firstLocation = false;
            return;
        }
        if (location.distanceTo(previous) > 10) {
            storage.writeData(location.getLatitude(), location.getLongitude(), false);
            previous = location;
        }
        latCurr = location.getLatitude();
        lonCurr = location.getLongitude();

    }



    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        stepCounter++;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
