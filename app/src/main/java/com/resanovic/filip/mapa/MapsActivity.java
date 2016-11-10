package com.resanovic.filip.mapa;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
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
    private Button btnShare;
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;
    private double distance;
    private int steps;
    private int calories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        btnShare = (Button) findViewById(R.id.btnShare);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                            .setContentTitle("WalkApp")
                            .setImageUrl(Uri.parse("http://www.clker.com/cliparts/3/u/P/P/q/W/walking-icon-hi.png"))
                            .setContentDescription("I just walked for " + String.valueOf(distance) + " metres, made " + String.valueOf(steps) +
                                    " steps and burnt " + String.valueOf(calories) + " calories!")
                            .setContentUrl(Uri.parse("http://www.google.com"))
                            .build();
                    shareDialog.show(linkContent);
                }
            }
        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Intent i = getIntent();
        distance = i.getDoubleExtra("distance", 0);
        steps = i.getIntExtra("steps", 0);
        calories = i.getIntExtra("calories", 0);
        String uuid = i.getStringExtra("uuid");
        LocationsStorage storage = new LocationsStorage(this);
        PolylineOptions options = storage.getSpecificRoute(uuid);
        mMap.addPolyline(options);
        LatLng start = new LatLng(i.getDoubleExtra("lat_start", 0.0), i.getDoubleExtra("lon_start", 0.0));
        LatLng stop = new LatLng(i.getDoubleExtra("lat_stop", 0.0), i.getDoubleExtra("lon_stop", 0.0));
        mMap.addMarker(new MarkerOptions().position(start).title("start"));
        mMap.addMarker(new MarkerOptions().position(stop).title("stop"));
        CameraUpdate zoom = CameraUpdateFactory.newLatLngZoom(start, 15);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(start));
        mMap.animateCamera(zoom);
    }
}
