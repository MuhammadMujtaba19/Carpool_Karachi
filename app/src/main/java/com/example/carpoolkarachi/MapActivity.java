package com.example.carpoolkarachi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carpoolkarachi.directionhelpers.FetchURL;
import com.example.carpoolkarachi.directionhelpers.TaskLoadedCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import java.util.Arrays;
import java.util.List;
import java.util.PrimitiveIterator;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback , TaskLoadedCallback {

    private Button btnConfirm;
    private GoogleMap  map;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlacesClient placesClient;

    private Location mLastKnownLocation;
    private LocationCallback locationCallback;
    private AutocompleteSupportFragment sourceFragment,destinationFragment;
    private TextView sourceTextView,destTextView;
    private View mapView;
    private MarkerOptions sourceMarker, destinationMarker;
    private final float DEAFUALT_ZOOM = 18;
    private Polyline currentpolyline;
    List<Place.Field> placesFields = Arrays.asList(Place.Field.ID,Place.Field.NAME,Place.Field.ADDRESS);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        initUI();
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapView = mapFragment.getView();

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        Places.initialize(this,"AIzaSyDZgQfvsvXJb_LXCqWXtHKJD9qYrOE09qA");
        placesClient = Places.createClient(this);

        sourceFragment = (AutocompleteSupportFragment)getSupportFragmentManager().findFragmentById(R.id.place_autocomplete1);
        destinationFragment = (AutocompleteSupportFragment)getSupportFragmentManager().findFragmentById(R.id.place_autocomplete2);

        sourceFragment.setPlaceFields(placesFields);
        sourceFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
        @Override
        public void onPlaceSelected(@NonNull Place place) {
            sourceTextView.setText(place.getName());
//                dest.setVisibility(View.VISIBLE);
//
                String placeID = place.getId();

            List<Place.Field>  placeFields = Arrays.asList(Place.Field.LAT_LNG);
                FetchPlaceRequest fetchPlaceRequest = FetchPlaceRequest.builder(placeID,placeFields).build();
                placesClient.fetchPlace(fetchPlaceRequest).addOnSuccessListener( fetchPlaceResponse -> {
                    Place place1 = fetchPlaceResponse.getPlace();
                    Log.i("mytag", "Place found: " + place1.getName());
                    LatLng latLngOfPlace = place1.getLatLng();
                    if (latLngOfPlace != null) {
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngOfPlace, DEAFUALT_ZOOM));
                        sourceMarker = new MarkerOptions()
                                .position(latLngOfPlace)
                                .title("Source");
                        map.addMarker(sourceMarker);
                    }
                });
        }

        @Override
        public void onError(@NonNull Status status) {

        }
    });

        destinationFragment.setPlaceFields(placesFields);
        destinationFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                destTextView.setText(place.getName());
//                dest.setVisibility(View.VISIBLE);
//
                String placeID = place.getId();

                List<Place.Field>  placeFields = Arrays.asList(Place.Field.LAT_LNG);
                FetchPlaceRequest fetchPlaceRequest = FetchPlaceRequest.builder(placeID,placeFields).build();
                placesClient.fetchPlace(fetchPlaceRequest).addOnSuccessListener( fetchPlaceResponse -> {
                    Place place1 = fetchPlaceResponse.getPlace();
                    Log.i("mytag", "Place found: " + place1.getName());
                    LatLng latLngOfPlace = place1.getLatLng();
                    if (latLngOfPlace != null) {
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngOfPlace, DEAFUALT_ZOOM));
                        destinationMarker = new MarkerOptions()
                                .position(latLngOfPlace)
                                .title("Source");
                        map.addMarker(destinationMarker);
                        String Url = getUrl(sourceMarker.getPosition(),destinationMarker.getPosition(),"driving");
                        new FetchURL(MapActivity.this).execute(Url,"driving");
                    }
                });
            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });



        btnConfirm.setOnClickListener(v -> {
            Intent i = new  Intent(this,addRide.class);
            i.putExtra("SourceLocation",sourceTextView.getText().toString());
            i.putExtra("DestinationLocation",destTextView.getText().toString());
            i.putExtra("SourcePosition",sourceMarker.getPosition());
            i.putExtra("DestPosition",destinationMarker.getPosition());

            startActivity(i);

        });
    }
    public void initUI(){
        btnConfirm = findViewById(R.id.btnConfirm);
        sourceTextView  = findViewById(R.id.sourceTextView);
        destTextView  = findViewById(R.id.destTextView);
}
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 40, 180);
        }

        //check if gps is enabled or not and then request user to enable it
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        SettingsClient settingsClient = LocationServices.getSettingsClient(MapActivity.this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());
        task.addOnSuccessListener(MapActivity.this, new OnSuccessListener<LocationSettingsResponse>() {

            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getDeviceLocation();
            }
        });
        task.addOnFailureListener(MapActivity.this, e -> {

        });
    }   @SuppressLint("MissingPermission")
    public void getDeviceLocation(){
        mFusedLocationProviderClient.getLastLocation().
                addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if(task.isSuccessful()) {
                            mLastKnownLocation = task.getResult();
                            if(mLastKnownLocation !=null){
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude()),DEAFUALT_ZOOM));
                            }else {
                                LocationRequest locationRequest = LocationRequest.create();
                                locationRequest.setInterval(10000);
                                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                                locationCallback = new LocationCallback(){
                                    @Override
                                    public void onLocationResult(LocationResult locationResult) {
                                        super.onLocationResult(locationResult);
                                        if ( locationRequest ==null)return;
                                        mLastKnownLocation = locationResult.getLastLocation();
                                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude()),DEAFUALT_ZOOM));
                                        mFusedLocationProviderClient.removeLocationUpdates(locationCallback);
                                    }
                                };
                                mFusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,null);
                            }

                        }else{
                            Toast.makeText(MapActivity.this, "Unable to get Last Location", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + "AIzaSyDZgQfvsvXJb_LXCqWXtHKJD9qYrOE09qA";
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if(currentpolyline!=null)currentpolyline.remove();
        currentpolyline = map.addPolyline((PolylineOptions)values[0]);
    }
}