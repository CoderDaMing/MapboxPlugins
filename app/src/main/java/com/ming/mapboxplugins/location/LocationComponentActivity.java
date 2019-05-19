package com.ming.mapboxplugins.location;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.OnCameraTrackingChangedListener;
import com.mapbox.mapboxsdk.location.OnLocationClickListener;
import com.mapbox.mapboxsdk.location.OnLocationLongClickListener;
import com.mapbox.mapboxsdk.location.OnLocationStaleListener;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.ming.mapboxplugins.AppCons;
import com.ming.mapboxplugins.R;

import java.util.List;

public class LocationComponentActivity extends AppCompatActivity implements OnMapReadyCallback, PermissionsListener {

    private static final String TAG = "LocationComponent";
    private PermissionsManager permissionsManager;
    private MapboxMap mapboxMap;
    private MapView mapView;
    private Style style;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_component);

        mapView = findViewById(R.id.mapView_location);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        LocationComponentActivity.this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(AppCons.MAPBOX_DEFAULT_STYLE, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                LocationComponentActivity.this.style = style;
                enableLocationComponent();
            }
        });

    }

    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent() {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            // Get an instance of the component
            LocationComponent locationComponent = mapboxMap.getLocationComponent();

            /*定制
            在LocationComponent允许一些自定义。您可以设置drawable，不透明度，颜色等。创建一个LocationComponentOptions对象，然后使用您想要的任何方法。然后在激活组件时使用该对象，或者LocationComponent#applyStyle()稍后将其作为方法的参数传递。

            查看可以通过位于公共API一部分的属性和样式的完整列表中的LocationComponent方法自定义的所有属性。*/


//            LocationComponentOptions options = LocationComponentOptions.builder(this)
//                    .layerBelow(layerId)
//                    .foregroundDrawable(R.drawable.drawable_name)
//                    .bearingTintColor(int color)
//                    .accuracyAlpha(float)
//                    .build();
//            locationComponent.activateLocationComponent(this, mapStyle, options);


            // Activate
            locationComponent.activateLocationComponent(this,style);

            // Enable to make component visible
            // 有一种方法可以LocationComponent在激活后启用或禁用可见性。该setLocationComponentEnabled()方法需要true/ falseboolean参数。设置为时false，此方法将隐藏设备位置图标并停止发生地图相机动画。
            locationComponent.setLocationComponentEnabled(true);

            // Set the component's camera mode
            //该方法LocationComponent#setCameraMode(@CameraMode.Mode int cameraMode)允许开发人员在设备位置改变时设置特定的摄像机跟踪指令。
            /**
             * 目前有7种CameraMode选择：
             *
             * CameraMode	描述
             * NONE	没有摄像头跟踪。
             * NONE_COMPASS	相机不跟踪位置，但会跟踪罗盘方位。
             * NONE_GPS	相机不会跟踪位置，但会跟踪GPS Location方位。
             * TRACKING	摄像机跟踪设备位置，不考虑轴承。
             * TRACKING_COMPASS	摄像机跟踪设备位置，跟踪设备指南针提供的轴承。
             * TRACKING_GPS	摄像机跟踪设备位置，轴承由标准化提供Location#getBearing()。
             * TRACKING_GPS_NORTH	摄像机跟踪设备位置，轴承始终设置为北（0）。
             */
            locationComponent.setCameraMode(CameraMode.TRACKING);

            // Set the component's render mode
            //RenderMode类包含所述装置位置图像预设选项
            // NORMAL	此模式显示设备位置，忽略指南针和GPS方位（无箭头渲染）。
            //COMPASS	此模式显示设备位置，以及考虑设备指南针的箭头。
//            GPS	此模式显示设备位置，图标轴承从Location提供的更新更新LocationComponent。
            locationComponent.setRenderMode(RenderMode.COMPASS);


            locationComponent.addOnCameraTrackingChangedListener(new OnCameraTrackingChangedListener() {
                @Override
                public void onCameraTrackingDismissed() {
                    Log.d(TAG,"onCameraTrackingDismissed");
                }

                @Override
                public void onCameraTrackingChanged(int currentMode) {
                    Log.d(TAG,"onCameraTrackingChanged :" + currentMode);
                }
            });

            locationComponent.addOnLocationClickListener(new OnLocationClickListener() {
                @Override
                public void onLocationComponentClick() {
                    Log.d(TAG,"onLocationComponentClick");
                }
            });

            locationComponent.addOnLocationLongClickListener(new OnLocationLongClickListener() {
                @Override
                public void onLocationComponentLongClick() {
                    Log.d(TAG,"onLocationComponentLongClick");
                }
            });
            
            locationComponent.addOnLocationStaleListener(new OnLocationStaleListener() {
                @Override
                public void onStaleStateChange(boolean isStale) {
                    Log.d(TAG,"onStaleStateChange :" + isStale);
                }
            });
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    //region 权限
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, "user_location_permission_explanation", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocationComponent();
        } else {
            Toast.makeText(this,"user_location_permission_not_granted", Toast.LENGTH_LONG).show();
            finish();
        }
    }
    //endregion


    //region 生命周期
    @Override
    @SuppressWarnings( {"MissingPermission"})
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    //endregion
}
