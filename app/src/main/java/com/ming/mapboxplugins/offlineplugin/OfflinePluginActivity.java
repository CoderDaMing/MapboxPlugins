package com.ming.mapboxplugins.offlineplugin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.geometry.VisibleRegion;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.offline.OfflineTilePyramidRegionDefinition;
import com.mapbox.mapboxsdk.plugins.offline.model.NotificationOptions;
import com.mapbox.mapboxsdk.plugins.offline.model.OfflineDownloadOptions;
import com.mapbox.mapboxsdk.plugins.offline.offline.OfflineDownloadChangeListener;
import com.mapbox.mapboxsdk.plugins.offline.offline.OfflinePlugin;
import com.mapbox.mapboxsdk.plugins.offline.utils.OfflineUtils;
import com.ming.mapboxplugins.AppCons;
import com.ming.mapboxplugins.R;


import java.util.List;

public class OfflinePluginActivity extends AppCompatActivity implements OfflineDownloadChangeListener {
    private static final String TAG = "OfflinePlugin";
    private MapView mapView;
    private MapboxMap mapboxMap;

    private EditText et_name;
    private boolean isCanDownLoad = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);
        mapView = findViewById(R.id.mapView_offline);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                OfflinePluginActivity.this.mapboxMap = mapboxMap;
                mapboxMap.setStyle(AppCons.MAPBOX_DEFAULT_STYLE, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        isCanDownLoad = true;
                    }
                });
            }
        });

        et_name = findViewById(R.id.et_name);
    }

    public void downLoad(View view) {
        if (!isCanDownLoad) {
            Toast.makeText(this, "style未加载！！！", Toast.LENGTH_SHORT).show();
            return;
        }
        VisibleRegion visibleRegion = mapboxMap.getProjection().getVisibleRegion();
        LatLngBounds latLngBounds = visibleRegion.latLngBounds;
        // Define region of map tiles
        OfflineTilePyramidRegionDefinition definition = new OfflineTilePyramidRegionDefinition(
                AppCons.MAPBOX_DEFAULT_STYLE,
                new LatLngBounds.Builder()
                        .include(new LatLng(latLngBounds.getLatNorth(), latLngBounds.getLonEast()))
                        .include(new LatLng(latLngBounds.getLatSouth(), latLngBounds.getLonWest()))
                        .build(),
                mapboxMap.getMinZoomLevel(),
                mapboxMap.getMaxZoomLevel(),
                getResources().getDisplayMetrics().density
        );

        // Customize the download notification's appearance
        NotificationOptions notificationOptions = NotificationOptions.builder(this)
                .smallIconRes(R.drawable.mapbox_logo_icon)
                .returnActivity(OfflinePluginActivity.class.getName())
                .build();

        // Start downloading the map tiles for offline use
        OfflinePlugin.getInstance(this).startDownload(
                OfflineDownloadOptions.builder()
                        .definition(definition)
                        .metadata(OfflineUtils.convertRegionName(et_name.getText().toString().trim()))
                        .notificationOptions(notificationOptions)
                        .build()
        );

        OfflinePlugin.getInstance(this).addOfflineDownloadStateChangeListener(this);
    }


    public void stopDownload(View view) {
        List<OfflineDownloadOptions> offlineDownloadOptionsList = OfflinePlugin.getInstance(this).getActiveDownloads();
        for (OfflineDownloadOptions options : offlineDownloadOptionsList) {
            OfflinePlugin.getInstance(this).cancelDownload(options);
        }
    }

    //region 生命周期
    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();

        OfflinePlugin.getInstance(this).removeOfflineDownloadStateChangeListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
    //endregion

    //region 下载监听
    @Override
    public void onCreate(OfflineDownloadOptions offlineDownload) {
        Log.d(TAG,"onCreate==>");
    }

    @Override
    public void onSuccess(OfflineDownloadOptions offlineDownload) {
        Log.d(TAG,"onSuccess==>");
    }

    @Override
    public void onCancel(OfflineDownloadOptions offlineDownload) {
        Log.d(TAG,"onCancel==>");
    }

    @Override
    public void onError(OfflineDownloadOptions offlineDownload, String error, String message) {
        Log.d(TAG,"onError==>error: " + error + " message: " + message);
    }

    @Override
    public void onProgress(OfflineDownloadOptions offlineDownload, int progress) {
        Log.d(TAG,"onProgress==>"+progress);
    }
    //endregion
}
