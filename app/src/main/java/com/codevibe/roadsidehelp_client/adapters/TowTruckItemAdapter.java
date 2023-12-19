package com.codevibe.roadsidehelp_client.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codevibe.roadsidehelp_client.R;
import com.codevibe.roadsidehelp_client.fragments.TowTrucks;
import com.codevibe.roadsidehelp_client.model.TowTruck;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class TowTruckItemAdapter extends RecyclerView.Adapter<TowTruckItemAdapter.ViewHolder> {
    private static final String TAG =TowTruckItemAdapter.class.getName();
    private ArrayList<TowTruck> items;
    private Context context;
    private FirebaseStorage firebaseStorage;

    private FirebaseFirestore firestore;

    private FirebaseAuth firebaseAuth;

    public TowTruckItemAdapter(ArrayList<TowTruck> items, Context context) {
        this.items = items;
        this.context = context;
        this.firebaseStorage = FirebaseStorage.getInstance();
        this.firestore = FirebaseFirestore.getInstance();
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public TowTruckItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.towtruck_item_layout,parent,false);
        return new TowTruckItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TowTruckItemAdapter.ViewHolder holder, int position) {
        TowTruck item = items.get(position);
        holder.towTruckName.setText(item.getName());
        holder.contact.setText(item.getContact());
        holder.address.setText(item.getTowTruckAddress());
        holder.status.setText(item.getStatus());
        holder.locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+item.getContact().toString()));
                TowTrucks.getInstance().getCallforTowTruck(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView towTruckName,contact,address, status;
        Button locationButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            towTruckName = itemView.findViewById(R.id.tow_truck_name);
            contact= itemView.findViewById(R.id.tow_truck_contact);
            address=itemView.findViewById(R.id.tow_truck_address);
            status = itemView.findViewById(R.id.tow_truck_distance);
            locationButton = itemView.findViewById(R.id.tow_truck_location_button);

        }

    }
}
