package com.ming.mapboxplugins.annotationplugin;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.Fill;
import com.mapbox.mapboxsdk.plugins.annotation.FillManager;
import com.mapbox.mapboxsdk.plugins.annotation.FillOptions;
import com.mapbox.mapboxsdk.utils.ColorUtils;
import com.ming.mapboxplugins.R;


import java.util.ArrayList;
import java.util.List;

import static com.ming.mapboxplugins.annotationplugin.FillChangeActivity.Config.BLUE_COLOR;
import static com.ming.mapboxplugins.annotationplugin.FillChangeActivity.Config.BROKEN_SHAPE_POINTS;
import static com.ming.mapboxplugins.annotationplugin.FillChangeActivity.Config.FULL_ALPHA;
import static com.ming.mapboxplugins.annotationplugin.FillChangeActivity.Config.NO_ALPHA;
import static com.ming.mapboxplugins.annotationplugin.FillChangeActivity.Config.PARTIAL_ALPHA;
import static com.ming.mapboxplugins.annotationplugin.FillChangeActivity.Config.RED_COLOR;
import static com.ming.mapboxplugins.annotationplugin.FillChangeActivity.Config.STAR_SHAPE_HOLES;
import static com.ming.mapboxplugins.annotationplugin.FillChangeActivity.Config.STAR_SHAPE_POINTS;


public class FillChangeActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {
    private FillManager fillManager;
    private Fill fill;

    private MapView mapView;

    private boolean fullAlpha = true;
    private boolean visible = true;
    private boolean color = true;
    private boolean allPoints = true;
    private boolean holes = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_change);
        mapView = findViewById(R.id.mapview_fill_change);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        findViewById(R.id.action_id_alpha).setOnClickListener(this);
        findViewById(R.id.action_id_visible).setOnClickListener(this);
        findViewById(R.id.action_id_points).setOnClickListener(this);
        findViewById(R.id.action_id_color).setOnClickListener(this);
        findViewById(R.id.action_id_holes).setOnClickListener(this);
    }

    @Override
    public void onMapReady(@NonNull MapboxMap map) {
        map.setStyle(new Style.Builder().fromUrl(Style.MAPBOX_STREETS), style -> {
            CameraPosition  cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(45.520486, -122.673541))
                    .zoom(12)
                    .tilt(40)
                    .build();
            map.setCameraPosition(cameraPosition);

            fillManager = new FillManager(mapView, map, style, "aerialway");
            fillManager.addClickListener(fill -> Toast.makeText(
                    FillChangeActivity.this,
                    "Clicked: " + fill.getId(),
                    Toast.LENGTH_SHORT).show());

            fill = fillManager.create(new FillOptions()
                    .withLatLngs(STAR_SHAPE_POINTS)
                    .withFillColor(ColorUtils.colorToRgbaString(BLUE_COLOR))
            );
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_id_alpha:
                fullAlpha = !fullAlpha;
                fill.setFillOpacity(fullAlpha ? FULL_ALPHA : PARTIAL_ALPHA);
                break;
            case R.id.action_id_visible:
                visible = !visible;
                fill.setFillOpacity(visible ? (fullAlpha ? FULL_ALPHA : PARTIAL_ALPHA) : NO_ALPHA);
                break;
            case R.id.action_id_points:
                allPoints = !allPoints;
                fill.setLatLngs(allPoints ? STAR_SHAPE_POINTS : BROKEN_SHAPE_POINTS);
                break;
            case R.id.action_id_color:
                color = !color;
                fill.setFillColor(color ? BLUE_COLOR : RED_COLOR);
                break;
            case R.id.action_id_holes:
                holes = !holes;
                fill.setLatLngs(holes ? STAR_SHAPE_HOLES : STAR_SHAPE_POINTS);
                break;
            default:
                break;
        }
        fillManager.update(fill);
    }

    static final class Config {
        static final int BLUE_COLOR = Color.parseColor("#3bb2d0");
        static final int RED_COLOR = Color.parseColor("#AF0000");

        static final float FULL_ALPHA = 1.0f;
        static final float PARTIAL_ALPHA = 0.5f;
        static final float NO_ALPHA = 0.0f;

        static final List<List<LatLng>> STAR_SHAPE_POINTS = new ArrayList<List<LatLng>>() {
            {
                add(new ArrayList<LatLng>() {{
                    add(new LatLng(45.522585, -122.685699));
                    add(new LatLng(45.534611, -122.708873));
                    add(new LatLng(45.530883, -122.678833));
                    add(new LatLng(45.547115, -122.667503));
                    add(new LatLng(45.530643, -122.660121));
                    add(new LatLng(45.533529, -122.636260));
                    add(new LatLng(45.521743, -122.659091));
                    add(new LatLng(45.510677, -122.648792));
                    add(new LatLng(45.515008, -122.664070));
                    add(new LatLng(45.502496, -122.669048));
                    add(new LatLng(45.515369, -122.678489));
                    add(new LatLng(45.506346, -122.702007));
                    add(new LatLng(45.522585, -122.685699));
                }});
            }
        };

        static final List<List<LatLng>> BROKEN_SHAPE_POINTS =
                new ArrayList<List<LatLng>>() {
                    {
                        add(STAR_SHAPE_POINTS.get(0).subList(0, STAR_SHAPE_POINTS.get(0).size() - 3));
                    }
                };


        static final List<List<LatLng>> STAR_SHAPE_HOLES = new ArrayList<List<LatLng>>() {
            {
                add(STAR_SHAPE_POINTS.get(0));
                add(new ArrayList<>(new ArrayList<LatLng>() {
                    {
                        add(new LatLng(45.521743, -122.669091));
                        add(new LatLng(45.530483, -122.676833));
                        add(new LatLng(45.520483, -122.676833));
                        add(new LatLng(45.521743, -122.669091));
                    }
                }));
                add(new ArrayList<>(new ArrayList<LatLng>() {
                    {
                        add(new LatLng(45.529743, -122.662791));
                        add(new LatLng(45.525543, -122.662791));
                        add(new LatLng(45.525543, -122.660));
                        add(new LatLng(45.527743, -122.660));
                        add(new LatLng(45.529743, -122.662791));
                    }
                }));
            }
        };
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
