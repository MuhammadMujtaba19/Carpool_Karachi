package com.example.carpoolkarachi;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.internal.NavigationMenu;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.List;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

public class MainActivity extends AppCompatActivity {
    private MapView mapView;
    private MapboxMap map;
    private PermissionsManager permissionsManager;
    private LocationManager locationManager;
    private LocationEngine locationEngine;
    private LocationLayerPlugin locationLayerPlugin;
    private Location originLocation;

    RecyclerView topAdapter;
    homeAdapter adapter;
    FabSpeedDial fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_main);
        fab = findViewById(R.id.floatingActionButton);
        topAdapter=(RecyclerView)findViewById(R.id.myRecycler);
        topAdapter.setHasFixedSize(true);
        topAdapter.setLayoutManager(new LinearLayoutManager(this));
        topAdapter.setNestedScrollingEnabled(false);
        adapter = new homeAdapter(this);
        topAdapter.setAdapter(adapter);
        fab.setMenuListener(new FabSpeedDial.MenuListener() {
            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                return true;
            }

            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                Intent i = new Intent(MainActivity.this,MapActivity.class);

                if( menuItem.getItemId()==R.id.addRideItem){
                    i.putExtra("isRide",true);
                }else if (menuItem.getItemId()==R.id.addRequestItem){
                    i.putExtra("isRide",false);
                }
                startActivity(i);
                return true;
            }

            @Override
            public void onMenuClosed() {

            }


    });

}
}