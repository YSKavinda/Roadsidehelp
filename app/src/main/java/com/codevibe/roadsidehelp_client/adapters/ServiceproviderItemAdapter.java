package com.codevibe.roadsidehelp_client.adapters;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codevibe.roadsidehelp_client.MainActivity;
import com.codevibe.roadsidehelp_client.R;
import com.codevibe.roadsidehelp_client.fragments.ServiceCenters;
import com.codevibe.roadsidehelp_client.uiItems.ServiceProviderItem;

import java.util.List;

public class ServiceproviderItemAdapter extends RecyclerView.Adapter<ServiceproviderItemAdapter.ServiceProviderViewHolder> {

    private List<ServiceProviderItem> serviceProviderList;
    private Context appcontext;

    public ServiceproviderItemAdapter(List<ServiceProviderItem> serviceProviderList, Context appcontext) {
        this.appcontext = appcontext;
        this.serviceProviderList = serviceProviderList;
    }

    @NonNull
    @Override
    public ServiceProviderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(appcontext).inflate(R.layout.serviceprovider_item, parent, false);
        return new ServiceProviderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceProviderViewHolder holder, int position) {
        ServiceProviderItem serviceProviderItem = serviceProviderList.get(position);
        holder.spImageThumbnail.setImageResource(serviceProviderItem.getSpImageThumbnail());
        holder.title.setText(serviceProviderItem.getTitle());
        holder.contact.setText(serviceProviderItem.getContact());
        holder.status.setText(serviceProviderItem.getStatus());
        holder.track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getDirections(String.valueOf(6.7201),String.valueOf(80.1508));
            }
        });
    }

    public void getDirections(String lat , String lng){
        String latitude =  lat;
        String longitude = lng;
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latitude + "," + longitude);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        ServiceCenters.getInstance().setLoccationToMaps(mapIntent);
    }

    @Override
    public int getItemCount() {
        Log.i(ServiceproviderItemAdapter.class.getName(),"count :"+serviceProviderList.size());
        return serviceProviderList.size();
    }

    public static class ServiceProviderViewHolder extends RecyclerView.ViewHolder {
        ImageView spImageThumbnail;
        TextView title;
        TextView contact;
        TextView status;
        Button track;

        public ServiceProviderViewHolder(@NonNull View itemView) {
            super(itemView);
            spImageThumbnail = itemView.findViewById(R.id.spImageThumbnail);
            title = itemView.findViewById(R.id.spItemTitle);
            contact = itemView.findViewById(R.id.spItemContact);
            status = itemView.findViewById(R.id.spItemStatus);
            track = itemView.findViewById(R.id.spItemTrack);

        }
    }
}
