package com.codevibe.roadsidehelp_client;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.codevibe.roadsidehelp_client.model.Driver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class DriverProfile extends AppCompatActivity {
    private static final String TAG = DriverProfile.class.getName();
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;

    EditText eusername,eemail,emobile,estatus;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_profile);


        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        String email = user.getEmail().toString();

        eusername = findViewById(R.id.profileDriverUsername);
        eemail = findViewById(R.id.profileDriverEmail);
        emobile = findViewById(R.id.profileDriverMobile);
        estatus = findViewById(R.id.profileDriverStatus);

        Button backtoHome = findViewById(R.id.backtoHomeButton);
        backtoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DriverProfile.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(intent);
            }
        });

        Button updateButton = findViewById(R.id.updateProfileButton);


        db = FirebaseFirestore.getInstance();
        db.collection("drivers").whereEqualTo("email",email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot query = task.getResult();
                    query.forEach(doc->{
                        if(doc.exists()){
                            eusername.setText(doc.get("username").toString());
                            eemail.setText(doc.get("email").toString());
                            emobile.setText(doc.get("mobile").toString());
                            if( doc.get("active").toString().equals("true")){
                                estatus.setText("Active");
                            }else{
                                estatus.setText("Disabled");
                            }
                            id = doc.getId();

                        }
                    });



                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Failed to get Dat a:", new Throwable());
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile(email);
            }
        });




    }
    public void updateProfile(String email){

        eusername = findViewById(R.id.profileDriverUsername);
        emobile = findViewById(R.id.profileDriverMobile);

        if(eusername.getText().toString().isEmpty()){
            Toast.makeText(DriverProfile.this,"cannot leave username field empty",Toast.LENGTH_LONG);
        }else if(eemail.getText().toString().isEmpty()){
            Toast.makeText(DriverProfile.this,"cannot leave username field empty",Toast.LENGTH_LONG);
        }else if(emobile.getText().toString().isEmpty()){
            Toast.makeText(DriverProfile.this,"cannot leave mobile field empty",Toast.LENGTH_LONG);
        }else{

            Driver driver = new Driver(eusername.getText().toString(),email,emobile.getText().toString());
            HashMap driverDataMap = new HashMap<String,String>();

            driverDataMap.put("username",eusername.getText().toString());
            driverDataMap.put("mobile",emobile.getText().toString());

         DocumentReference reference = db.collection("drivers").document(id);
            reference.update(driverDataMap).addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    Toast.makeText(DriverProfile.this,"Update Successful",Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(DriverProfile.this,"Update Failed",Toast.LENGTH_LONG).show();
                }
            });
        }






    }
}