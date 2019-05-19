package com.ming.mapboxplugins;

import android.app.Application;

import com.mapbox.mapboxsdk.Mapbox;

public class MyApplication extends Application {
    private static MyApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        // Mapbox Access token
        Mapbox.getInstance(getApplicationContext(), getString(R.string.mapbox_access_token));
    }

    public static MyApplication getInstance() {
        return instance;
    }
}
