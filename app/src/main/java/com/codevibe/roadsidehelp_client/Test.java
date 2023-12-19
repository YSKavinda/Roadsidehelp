package com.codevibe.roadsidehelp_client;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;

public class Test extends AppCompatActivity {

    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


    }


//    @Override
//    public void onMapReady(@NonNull GoogleMap googleMap) {
//        map = googleMap;
//
//        LatLng latLng = new LatLng(6.7204531,80.0821483);
//        map.addMarker(new MarkerOptions().position(latLng).title("MyLocation"));
//        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//
//    }
}