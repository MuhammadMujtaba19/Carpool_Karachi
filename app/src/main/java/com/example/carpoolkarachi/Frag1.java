package com.example.carpoolkarachi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.carpoolkarachi.directionhelpers.FetchURL;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class Frag1 extends Fragment implements OnMapReadyCallback {
    private View mapView;
    private GoogleMap  map;
    LatLng source,dest;


    public Frag1(){}

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
             source = getArguments().getParcelable("s1");
             dest= getArguments().getParcelable("d1");

        }
        SupportMapFragment mapFragment = (SupportMapFragment)getFragmentManager().findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);
        mapView = mapFragment.getView();


        return inflater.inflate(R.layout.fragment1, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        String Url = getUrl(sourceMarker.getPosition(),destinationMarker.getPosition(),"driving");
//        new FetchURL(MapActivity.this).execute(Url,"driving");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(source, (float) 14));
        MarkerOptions sourceMarker = new MarkerOptions()
                .position(source)
                .title("Source");
        map.addMarker(sourceMarker);

//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(dest, (float) 14));
//        MarkerOptions destMarker = new MarkerOptions()
//                .position(dest)
//                .title("Dest");
//        map.addMarker(destMarker);
    }
}
