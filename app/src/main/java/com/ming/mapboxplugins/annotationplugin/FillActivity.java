package com.ming.mapboxplugins.annotationplugin;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.plugins.annotation.FillManager;
import com.mapbox.mapboxsdk.plugins.annotation.FillOptions;
import com.mapbox.mapboxsdk.utils.ColorUtils;
import com.ming.mapboxplugins.AppCons;
import com.ming.mapboxplugins.R;


import java.util.ArrayList;
import java.util.List;

public class FillActivity extends AppCompatActivity {
    private MapView mapView;
    private FillManager fillManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill);
        mapView = findViewById(R.id.mapview_fill);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(mapboxMap -> mapboxMap.setStyle(AppCons.MAPBOX_DEFAULT_STYLE, style -> {

            mapboxMap.moveCamera(CameraUpdateFactory.zoomTo(2));

            fillManager = new FillManager(mapView, mapboxMap, style);
            fillManager.addClickListener(fill -> Toast.makeText(FillActivity.this,
                    String.format("Fill clicked %s", fill.getId()),
                    Toast.LENGTH_SHORT
            ).show());
            fillManager.addLongClickListener(fill -> Toast.makeText(FillActivity.this,
                    String.format("Fill long clicked %s", fill.getId()),
                    Toast.LENGTH_SHORT
            ).show());

            // create a fixed fill
            List<LatLng> innerLatLngs = new ArrayList<>();
            innerLatLngs.add(new LatLng(-10.733102, -3.363937));
            innerLatLngs.add(new LatLng(-19.716317, 1.754703));
            innerLatLngs.add(new LatLng(-21.085074, -15.747196));
            innerLatLngs.add(new LatLng(-10.733102, -3.363937));
            List<List<LatLng>> latLngs = new ArrayList<>();
            latLngs.add(innerLatLngs);

            FillOptions fillOptions = new FillOptions()
                    .withLatLngs(latLngs)
                    .withFillColor(ColorUtils.colorToRgbaString(Color.RED));
            fillManager.create(fillOptions);
        }));
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
