package com.ming.mapboxplugins.localizationplugin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.localization.LocalizationPlugin;
import com.mapbox.mapboxsdk.plugins.localization.MapLocale;
import com.ming.mapboxplugins.AppCons;
import com.ming.mapboxplugins.R;


import java.util.Locale;

/**
 * Use the localization plugin to retrieve the device's language and set all map text labels to that language.
 */
public class LocalizationPluginActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private LocalizationPlugin localizationPlugin;
    private Boolean mapIsLocalized = false;
    private FloatingActionButton fabLocalize;
    private FloatingActionButton fabCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localization_plugin);
        mapView = findViewById(R.id.mapView_localizationplugin);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        fabLocalize = findViewById(R.id.fabLocalize);
        fabCamera = findViewById(R.id.fabCamera);
    }

    @Override
    public void onMapReady(final MapboxMap mapboxMap) {
        mapboxMap.setStyle(AppCons.MAPBOX_DEFAULT_STYLE, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                localizationPlugin = new LocalizationPlugin(mapView, mapboxMap, style);
                localizationPlugin.matchMapLanguageWithDeviceDefault();
                fabLocalize.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mapIsLocalized) {
                            localizationPlugin.setMapLanguage(new MapLocale(MapLocale.FRENCH));
                            mapIsLocalized = false;
                        } else {
                            localizationPlugin.matchMapLanguageWithDeviceDefault();
                            mapIsLocalized = true;
                        }
                    }
                });

                fabCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Locale locale = getNextMapLocale();
                        localizationPlugin.setMapLanguage(locale);
                        localizationPlugin.setCameraToLocaleCountry(locale, 25);
                    }
                });
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_languages, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.english:
                localizationPlugin.setMapLanguage(MapLocale.ENGLISH);
                return true;
            case R.id.spanish:
                localizationPlugin.setMapLanguage(MapLocale.SPANISH);
                return true;
            case R.id.french:
                localizationPlugin.setMapLanguage(MapLocale.FRENCH);
                return true;
            case R.id.german:
                localizationPlugin.setMapLanguage(MapLocale.GERMAN);
                return true;
            case R.id.russian:
                localizationPlugin.setMapLanguage(MapLocale.RUSSIAN);
                return true;
            case R.id.chinese:
                localizationPlugin.setMapLanguage(MapLocale.CHINESE);
                return true;
            case R.id.simplified_chinese:
                localizationPlugin.setMapLanguage(MapLocale.SIMPLIFIED_CHINESE);
                return true;
            case R.id.portuguese:
                localizationPlugin.setMapLanguage(MapLocale.PORTUGUESE);
                return true;
            case R.id.arabic: {
                localizationPlugin.setMapLanguage(MapLocale.ARABIC);
                return true;
            }
            case R.id.japanese: {
                localizationPlugin.setMapLanguage(MapLocale.JAPANESE);
                return true;
            }
            case R.id.korean: {
                localizationPlugin.setMapLanguage(MapLocale.KOREAN);
                return true;
            }
            case R.id.local: {
                localizationPlugin.setMapLanguage(MapLocale.LOCAL_NAME);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Add the mapView lifecycle to the activity's lifecycle methods
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

    private MapLocale[] LOCALES = new MapLocale[]{MapLocale.CANADA, MapLocale.GERMANY, MapLocale.CHINA, MapLocale.US, MapLocale.CANADA_FRENCH, MapLocale.JAPAN, MapLocale.KOREA, MapLocale.FRANCE, MapLocale.SPAIN};

    private int index = 0;

    Locale getNextMapLocale() {
        index++;
        if (index == LOCALES.length) {
            index = 0;
        }
        return new Locale(LOCALES[index].getMapLanguage());
    }
}