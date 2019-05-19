package com.ming.mapboxplugins.annotationplugin;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.ming.mapboxplugins.R;


/**
 * Test activity showcasing updating a Marker position, title, icon and snippet.
 */
public class DynamicSymbolChangeActivity extends AppCompatActivity {
    private static final LatLng LAT_LNG_CHELSEA = new LatLng(51.481670, -0.190849);
    private static final LatLng LAT_LNG_ARSENAL = new LatLng(51.555062, -0.108417);

    private static final String ID_ICON_1 = "com.mapbox.annotationplugin.icon.1";
    private static final String ID_ICON_2 = "com.mapbox.annotationplugin.icon.2";

    private SymbolManager symbolManager;
    private MapView mapView;
    private MapboxMap mapboxMap;
    private Symbol symbol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_symbol_change);
        mapView = findViewById(R.id.mapview_dynamic_symbol_change);
        mapView.setTag(false);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(mapboxMap -> {
            DynamicSymbolChangeActivity.this.mapboxMap = mapboxMap;

            LatLng target = new LatLng(51.506675, -0.128699);

            mapboxMap.moveCamera(CameraUpdateFactory.newCameraPosition(
                    new CameraPosition.Builder()
                            .bearing(90)
                            .tilt(40)
                            .zoom(10)
                            .target(target)
                            .build()
            ));

            mapboxMap.setStyle(new Style.Builder()
                            .fromUrl(Style.MAPBOX_STREETS)
                            .withImage(ID_ICON_1, generateBitmap(R.drawable.blue_marker), true)
                            .withImage(ID_ICON_2, generateBitmap(R.drawable.yellow_marker), true)
                    , style -> {
                        symbolManager = new SymbolManager(mapView, mapboxMap, style);
                        symbolManager.setIconAllowOverlap(true);
                        symbolManager.setTextAllowOverlap(true);

                        // Create Symbol
                        SymbolOptions SymbolOptions = new SymbolOptions()
                                .withLatLng(LAT_LNG_CHELSEA)
                                .withIconImage(ID_ICON_1);

                        symbol = symbolManager.create(SymbolOptions);
                    });


        });

        findViewById(R.id.btn_update_change).setOnClickListener(view -> {
            if (mapboxMap != null) {
                updateSymbol();
            }
        });
    }

    private void updateSymbol() {
        // update model
        boolean first = (boolean) mapView.getTag();
        mapView.setTag(!first);

        // update symbol
        symbol.setLatLng(first ? LAT_LNG_CHELSEA : LAT_LNG_ARSENAL);
        symbol.setIconImage(first ? ID_ICON_1 : ID_ICON_2);
        symbolManager.update(symbol);
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
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
    //endregion
}
