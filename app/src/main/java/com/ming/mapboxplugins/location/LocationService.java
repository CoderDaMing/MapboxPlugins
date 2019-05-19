package com.ming.mapboxplugins.location;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;

import java.util.List;

public class LocationService extends Service implements LocationEngineCallback<LocationEngineResult> {
    private static final long DEFAULT_INTERVAL_IN_MILLISECONDS = 5000;
    private static final long DEFAULT_MAX_WAIT_TIME = 50000;
    private static final String TAG = "LocationService";
    private LocationEngine locationEngine;

    public LocationService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        locationEngine = LocationEngineProvider.getBestLocationEngine(this);
        Log.d(TAG,"LocationService onCreate==>");
        LocationEngineRequest request = new LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME).build();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        locationEngine.requestLocationUpdates(request, this, getMainLooper());
        Log.d(TAG,"requestLocationUpdates==>");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (locationEngine != null) {
            locationEngine.removeLocationUpdates(this);
        }
    }

    @Override
    public void onSuccess(LocationEngineResult result) {
        // Location logic here
        Location lastLocation = result.getLastLocation();
        Log.d(TAG,"onSuccess lastLocation==>"+lastLocation.toString());
        List<Location> locationList= result.getLocations();
        for (Location loc:locationList) {
            Log.d(TAG,"onSuccess loc==>"+loc.toString());
        }
    }

    @Override
    public void onFailure(@NonNull Exception exception) {
        Log.d(TAG,"onFailure==>"+exception.getMessage());
    }
}
