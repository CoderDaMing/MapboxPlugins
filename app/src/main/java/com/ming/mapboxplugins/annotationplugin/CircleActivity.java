package com.ming.mapboxplugins.annotationplugin;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.plugins.annotation.Circle;
import com.mapbox.mapboxsdk.plugins.annotation.CircleManager;
import com.mapbox.mapboxsdk.plugins.annotation.CircleOptions;
import com.mapbox.mapboxsdk.plugins.annotation.OnCircleDragListener;
import com.mapbox.mapboxsdk.utils.ColorUtils;
import com.ming.mapboxplugins.AppCons;
import com.ming.mapboxplugins.R;


/**
 * Activity showcasing adding circles using the annotation plugin
 */
public class CircleActivity extends AppCompatActivity {
    private MapView mapView;
    private CircleManager circleManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle);
        mapView = findViewById(R.id.mapView_circle);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(mapboxMap -> mapboxMap.setStyle(AppCons.MAPBOX_DEFAULT_STYLE, style -> {
            mapboxMap.moveCamera(CameraUpdateFactory.zoomTo(2));

            // create circle manager
            circleManager = new CircleManager(mapView, mapboxMap, style);
            circleManager.addClickListener(circle -> Toast.makeText(CircleActivity.this,
                    String.format("Circle clicked %s", circle.getId()),
                    Toast.LENGTH_SHORT
            ).show());
            circleManager.addLongClickListener(circle -> Toast.makeText(CircleActivity.this,
                    String.format("Circle long clicked %s", circle.getId()),
                    Toast.LENGTH_SHORT
            ).show());

            circleManager.addDragListener(new OnCircleDragListener() {
                @Override
                public void onAnnotationDragStarted(Circle annotation) {

                }

                @Override
                public void onAnnotationDrag(Circle annotation) {

                }

                @Override
                public void onAnnotationDragFinished(Circle annotation) {

                }
            });

            // create a fixed circle
            CircleOptions circleOptions = new CircleOptions()
                    .withLatLng(new LatLng(6.687337, 0.381457))
                    .withCircleColor(ColorUtils.colorToRgbaString(Color.YELLOW))
                    .withCircleRadius(20f)
                    .setDraggable(true);
            circleManager.create(circleOptions);
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
