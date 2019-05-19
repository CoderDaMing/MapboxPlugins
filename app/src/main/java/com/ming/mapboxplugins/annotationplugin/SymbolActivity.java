package com.ming.mapboxplugins.annotationplugin;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.OnSymbolDragListener;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.ming.mapboxplugins.AppCons;
import com.ming.mapboxplugins.R;


import java.util.ArrayList;
import java.util.List;

//地图注解物
public class SymbolActivity extends AppCompatActivity {
    private static final String TAG = "Ming SymbolActivity==>";
    private MapView mapView;
    private MapboxMap mapboxMap;
    private Style style;
    private Symbol symbol;

    private SymbolManager symbolManager;

    private static final String MAKI_ICON_AIRPORT = "airport-15";
    private static final String MAKI_ICON_CAR = "car-15";
    private static final String MAKI_ICON_CAFE = "cafe-15";
    private static final String MAKI_ICON_CIRCLE = "fire-station-15";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_symbol);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull final MapboxMap mapboxMap) {
                Log.d(TAG, "onMapReady");
                SymbolActivity.this.mapboxMap = mapboxMap;
                mapboxMap.setStyle(AppCons.MAPBOX_DEFAULT_STYLE, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        SymbolActivity.this.style = style;
                        // Map is set up and the style has loaded. Now you can add data or make other map adjustments
                        Log.d(TAG, "onStyleLoaded");

//                        for (Layer singleLayer : mapboxMap.getStyle().getLayers()) {
//                            Log.d(TAG, "onStyleLoaded: layer id = " + singleLayer.getId());
//                        }

                        onMapLoad();
                    }
                });
            }
        });

        findViewById(R.id.update_symbol).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSymbol(v);
            }
        });

        findViewById(R.id.reset_symbol).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetSymbol(v);
            }
        });
    }

    private void onMapLoad() {
        initSymbolManager();

        // create a symbol
        SymbolOptions symbolOptions = new SymbolOptions()
                .withLatLng(new LatLng(6.687337, 0.381457))
                .withIconImage(MAKI_ICON_CAR)
                .withIconSize(1.3f)
                .withTextAnchor("MAKI_ICON_AIRPORT")
                .withZIndex(10)
                .setDraggable(true);
        symbol = symbolManager.create(symbolOptions);

        // create nearby symbols
//        SymbolOptions nearbyOptions = new SymbolOptions()
//                .withLatLng(new LatLng(6.626384, 0.367099))
//                .withIconImage(MAKI_ICON_CIRCLE)
//                .withIconColor(ColorUtils.colorToRgbaString(Color.YELLOW))
//                .withIconSize(2.5f)
//                .withZIndex(5)
//                .setDraggable(true);
//        symbolManager.create(nearbyOptions);


        // random add symbols across the globe
//        List<SymbolOptions> symbolOptionsList = new ArrayList<>();
//        SymbolOptions sO = new SymbolOptions()
//                .withLatLng(new LatLng(6.626384, 0.367099))
//                .withIconImage(MAKI_ICON_AIRPORT)
//                .withIconColor(ColorUtils.colorToRgbaString(Color.YELLOW))
//                .withIconSize(2.5f)
//                .withZIndex(5)
//                .setDraggable(true);
//        symbolOptionsList.add(sO);
//        symbolManager.create(symbolOptionsList);


        //相机移动到地图
        mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(6.626384, 0.367099), 1));
    }

    private void initSymbolManager() {
        // create symbol manager
        symbolManager = new SymbolManager(mapView, mapboxMap, style);
        // set non data driven properties
        symbolManager.setIconAllowOverlap(true);
        symbolManager.setTextAllowOverlap(true);
        //点击监听
        symbolManager.addClickListener(symbol -> Toast.makeText(SymbolActivity.this,
                String.format("Symbol clicked %s", symbol.getId()),
                Toast.LENGTH_SHORT
        ).show());
        //长按监听
        symbolManager.addLongClickListener(symbol ->
                Toast.makeText(SymbolActivity.this,
                        String.format("Symbol long clicked %s", symbol.getId()),
                        Toast.LENGTH_SHORT
                ).show());
        //拖拽监听
        symbolManager.addDragListener(new OnSymbolDragListener() {
            @Override
            public void onAnnotationDragStarted(Symbol annotation) {
                Log.d(TAG, "onAnnotationDragStarted ID:" + annotation.getId()
                        + "\nLatLng:" + annotation.getLatLng().toString());
            }

            @Override
            public void onAnnotationDrag(Symbol annotation) {
                Log.d(TAG, "onAnnotationDrag ID:" + annotation.getId()
                        + "\nLatLng:" + annotation.getLatLng().toString());
            }

            @Override
            public void onAnnotationDragFinished(Symbol annotation) {
                Log.d(TAG, "onAnnotationDragFinished ID:" + annotation.getId()
                        + "\nLatLng:" + annotation.getLatLng().toString());
            }

        });
    }

    public void updateSymbol(View view) {
        if (symbol != null){
            symbol.setDraggable(false);//是否可以拖拽
            symbol.setIconRotate(45.0f);//旋转角度
            symbol.setTextField("要翻车了!");//文字内容
            symbol.setTextAnchor(Property.TEXT_ANCHOR_TOP);//文字在图片的位置
            symbol.setIconOpacity(0.5f);//透明度
            symbol.setIconOffset(new PointF(10.0f, 20.0f));//图片偏移
            symbol.setTextColor(Color.WHITE);//文字内容颜色
            symbol.setTextSize(22f);//文字内容大小
            symbol.setZIndex(0);//如果符号z-index高于另一个符号，它将在其上方呈现。

            symbolManager.update(symbol);
        }
    }

    private void resetSymbol(View v) {
        symbol.setIconRotate(0.0f);
        symbol.setGeometry(Point.fromLngLat(6.687337, 0.381457));
        symbolManager.update(symbol);

        easeSymbol(symbol, new LatLng(6.687337, 0.381457), 180);
    }

    private final List<ValueAnimator> animators = new ArrayList<>();
    private void easeSymbol(Symbol symbol, final LatLng location, final float rotation) {
        final LatLng originalPosition = symbol.getLatLng();
        final float originalRotation = symbol.getIconRotate();
        final boolean changeLocation = originalPosition.distanceTo(location) > 0;
        final boolean changeRotation = originalRotation != rotation;
        if (!changeLocation && !changeRotation) {
            return;
        }

        ValueAnimator moveSymbol = ValueAnimator.ofFloat(0, 1).setDuration(5000);
        moveSymbol.setInterpolator(new LinearInterpolator());
        moveSymbol.addUpdateListener(animation -> {
            if (symbolManager == null || symbolManager.getAnnotations().indexOfValue(symbol) < 0) {
                return;
            }
            float fraction = (float) animation.getAnimatedValue();

            if (changeLocation) {
                double lat = ((location.getLatitude() - originalPosition.getLatitude()) * fraction) + originalPosition.getLatitude();
                double lng = ((location.getLongitude() - originalPosition.getLongitude()) * fraction) + originalPosition.getLongitude();
                symbol.setGeometry(Point.fromLngLat(lng, lat));
            }

            if (changeRotation) {
                symbol.setIconRotate((rotation - originalRotation) * fraction + originalRotation);
            }

            symbolManager.update(symbol);
        });

        moveSymbol.start();
        animators.add(moveSymbol);
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

        for (ValueAnimator animator : animators) {
            animator.cancel();
        }
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
