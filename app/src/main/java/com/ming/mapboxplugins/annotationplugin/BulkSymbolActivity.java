package com.ming.mapboxplugins.annotationplugin;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.ming.mapboxplugins.AppCons;
import com.ming.mapboxplugins.R;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.ref.WeakReference;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import timber.log.Timber;

public class BulkSymbolActivity extends AppCompatActivity {
    private SymbolManager symbolManager;
    private List<Symbol> symbols = new ArrayList<>();

    private MapboxMap mapboxMap;
    private MapView mapView;
    private FeatureCollection locations;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bulk_symbol);
        mapView = findViewById(R.id.mapview_bulk_symbol);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this::initMap);
    }

    private void initMap(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                        new LatLng(38.87031, -77.00897), 10
                )
        );

        mapboxMap.setStyle(AppCons.MAPBOX_DEFAULT_STYLE, style -> {
            symbolManager = new SymbolManager(mapView, mapboxMap, style);
            symbolManager.setIconAllowOverlap(true);
            loadData(3);
        });
    }

    private void loadData(int position) {
        int amount = Integer.valueOf(getResources().getStringArray(R.array.bulk_marker_list)[position]);
        if (locations == null) {
            progressDialog = ProgressDialog.show(this, "Loading", "Fetching markers", false);
            new LoadLocationTask(this, amount).execute();
        } else {
            showMarkers(amount);
        }
    }

    private void onLatLngListLoaded(FeatureCollection featureCollection, int amount) {
        progressDialog.hide();
        locations = featureCollection;
        showMarkers(amount);
    }

    private void showMarkers(int amount) {
        if (mapboxMap == null || locations == null || locations.features() == null || mapView.isDestroyed()) {
            return;
        }

        // delete old symbols
        symbolManager.delete(symbols);

        if (locations.features().size() < amount) {
            amount = locations.features().size();
        }

        showSymbols(amount);
    }

    private void showSymbols(int amount) {
        List<SymbolOptions> options = new ArrayList<>();
        Random random = new Random();
        int randomIndex;

        List<Feature> features = locations.features();
        if (features == null) {
            return;
        }

        for (int i = 0; i < amount; i++) {
            randomIndex = random.nextInt(features.size());
            Feature feature = features.get(randomIndex);
            options.add(new SymbolOptions()
                    .withGeometry((Point) feature.geometry())
                    .withIconImage("fire-station-11")
            );
        }
        symbols = symbolManager.create(options);
    }

    private static class LoadLocationTask extends AsyncTask<Void, Integer, FeatureCollection> {

        private WeakReference<BulkSymbolActivity> activity;
        private int amount;

        private LoadLocationTask(BulkSymbolActivity activity, int amount) {
            this.amount = amount;
            this.activity = new WeakReference<>(activity);
        }

        @Override
        protected FeatureCollection doInBackground(Void... params) {
            BulkSymbolActivity activity = this.activity.get();
            if (activity != null) {
                String json = null;
                try {
                    json = GeoParseUtil.loadStringFromAssets(activity.getApplicationContext());
                } catch (IOException exception) {
                    Timber.e(exception, "Could not add markers");
                }

                if (json != null) {
                    return FeatureCollection.fromJson(json);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(FeatureCollection locations) {
            super.onPostExecute(locations);
            BulkSymbolActivity activity = this.activity.get();
            if (activity != null) {
                activity.onLatLngListLoaded(locations, amount);
            }
        }
    }

    public static class GeoParseUtil {

        static String loadStringFromAssets(final Context context) throws IOException {
            InputStream is = context.getAssets().open("points.geojson");
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            return readAll(rd);
        }

        private static String readAll(Reader rd) throws IOException {
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            return sb.toString();
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
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
    //endregion
}
