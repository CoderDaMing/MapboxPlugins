package com.ming.mapboxplugins.markerview;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerView;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager;
import com.ming.mapboxplugins.AppCons;
import com.ming.mapboxplugins.R;

import java.util.Random;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class MarkerViewActivity extends AppCompatActivity implements MapboxMap.OnMapLongClickListener, MapboxMap.OnMapClickListener {
    private Random random = new Random();
    private MarkerViewManager markerViewManager = null;
    private MarkerView marker = null;

    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_view);

        mapView = findViewById(R.id.mapView_markerview);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                mapboxMap.setStyle(AppCons.MAPBOX_DEFAULT_STYLE, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        mapboxMap.moveCamera(CameraUpdateFactory.zoomTo(2.0));

                        markerViewManager = new MarkerViewManager(mapView, mapboxMap);
                        createCustomMarker();
                        createRandomMarkers();

                        mapboxMap.addOnMapLongClickListener(MarkerViewActivity.this);
                        mapboxMap.addOnMapClickListener(MarkerViewActivity.this);
                    }
                });
            }
        });

    }

    private void createCustomMarker() {
        // create a custom animation marker view
        View customView = createCustomAnimationView();
        marker = new MarkerView(new LatLng(), customView);
        markerViewManager.addMarker(marker);
    }


    private View createCustomAnimationView() {
        View customView = LayoutInflater.from(this).inflate(R.layout.marker_view, null);
        customView.setLayoutParams(new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        ImageView icon = customView.findViewById(R.id.imageview);
        View animationView = customView.findViewById(R.id.animation_layout);
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValueAnimator anim = ValueAnimator.ofInt(animationView.getMeasuredWidth(), 350);
                anim.setInterpolator(new AccelerateDecelerateInterpolator());
                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int val = (int) animation.getAnimatedValue();
                        ViewGroup.LayoutParams layoutParams = animationView.getLayoutParams();
                        layoutParams.width = val;
                        animationView.setLayoutParams(layoutParams);
                    }
                });
                anim.setDuration(1250);
                anim.start();
            }
        });
        return customView;
    }

    private void createRandomMarkers() {
        for (int i = 0; i < 19; i++) {
            ImageView imageView = new ImageView(MarkerViewActivity.this);
            imageView.setImageResource(R.drawable.blue_marker);
            imageView.setLayoutParams(new FrameLayout.LayoutParams(56, 56));
            MarkerView markerView = new MarkerView(createRandomLatLng(), imageView);
            markerViewManager.addMarker(markerView);
        }

    }

    private LatLng createRandomLatLng() {
        return new LatLng(random.nextDouble() * -180.0 + 90.0,
                random.nextDouble() * -360.0 + 180.0);
    }


    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        marker.setLatLng(point);
        return false;
    }

    @Override
    public boolean onMapLongClick(@NonNull LatLng point) {
        markerViewManager.removeMarker(marker);
        return false;
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
