package com.ming.mapboxplugins;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

import com.ming.mapboxplugins.abase.ItemBean;
import com.ming.mapboxplugins.abase.RvItemListAdapter;
import com.ming.mapboxplugins.annotationplugin.BulkSymbolActivity;
import com.ming.mapboxplugins.annotationplugin.CircleActivity;
import com.ming.mapboxplugins.annotationplugin.DynamicSymbolChangeActivity;
import com.ming.mapboxplugins.annotationplugin.FillActivity;
import com.ming.mapboxplugins.annotationplugin.FillChangeActivity;
import com.ming.mapboxplugins.annotationplugin.LineActivity;
import com.ming.mapboxplugins.annotationplugin.LineChangeActivity;
import com.ming.mapboxplugins.annotationplugin.PressForSymbolActivity;
import com.ming.mapboxplugins.annotationplugin.SymbolActivity;
import com.ming.mapboxplugins.buildingplugin.BuildingPluginActivity;
import com.ming.mapboxplugins.dataclustering.CircleLayerClusteringActivity;
import com.ming.mapboxplugins.dataclustering.ImageClusteringActivity;
import com.ming.mapboxplugins.localizationplugin.LocalizationPluginActivity;
import com.ming.mapboxplugins.location.LocationActivity;
import com.ming.mapboxplugins.location.LocationComponentActivity;
import com.ming.mapboxplugins.markerview.MarkerViewActivity;
import com.ming.mapboxplugins.offlineplugin.OfflinePluginActivity;
import com.ming.mapboxplugins.snapshotter.SnapshotNotificationActivity;
import com.ming.mapboxplugins.snapshotter.SnapshotShareActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private RecyclerView rv_list;
    private RvItemListAdapter rvListAdapter;
    private List<ItemBean> itemBeans = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rv_list = findViewById(R.id.rv_list);
        rv_list.setLayoutManager(new LinearLayoutManager(this));
        rvListAdapter = new RvItemListAdapter(this, itemBeans);
        rvListAdapter.setOnItemClickListener(this);
        rv_list.setAdapter(rvListAdapter);

        initCls();
    }

    private void initCls() {
        itemBeans.add(new ItemBean("Ano-BulkSymbolActivity", BulkSymbolActivity.class));
        itemBeans.add(new ItemBean("Ano-CircleActivity", CircleActivity.class));
        itemBeans.add(new ItemBean("Ano-DynamicSymbolChangeActivity", DynamicSymbolChangeActivity.class));
        itemBeans.add(new ItemBean("Ano-FillActivity", FillActivity.class));
        itemBeans.add(new ItemBean("Ano-FillChangeActivity", FillChangeActivity.class));
        itemBeans.add(new ItemBean("Ano-LineActivity", LineActivity.class));
        itemBeans.add(new ItemBean("Ano-LineChangeActivity", LineChangeActivity.class));
        itemBeans.add(new ItemBean("Ano-PressForSymbolActivity", PressForSymbolActivity.class));
        itemBeans.add(new ItemBean("Ano-SymbolActivity", SymbolActivity.class));

        itemBeans.add(new ItemBean("BuildingPluginActivity", BuildingPluginActivity.class));

        itemBeans.add(new ItemBean("DataCluster-CircleLayerClusteringActivity", CircleLayerClusteringActivity.class));
        itemBeans.add(new ItemBean("DataCluster-ImageClusteringActivity", ImageClusteringActivity.class));

        itemBeans.add(new ItemBean("LocalizationPluginActivity", LocalizationPluginActivity.class));

        itemBeans.add(new ItemBean("location-LocationActivity", LocationActivity.class));
        itemBeans.add(new ItemBean("location-LocationComponentActivity", LocationComponentActivity.class));

        itemBeans.add(new ItemBean("MarkerViewActivity", MarkerViewActivity.class));

        itemBeans.add(new ItemBean("OfflinePluginActivity", OfflinePluginActivity.class));

        itemBeans.add(new ItemBean("Snapshot-SnapshotNotificationActivity", SnapshotNotificationActivity.class));
        itemBeans.add(new ItemBean("Snapshot-SnapshotShareActivity", SnapshotShareActivity.class));
        rvListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (itemBeans != null && itemBeans.size() > 0) {
            ItemBean itemBean = itemBeans.get(position);
            startActivity(new Intent(this, itemBean.getCls()));
        }
    }
}
