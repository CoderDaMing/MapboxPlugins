package com.ming.mapboxplugins.snapshotter;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.snapshotter.MapSnapshot;
import com.mapbox.mapboxsdk.snapshotter.MapSnapshotter;
import com.ming.mapboxplugins.AppCons;
import com.ming.mapboxplugins.MainActivity;
import com.ming.mapboxplugins.R;

import static android.app.PendingIntent.getActivity;

/**
 * 适用于Android的Mapbox Maps SDK的快照功能会生成要在Android项目中使用的静态地图图像。拍摄任何Mapbox地图的快照并将图像添加到：
 * <p>
 * 您应用中的另一个屏幕
 * 主屏幕
 * 主屏幕小部件
 * 通知
 * 一个列表/RecyclerView
 * 无论在哪里都Bitmap可以放置
 * 无需显示Mapbox映射即可使用快照功能。MapSnapshotter可以在应用程序的任何位置调用。
 * <p>
 * 除非您已经缓存了地图图块，否则设备将需要Internet连接来下载渲染地图所需的样式和图块，从而下载快照。
 * <p>
 * 快照生成可以在设备的后台线程上进行，不会影响用户体验。
 */
public class SnapshotNotificationActivity extends AppCompatActivity implements OnMapReadyCallback,
        MapboxMap.OnMapClickListener {
    private static final String TAG = "SnapshotNotification";
    private MapView mapView;
    private MapSnapshotter mapSnapshotter;
    private MapboxMap mapboxMap;
    private NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snapshot_notification);
        mapView = findViewById(R.id.mapView_snapshotter);
        mapView.onCreate(savedInstanceState);

        // Set a callback for when MapboxMap is ready to be used
        mapView.getMapAsync(this);
    }

    /**
     * Creates bitmap from given parameters, and creates a notification with that bitmap
     *
     * @param latLngBounds of map
     * @param height       of map
     * @param width        of map
     */
    private void startSnapShot(LatLngBounds latLngBounds, int height, int width) {
        if (mapSnapshotter == null) {
            // Initialize snapshotter with map dimensions and given bounds
            MapSnapshotter.Options options =
                    new MapSnapshotter.Options(width, height).withStyle(mapboxMap.getStyle().getUrl()).withRegion(latLngBounds);

            mapSnapshotter = new MapSnapshotter(SnapshotNotificationActivity.this, options);
        } else {
            // Reuse pre-existing MapSnapshotter instance
            mapSnapshotter.setSize(width, height);
            mapSnapshotter.setRegion(latLngBounds);
        }

        mapSnapshotter.start(new MapSnapshotter.SnapshotReadyCallback() {
            @Override
            public void onSnapshotReady(MapSnapshot snapshot) {
                createNotification(snapshot.getBitmap());
            }
        });
    }

    /**
     * Creates a notification with given bitmap as a large icon
     *
     * @param bitmap to set as large icon
     */
    private void createNotification(Bitmap bitmap) {
        final int notifyId = 1002;
        String id = "channel_id";
        if (notificationManager == null) {
            notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(id);
            if (notificationChannel == null) {
                notificationChannel = new NotificationChannel(id, "channel_name", NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.setDescription("channel_description");
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
        Intent intent = new Intent(this, MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, id)
                .setContentTitle("content")
                .setSmallIcon(R.drawable.ic_circle)
                .setContentTitle("image_generator_snapshot_notification_title")
                .setContentText("image_generator_snapshot_notification_description")
                .setContentIntent(getActivity(this, 0, intent, 0))
                .setLargeIcon(bitmap);
        Notification notification = builder.build();
        notificationManager.notify(notifyId, notification);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();

        // Make sure to stop the snapshotter on pause if it exists
        if (mapSnapshotter != null) {
            mapSnapshotter.cancel();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mapboxMap != null) {
            mapboxMap.removeOnMapClickListener(this);
        }
        mapView.onDestroy();
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        SnapshotNotificationActivity.this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(AppCons.MAPBOX_DEFAULT_STYLE, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                mapboxMap.addOnMapClickListener(SnapshotNotificationActivity.this);
            }
        });
    }

    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        startSnapShot(
                mapboxMap.getProjection().getVisibleRegion().latLngBounds,
                mapView.getMeasuredHeight(),
                mapView.getMeasuredWidth());
        return false;
    }
}
