package com.codevibe.roadsidehelp_client.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.codevibe.roadsidehelp_client.model.Driver;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserService{

    enum FireBaseEntities{
        drivers,category,emergencyServices,serviceCenters,towTrucks,vehicles
    }

    FirebaseFirestore db;
    public UserService(){
        super();
        db = FirebaseFirestore.getInstance();
    }

    public static final String TAG = UserService.class.getName();
    public boolean registerUsers(Driver driver){



        db.collection(FireBaseEntities.drivers.toString()).add(driver).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.i(TAG,"Driver Document Added with ID : " + documentReference.getId());
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG,"Error",e);
            }
        });
        return true;
    }

}
