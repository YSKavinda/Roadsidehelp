package com.codevibe.roadsidehelp_client.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codevibe.roadsidehelp_client.R;
import com.codevibe.roadsidehelp_client.adapters.TowTruckItemAdapter;
import com.codevibe.roadsidehelp_client.model.TowTruck;
import com.codevibe.roadsidehelp_client.model.Vehicle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;


public class TowTrucks extends Fragment {

    private static TowTrucks towTrucks;
    private ArrayList<TowTruck> towTruckItemList;

    private FirebaseFirestore firestore;

    public TowTrucks() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tow_trucks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firestore = FirebaseFirestore.getInstance();
       towTruckItemList = new ArrayList<>();
       RecyclerView itemView = getActivity().findViewById(R.id.towtruckRecycleView);

        TowTruckItemAdapter adapter = new TowTruckItemAdapter(towTruckItemList,getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        itemView.setLayoutManager(linearLayoutManager);
        itemView.setAdapter(adapter);

//        for(int i = 0;i<5;i++){
//            TowTruck towTruck = new TowTruck("ABC","0762282965","no.37","distance","1.50000","2.0000");
//            towTruckItemList.add(towTruck);
//        }

        firestore.collection("towTrucks").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        towTruckItemList.clear();
                        for(QueryDocumentSnapshot snapshot: task.getResult()){
                            TowTruck towTruck = snapshot.toObject(TowTruck.class);
                            towTruckItemList.add(towTruck);
                        }
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


        adapter.notifyDataSetChanged();

    }

    public void getCallforTowTruck(Intent intent){
        startActivity(intent);
    }

    public static TowTrucks getInstance(){
        if(towTrucks==null){
            towTrucks = new TowTrucks();
        }
        return towTrucks;
    }
}