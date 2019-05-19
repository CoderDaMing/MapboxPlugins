package com.ming.mapboxplugins.annotationplugin;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.Line;
import com.mapbox.mapboxsdk.plugins.annotation.LineManager;
import com.mapbox.mapboxsdk.plugins.annotation.LineOptions;
import com.mapbox.mapboxsdk.plugins.annotation.OnLineDragListener;
import com.mapbox.mapboxsdk.utils.ColorUtils;
import com.ming.mapboxplugins.AppCons;
import com.ming.mapboxplugins.R;


import java.util.ArrayList;
import java.util.List;

public class LineActivity extends AppCompatActivity {
    private static final String TAG = "LineActivity";

    private MapView mapView;
    private LineManager lineManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line);
        mapView = findViewById(R.id.mapView_line);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull final MapboxMap mapboxMap) {
                Log.d(TAG, "onMapReady");

                mapboxMap.setStyle(AppCons.MAPBOX_DEFAULT_STYLE, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        // Map is set up and the style has loaded. Now you can add data or make other map adjustments
                        Log.d(TAG, "onStyleLoaded");
                        //1.
                        mapboxMap.moveCamera(CameraUpdateFactory.zoomTo(2));

                        //2.LineManager
                        lineManager = new LineManager(mapView, mapboxMap, style);
                        lineManager.addClickListener(line -> Toast.makeText(LineActivity.this,
                                String.format("Line clicked %s", line.getId()),
                                Toast.LENGTH_SHORT
                        ).show());
                        lineManager.addLongClickListener(line -> Toast.makeText(LineActivity.this,
                                String.format("Line long clicked %s", line.getId()),
                                Toast.LENGTH_SHORT
                        ).show());

                        lineManager.addDragListener(new OnLineDragListener() {
                            @Override
                            public void onAnnotationDragStarted(Line annotation) {

                            }

                            @Override
                            public void onAnnotationDrag(Line annotation) {

                            }

                            @Override
                            public void onAnnotationDragFinished(Line annotation) {

                            }
                        });

                        //3.create a fixed line
                        List<LatLng> latLngs = new ArrayList<>();
                        latLngs.add(new LatLng(-2.178992, -4.375974));
                        latLngs.add(new LatLng(-4.107888, -7.639772));
                        latLngs.add(new LatLng(2.798737, -11.439207));
                        LineOptions lineOptions = new LineOptions()
                                .withLatLngs(latLngs)
                                .withLineColor(ColorUtils.colorToRgbaString(Color.RED))
                                .setDraggable(true)
                                .withLineWidth(5.0f);

                        lineManager.create(lineOptions);
                    }
                });
            }
        });
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
