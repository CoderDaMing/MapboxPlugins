package com.ming.mapboxplugins.annotationplugin;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.ming.mapboxplugins.AppCons;
import com.ming.mapboxplugins.R;


public class PressForSymbolActivity extends AppCompatActivity implements Style.OnStyleLoaded{

    public static final String ID_ICON = "id-icon";
    private SymbolManager symbolManager;
    private MapView mapView;
    private MapboxMap mapboxMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_press_for_symbol);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        mapView = findViewById(R.id.mapView_press_for_symbol);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(map -> {
            mapboxMap = map;

            mapboxMap.setCameraPosition(new CameraPosition.Builder()
                    .target(new LatLng(60.169091, 24.939876))
                    .zoom(12)
                    .tilt(20)
                    .bearing(90)
                    .build()
            );
            mapboxMap.addOnMapLongClickListener(this::addSymbol);

            mapboxMap.setStyle(new Style.Builder()
                            .fromUrl(AppCons.MAPBOX_DEFAULT_STYLE)
                            .withImage(ID_ICON, generateBitmap(R.drawable.ic_circle)),
                    PressForSymbolActivity.this);
        });
    }


    @Override
    public void onStyleLoaded(@NonNull Style style) {
        symbolManager = new SymbolManager(mapView, mapboxMap, style);
        symbolManager.setIconAllowOverlap(true);
        symbolManager.setTextAllowOverlap(true);
        mapboxMap.addOnMapLongClickListener(this::addSymbol);
    }

    private boolean addSymbol(LatLng point) {
        symbolManager.create(new SymbolOptions()
                .withLatLng(point)
                .withIconImage(ID_ICON)
        );
        return true;
    }

    private Bitmap generateBitmap(@DrawableRes int drawableRes) {
        Drawable drawable = getResources().getDrawable(drawableRes);
        return getBitmapFromDrawable(drawable);
    }

    static Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else {
            // width and height are equal for all assets since they are ovals.
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
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
        mapboxMap.removeOnMapLongClickListener(this::addSymbol);
        symbolManager.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
    //endregion
}
