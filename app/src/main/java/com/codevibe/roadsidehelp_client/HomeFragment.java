package com.codevibe.roadsidehelp_client;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.codevibe.roadsidehelp_client.fragments.ServiceCenters;
import com.codevibe.roadsidehelp_client.fragments.TowTrucks;
import com.codevibe.roadsidehelp_client.fragments.Vehicles;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 10;

    private GoogleMap map;
    private Location current_location;
    private FusedLocationProviderClient fusedLocationProviderClient;
  
    private static HomeFragment homeFragment;

   private SharedPreferences preferences;

    public HomeFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final String TAG = getActivity().getClass().getName();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        
        preferences = getActivity().getSharedPreferences("ROADSIDE_USER_PREFS", Context.MODE_PRIVATE);
        previewSlider();
        getActivity().findViewById(R.id.buttonGoToServices).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             loadFragment(ServiceCenters.getInstance());
            }
        });
        getActivity().findViewById(R.id.buttongotoVehicles).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(Vehicles.getInstance());
            }
        });
        getActivity().findViewById(R.id.buttongotoTowTrucks).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(TowTrucks.getInstance());
            }
        });

        TextView displayUsername = getActivity().findViewById(R.id.textView2);
        displayUsername.setText(preferences.getString("username",""));
        if (checkPermission()) {
            getLastLocation();

        } else {
            getActivity().requestPermissions(new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, LOCATION_PERMISSION_REQUEST_CODE);
        }

    }

    public static HomeFragment getInstance(){
        if(homeFragment==null){
            homeFragment = new HomeFragment();
        }
        return homeFragment;
    }

    public void previewSlider(){
        ImageSlider imageSlider = getActivity().findViewById(R.id.imageSlider);
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.slide1, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.slide2,ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.slide3,ScaleTypes.FIT));


        imageSlider.setImageList(slideModels);
    }

    public void loadFragment(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container,fragment);
        fragmentTransaction.commit();
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

    private void getLastLocation() {
        if (checkPermission()) {
            LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY)
                    .setWaitForAccurateLocation(true)
                    .setMinUpdateIntervalMillis(500)
                    .setMaxUpdateDelayMillis(1000).build();
                     fusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    current_location = locationResult.getLastLocation();
                    preferences.getString("lat",String.valueOf(current_location.getLatitude()));
                    preferences.getString("lng",String.valueOf(current_location.getLongitude()));

                }
            }, Looper.getMainLooper());
        }

    }

}