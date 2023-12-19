package com.codevibe.roadsidehelp_client;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.codevibe.roadsidehelp_client.fragments.CustomerServices;
import com.codevibe.roadsidehelp_client.fragments.Emergency;
import com.codevibe.roadsidehelp_client.fragments.Vehicles;
import com.codevibe.roadsidehelp_client.model.Driver;
import com.codevibe.roadsidehelp_client.viewmodels.MainViewModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, NavigationBarView.OnItemSelectedListener {

    FirebaseAuth firebaseAuth;

    FirebaseFirestore firestore;
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private MaterialToolbar toolbar;
    private BottomNavigationView bottomNavView;
    private Driver driver;

    private MainViewModel mainViewModel;

    private static int REQUEST_CODE = 10;


    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        firebaseAuth = FirebaseAuth.getInstance();
        SharedPreferences prefs = getSharedPreferences("ROADSIDE_USER_PREFS", MODE_PRIVATE);

        if (firebaseAuth.getCurrentUser() != null) {

            String email = firebaseAuth.getCurrentUser().getEmail().toString();
//            firestore.collection("users").document(firebaseAuth.getCurrentUser().getEmail()).


            drawerLayout = findViewById(R.id.drawerLayout);
            navView = findViewById(R.id.mainNavigation);
            toolbar = findViewById(R.id.toolBar);
            bottomNavView = findViewById(R.id.bottomNavigation);

            //get Driver Data
            driver = new Driver();
            firestore = FirebaseFirestore.getInstance();
            firestore.collection("drivers").whereEqualTo("email", email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    QuerySnapshot result = task.getResult();

                    result.getDocuments().forEach(doc -> {

                        driver.setUsername(doc.get("username").toString());
//                        driver.setActive(Boolean.valueOf(doc.get("status").toString()));
                            driver.setLocation(new LatLng(2.0000,95.5000));
                        driver.setMobile(doc.get("mobile").toString());
                        driver.setEmail(doc.get("email").toString());
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("username", driver.getUsername());
                        editor.putString("email", driver.getEmail());
                        editor.apply(); // or commit();


                    });

                    Log.i(TAG, "No of Docs" + result.getDocuments().size());

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG,"driver Not recieved",e);
                }
            });





            setSupportActionBar(toolbar);

            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    drawerLayout.open();
                    TextView userName = findViewById(R.id.sideNavName);
                    TextView userEmail = findViewById(R.id.sideNavEmailAddress);
                    userEmail.setText(prefs.getString("email",""));
                    userName.setText(prefs.getString("username",""));

                }
            });

            navView.setNavigationItemSelectedListener(this);
            bottomNavView.setOnItemSelectedListener(this);


        } else {

            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);

        }


    }





    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()){
//            case  R.id.sideNavHome
//                    : loadFragment(HomeFragment.getInstance());
//                return true;
//
//            case R.id.sideNavCustomerCare :
//                     loadFragment(new CustomerServices());
//                return true;
//        }


        if (item.getItemId() == R.id.sideNavHome) {
            loadFragment(HomeFragment.getInstance());
        } else if (item.getItemId() == R.id.sideNavCustomerCare) {
            loadFragment(CustomerServices.getInstance());
        } else if (item.getItemId() == R.id.sideNavMap) {
            loadFragment(GMap.getInstance());
        } else if (item.getItemId() == R.id.sidNavemergency) {
            loadFragment(Emergency.getInstance());
        } else if (item.getItemId() == R.id.sideNavLogout) {
            firebaseAuth.signOut();
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
            finish();
        } else if(item.getItemId() == R.id.sideNavVehicles){
            loadFragment(Vehicles.getInstance());
        }else if(item.getItemId() == R.id.sideNavUserProfile) {

            Intent intent = new Intent(MainActivity.this, DriverProfile.class);
            startActivity(intent);

        }else if(item.getItemId() == R.id.bottomMap) {
                loadFragment(GMap.getInstance());
            } else if (item.getItemId() == R.id.bottomHome) {
                loadFragment(HomeFragment.getInstance());
            } else if (item.getItemId() == R.id.bottomCustomerCare) {
                loadFragment(CustomerServices.getInstance());
            } else if (item.getItemId() == R.id.bottomEmergency) {
                loadFragment(Emergency.getInstance());
            }

        return false;
    }




//    public void loadMap(Fragment fragment){
//        FragmentManager fragmentManager = ;
//
//    }
    public void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }

}