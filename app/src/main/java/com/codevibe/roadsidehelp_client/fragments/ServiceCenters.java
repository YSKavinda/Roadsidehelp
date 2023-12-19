package com.codevibe.roadsidehelp_client.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codevibe.roadsidehelp_client.MainActivity;
import com.codevibe.roadsidehelp_client.R;
import com.codevibe.roadsidehelp_client.adapters.ServiceproviderItemAdapter;
import com.codevibe.roadsidehelp_client.model.ServiceProvider;
import com.codevibe.roadsidehelp_client.services.ServiceProviderService;
import com.codevibe.roadsidehelp_client.uiItems.ServiceProviderItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ServiceCenters extends Fragment {

    public static final String TAG = ServiceCenters.class.getName();

    private static ServiceCenters serviceCenter ;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<ServiceProviderItem> serviceProviderList;
    private RecyclerView serviceProviderRecycleView;
    private ServiceproviderItemAdapter serviceproviderItemAdapter;

    public ServiceCenters() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_service_centers, container,false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View fragment = view;
        serviceProviderRecycleView = fragment.findViewById(R.id.servicesRecyclerView);
        getAllServiceProviders();

    }

    public static ServiceCenters getInstance(){
        if(serviceCenter==null){
            serviceCenter = new ServiceCenters();
        }
        return serviceCenter;
    }

    public void getAllServiceProviders() {

        ArrayList<ServiceProvider> providers = new ArrayList<>();
        db.collection("serviceProviders")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@android.support.annotation.NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            QuerySnapshot result = task.getResult();
                            List<DocumentSnapshot> documents = result.getDocuments();
                            documents.forEach(p -> {

                                ServiceProvider serviceProvider = new ServiceProvider(p.getString("title"),
                                        p.getString("lat"),
                                        p.getString("lng"),
                                        p.getString("status"),
                                        p.getString("contact"));
                                providers.add(serviceProvider);

                                Log.i(TAG,serviceProvider.toString() );

                            });
                           createServiceProviderItems(providers);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());

                        }
                    }
                });


    }

    public void setLoccationToMaps(Intent mapIntent){
        try{
            if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(mapIntent);
            }
        }catch (NullPointerException e){
            Log.e(TAG, "onClick: NullPointerException: Couldn't open map." + e.getMessage() );
            Toast.makeText(getActivity(), "Couldn't open map", Toast.LENGTH_SHORT).show();
        }
    }

    private void createServiceProviderItems(ArrayList<ServiceProvider> providers) {
        List<ServiceProviderItem> serviceProviderItems = new ArrayList<>();


        Log.i(TAG,"providers " + providers);
        providers.forEach(provider->{

            serviceProviderItems.add(new ServiceProviderItem(R.drawable.baseline_car_repair_24_black,provider.getTitle(),provider.getContact(),provider.getStatus()));
        });

        serviceproviderItemAdapter = new ServiceproviderItemAdapter(serviceProviderItems,getActivity());
        serviceProviderRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        serviceProviderRecycleView.setAdapter(serviceproviderItemAdapter);
    }
}