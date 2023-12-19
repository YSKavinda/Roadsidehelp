package com.codevibe.roadsidehelp_client.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codevibe.roadsidehelp_client.R;
import com.codevibe.roadsidehelp_client.model.Vehicle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class VehicleItemAdapter extends RecyclerView.Adapter<VehicleItemAdapter.ViewHolder> {

    private static final String TAG = VehicleItemAdapter.class.getName();
    private ArrayList<Vehicle> items;
    private Context context;
    private FirebaseStorage firebaseStorage;

    private FirebaseFirestore firestore;

    private FirebaseAuth firebaseAuth;

    public VehicleItemAdapter(ArrayList<Vehicle> items, Context context) {
        this.items = items;
        this.context = context;
        this.firebaseStorage = FirebaseStorage.getInstance();
        this.firestore = FirebaseFirestore.getInstance();
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public VehicleItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.vehicle_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleItemAdapter.ViewHolder holder, int position) {
       Vehicle item = items.get(position);
       holder.modelNo.setText(item.getModel_no());
       holder.category.setText(item.getCategory());
       holder.LicenseNo.setText(item.getLicense_no());
       holder.status.setText(item.getStatus());
       holder.updateButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               deleteVehicle(item.getModel_no());

           }
       });

       firebaseStorage.getReference("vehicle-images/"+item.getImage())
               .getDownloadUrl()
               .addOnSuccessListener(new OnSuccessListener<Uri>() {
                   @Override
                   public void onSuccess(Uri uri) {
                       Log.i(TAG,"image Uri"+uri);
                       if(uri.getPath().toString().isEmpty()){
                           Picasso.get()
                                   .load("https://i.imgur.com/DvpvklR.png")
                                   .resize(200,200)
                                   .centerCrop()
                                   .into(holder.vehicleImage);
                       }else{
                           Picasso.get()
                                   .load(uri)
                                   .resize(200,200)
                                   .centerCrop()
                                   .into(holder.vehicleImage);
                       }

                   }
               });

    }

    public void deleteVehicle(String model){
    firestore.collection("vehicles").whereEqualTo("model_no",model).whereEqualTo("owner",firebaseAuth.getCurrentUser().getEmail())
            .get().addOnCompleteListener(
                    new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    DocumentReference documentRef = document.getReference();


                                    //Deleting the document
                                    documentRef.delete().addOnCompleteListener(deleteTask -> {
                                        if (deleteTask.isSuccessful()) {
                                            Toast.makeText(context,"Vehicle Deleted!",Toast.LENGTH_LONG).show();
                                        } else {
                                            Log.i(TAG,"Error Ocurred");
                                        }
                                    });
                                }
                            }else{
                                Log.i(TAG,"task failed");
                            }
                        }
                    }
            );
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView modelNo,LicenseNo,category,status;
        ImageView vehicleImage;
        Button updateButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            modelNo = itemView.findViewById(R.id.vehicle_item_model);
            category = itemView.findViewById(R.id.vehicle_item_category);
            LicenseNo = itemView.findViewById(R.id.vehicle_item_license);
            status = itemView.findViewById(R.id.vehicle_item_status);
            vehicleImage = itemView.findViewById(R.id.vehicle_item_image);
            updateButton = itemView.findViewById(R.id.vehicle_item_button);
        }
    }
}
