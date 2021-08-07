package com.academiamoviles.distrack.services;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.academiamoviles.distrack.Constants;
import com.academiamoviles.distrack.activities.TareasActivity;

public class AMLocationManager implements LocationListener {

    private Location lastLocation;
    private Activity activity;
    private boolean init = false;
    private AlertDialog alertNoLastLocation;
    private AlertDialog alertNoGPS;
    private boolean tareasUpdate = false;
    private TareasActivity tareasActivity = null;

    public boolean isInit() {
        return init;
    }

    public void setInit(boolean init) {
        this.init = init;
    }

    private static final AMLocationManager ourInstance = new AMLocationManager();

    public static AMLocationManager getInstance() {
        return ourInstance;
    }

    private AMLocationManager() {

    }

    public void init() {
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 0, this);
    }

    public void checkLastLocation() {
        if (lastLocation == null) {
            if (alertNoLastLocation == null) {
                alertNoLastLocation = buildAlertMessageNoLocation();
            }
            if (!alertNoLastLocation.isShowing()) {
                alertNoLastLocation = buildAlertMessageNoLocation();
                alertNoLastLocation.show();
            }
        }
    }

    public void checkLastLocation2(TareasActivity tareasActivity) {
        this.tareasActivity = tareasActivity;
        tareasUpdate = true;
        if (lastLocation == null) {
            if (alertNoLastLocation == null) {
                alertNoLastLocation = buildAlertMessageNoLocation();
            }
            if (!alertNoLastLocation.isShowing()) {
                alertNoLastLocation = buildAlertMessageNoLocation();
                alertNoLastLocation.show();
            }
        }
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public boolean statusCheck() {
        final LocationManager manager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, Constants.PERMISSIONS_LIST_GPS, Constants.UBICACION_REQUEST);
            return false;
        } else if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (alertNoGPS == null) {
                alertNoGPS = buildAlertMessageNoGps();
            }
            if (!alertNoGPS.isShowing()) {
                alertNoGPS = buildAlertMessageNoGps();
                alertNoGPS.show();
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (alertNoLastLocation.isShowing()) {
            alertNoLastLocation.dismiss();
        }
        lastLocation = location;
        if (tareasUpdate) {
            tareasUpdate = false;
            tareasActivity.actualizarDatos();
        }
        Log.d(Constants.TAG, "onLocationChanged: " + location.getLatitude());
        Log.d(Constants.TAG, "onLocationChanged: " + location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
        AMLocationManager.getInstance().checkLastLocation();
        if (alertNoGPS.isShowing()) {
            alertNoGPS.dismiss();
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        if (alertNoGPS == null) {
            alertNoGPS = buildAlertMessageNoGps();
        }
        if (!alertNoGPS.isShowing()) {
            alertNoGPS = buildAlertMessageNoGps();
            alertNoGPS.show();
        }
    }

    private AlertDialog buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Tu GPS está desactivado, para usar el app tiene que activarlo")
                .setCancelable(false)
                .setPositiveButton("Activar", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        activity.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });
        return builder.create();
    }

    private AlertDialog buildAlertMessageNoLocation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(Constants.MENSAJE_NOLOCATION)
                .setCancelable(false);
        return builder.create();
    }

    public Location getLastLocation() {
        return lastLocation;
    }

}
