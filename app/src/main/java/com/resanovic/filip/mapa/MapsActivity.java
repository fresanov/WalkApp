package com.resanovic.filip.mapa;

import android.content.Intent;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Intent i = getIntent();
        String uuid = i.getStringExtra("uuid");
        LocationsStorage storage = new LocationsStorage(this);
        PolylineOptions options = storage.getSpecificRoute(uuid);
        mMap.addPolyline(options);
        LatLng start = new LatLng(i.getDoubleExtra("lat_start", 0.0), i.getDoubleExtra("lon_start", 0.0));
        LatLng stop = new LatLng(i.getDoubleExtra("lat_stop", 0.0), i.getDoubleExtra("lon_stop", 0.0));
        mMap.addMarker(new MarkerOptions().position(start).title("start"));
        mMap.addMarker(new MarkerOptions().position(stop).title("stop"));
        CameraUpdate zoom=CameraUpdateFactory.newLatLngZoom(start, 15);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(start));
        mMap.animateCamera(zoom);
    }
}
