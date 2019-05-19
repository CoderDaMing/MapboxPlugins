package com.ming.mapboxplugins.annotationplugin;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
import com.mapbox.mapboxsdk.utils.ColorUtils;
import com.ming.mapboxplugins.AppCons;
import com.ming.mapboxplugins.R;


import java.util.ArrayList;
import java.util.List;

public class LineChangeActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LineChangeActivity";

    private static final LatLng ANDORRA = new LatLng(42.505777, 1.52529);
    private static final LatLng LUXEMBOURG = new LatLng(49.815273, 6.129583);
    private static final LatLng MONACO = new LatLng(43.738418, 7.424616);
    private static final LatLng VATICAN_CITY = new LatLng(41.902916, 12.453389);
    private static final LatLng SAN_MARINO = new LatLng(43.942360, 12.457777);
    private static final LatLng LIECHTENSTEIN = new LatLng(47.166000, 9.555373);

    private static final float FULL_ALPHA = 1.0f;
    private static final float PARTIAL_ALPHA = 0.5f;
    private static final float NO_ALPHA = 0.0f;

    private List<Line> lines;

    private MapView mapView;
    private LineManager lineManager;

    private boolean fullAlpha = true;
    private boolean visible = true;
    private boolean width = true;
    private boolean color = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_change);
        mapView = findViewById(R.id.mapView_line_change);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                Log.d(TAG, "onMapReady");
                mapboxMap.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                                new LatLng(47.798202, 7.573781),
                                4)
                );

                mapboxMap.setStyle(AppCons.MAPBOX_DEFAULT_STYLE, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        // Map is set up and the style has loaded. Now you can add data or make other map adjustments
                        Log.d(TAG, "onStyleLoaded");
                        lineManager = new LineManager(mapView, mapboxMap, style);
                        lines = lineManager.create(getAllPolylines());
                        lineManager.addClickListener(line -> Toast.makeText(
                                LineChangeActivity.this,
                                "Clicked: " + line.getId(),
                                Toast.LENGTH_SHORT).show());

                    }
                });
            }
        });

        findViewById(R.id.btn_remove).setOnClickListener(this);
        findViewById(R.id.btn_fullAlpha).setOnClickListener(this);
        findViewById(R.id.btn_color).setOnClickListener(this);
        findViewById(R.id.btn_width).setOnClickListener(this);
        findViewById(R.id.btn_visible).setOnClickListener(this);
    }

    private List<LineOptions> getAllPolylines() {
        List<LineOptions> options = new ArrayList<>();
        options.add(generatePolyline(ANDORRA, LUXEMBOURG, "#F44336"));
        options.add(generatePolyline(ANDORRA, MONACO, "#FF5722"));
        options.add(generatePolyline(MONACO, VATICAN_CITY, "#673AB7"));
        options.add(generatePolyline(VATICAN_CITY, SAN_MARINO, "#009688"));
        options.add(generatePolyline(SAN_MARINO, LIECHTENSTEIN, "#795548"));
        options.add(generatePolyline(LIECHTENSTEIN, LUXEMBOURG, "#3F51B5"));
        return options;
    }

    private LineOptions generatePolyline(LatLng start, LatLng end, String color) {
        LineOptions line = new LineOptions().withLatLngs(new ArrayList<LatLng>() {{
            add(start);
            add(end);
        }});

        line.withLineColor(ColorUtils.colorToRgbaString(Color.parseColor(color)));
        line.withLineWidth(3.0f);
        return line;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_remove:
                lineManager.delete(lines);
                lines.clear();
                break;
            case R.id.btn_fullAlpha:
                fullAlpha = !fullAlpha;
                for (Line p : lines) {
                    p.setLineOpacity(fullAlpha ? FULL_ALPHA : PARTIAL_ALPHA);
                }
                break;
            case R.id.btn_color:
                color = !color;
                for (Line p : lines) {
                    p.setLineColor(color ? Color.RED : Color.BLUE);
                }
                break;
            case R.id.btn_width:
                width = !width;
                for (Line p : lines) {
                    p.setLineWidth(width ? 3.0f : 5.0f);
                }
                break;
            case R.id.btn_visible:
                visible = !visible;
                for (Line p : lines) {
                    p.setLineOpacity(visible ? (fullAlpha ? FULL_ALPHA : PARTIAL_ALPHA) : NO_ALPHA);
                }
                break;
        }
        lineManager.update(lines);
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
