package com.codevibe.roadsidehelp_client;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.codevibe.roadsidehelp_client.mapplaces.DataParser;
import com.codevibe.roadsidehelp_client.mapplaces.DownloadUrl;
import com.codevibe.roadsidehelp_client.mapplaces.GetNearbyPlaces;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;


public class GMap extends Fragment implements OnMapReadyCallback {

    private static final String TAG = GMap.class.getName();
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 10;

    private Location current_location;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Marker marker_current;
    private static GMap mapscreen;


    public GMap() {
        // Required empty public constructor
    }

    private GoogleMap map;

    private boolean cameraMoved = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_g_map, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, mapFragment)
                .commit();
        mapFragment.getMapAsync(this::onMapReady);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.getUiSettings().setZoomControlsEnabled(true);
        if (checkPermission()) {
            getLastLocation();
            loadNearByLocations(6.7201,80.0508);
//            map.setMyLocationEnabled(true);
        } else {
            getActivity().requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, LOCATION_PERMISSION_REQUEST_CODE);
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//               getLastLocation();
            } else {
                Snackbar.make(getActivity().findViewById(R.id.gMapcontainer), "Location Permission denied", Snackbar.LENGTH_INDEFINITE).setAction("Settings", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }).show();
            }
        }

    }


    private boolean checkPermission() {
        boolean permission = false;
        if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            permission = true;
        }

        return permission;
    }

    @SuppressWarnings("MissingPermissions")
    private void getLastLocation() {
        if (checkPermission()) {
            LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,5000)
                    .setWaitForAccurateLocation(true)
                    .setMinUpdateIntervalMillis(500)
                    .setMaxUpdateDelayMillis(1000).build();
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    current_location = locationResult.getLastLocation();
                    LatLng latlng = new LatLng(current_location.getLatitude(), current_location.getLongitude());

                    if (marker_current == null) {
                        MarkerOptions markerOptions = new MarkerOptions()
                                .title("Me")
                                .position(latlng);
                        marker_current = map.addMarker(markerOptions);

                    } else {
                        marker_current.setPosition(latlng);
                    }
                    if (cameraMoved) {
                        moveCamera(latlng);
                        cameraMoved = false;
                    }

//
//                        map.addMarker(new MarkerOptions().position(latlng).title("Me"));
//                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,10));
                }
            }, Looper.getMainLooper());
        }

    }



    private void loadNearByLocations(Double lat, Double lon) {

        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("local.properties"));
            String apiKey = properties.getProperty("MAPS_API_KEY");
            String url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=bank&location=" + lat + "," + lon + "&radius=10000000&key=AIzaSyDFMho1DhWJ3f1s1IpIN-UkRrvP1aOi1LQ";
            Object transferData[] = new Object[3];
            transferData[0]=map;
            transferData[1]=lat;
            transferData[2]=lon;
            GetNearbyPlaces getNearbyPlaces = new GetNearbyPlaces();
            getNearbyPlaces.execute(transferData);
            Toast.makeText(getActivity(),"Searching for Nearby Tow Trucks",Toast.LENGTH_SHORT).show();


        } catch (Exception e) {

        }

    }


    public void moveCamera(LatLng latLng) {

        CameraPosition cameraPosition = CameraPosition.builder()
                .target(latLng)
                .zoom(10f)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        map.animateCamera(cameraUpdate);
    }


    public static GMap getInstance() {
        if (mapscreen == null) {
            mapscreen = new GMap();
        }
        return mapscreen;
    }


}