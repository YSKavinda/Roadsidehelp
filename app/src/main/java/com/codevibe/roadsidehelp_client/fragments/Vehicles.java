package com.codevibe.roadsidehelp_client.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codevibe.roadsidehelp_client.R;
import com.codevibe.roadsidehelp_client.adapters.VehicleItemAdapter;
import com.codevibe.roadsidehelp_client.model.Vehicle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class Vehicles extends Fragment {

    private static Vehicles vehicles;

    private FirebaseFirestore firestore;

    private Uri imagePath;
    private FirebaseStorage firebaseStorage;

    private String categoryName;

    ImageButton imageButton;
    private FirebaseAuth firebaseAuth;

    private FirebaseUser user;

    private String status = "active";

    private ArrayList<Vehicle> vehicleItems;

    public Vehicles() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vehicles, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        user = firebaseAuth.getCurrentUser();

        vehicleItems = new ArrayList<>();
        RecyclerView itemView = getActivity().findViewById(R.id.vehicleRecyclerView);

        VehicleItemAdapter vehicleItemAdapter = new VehicleItemAdapter(vehicleItems,getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        itemView.setLayoutManager(linearLayoutManager);
        itemView.setAdapter(vehicleItemAdapter);

        firestore.collection("vehicles").whereEqualTo("owner",firebaseAuth.getCurrentUser().getEmail()).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                               vehicleItems.clear();
                               for(QueryDocumentSnapshot snapshot: task.getResult()){
                                  Vehicle vehicle = snapshot.toObject(Vehicle.class);
                                  vehicleItems.add(vehicle);
                               }
                               vehicleItemAdapter.notifyDataSetChanged();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });




        getActivity().findViewById(R.id.reg_vehicle_launch_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.vehicle_registration_dialog);
                dialog.setTitle("Vehicle Registration");
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.show();

                dialog.findViewById(R.id.regVehicleExit).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        vehicleItemAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });


//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                builder.setTitle("Vehicle Registration");
//                builder.setMessage("Enter Details & Save to Register");
//                builder.setView(LayoutInflater.from(getActivity()).inflate(R.layout.vehicle_registration_dialog, null));
//
//                //save vehicle data
//                builder.setPositiveButton("Submit", (dialog, which) -> {
//
//                  Spinner catSpinner =  dialog.;
//                    System.out.println(catSpinner.getX());
//
//                });
//                builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
//                AlertDialog dialog = builder.create();
//                dialog.show();


                Spinner spinner = dialog.findViewById(R.id.categorySpinner);
                imageButton = dialog.findViewById(R.id.vehicleImageSelector);


                ArrayList<String> arrayList = new ArrayList();


                firestore.collection("category").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot taskResult = task.getResult();
                        List<DocumentSnapshot> documents = taskResult.getDocuments();
                        documents.forEach(c -> {
                            arrayList.add(c.get("name").toString());

                        });

                        String[] items = new String[arrayList.size()];

                        for (int i = 0; i < arrayList.size(); i++) {
                            items[i] = arrayList.get(i).toString();

                        }


                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.dropdown_layout, items);
                        spinner.setAdapter(adapter);
                        spinner.setSelection(0);


                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                categoryName = spinner.getItemAtPosition(position).toString();
//                               Log.i(getTag(),"selected item " + spinner.getItemAtPosition(position).toString());
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                categoryName = spinner.getItemAtPosition(0).toString();
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        activityResultLauncher.launch(Intent.createChooser(intent, "Select Vehicle Image"));
                    }
                });


                dialog.findViewById(R.id.regVehicleReg).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText vehicleModel = dialog.findViewById(R.id.reg_vehicle_model);
                        EditText vehicleLicense = dialog.findViewById(R.id.reg_vehicle_license);
                        Spinner spinner1 = dialog.findViewById(R.id.categorySpinner);

                        if (vehicleModel.getText().toString().isEmpty()) {

                            vehicleModel.setError("Field is Empty");

                        } else if (vehicleLicense.getText().toString().isEmpty()) {

                            vehicleLicense.setError("Field is Empty");

                        } else if (categoryName == null) {

                            Toast.makeText(getContext(), "No Category is Selected", Toast.LENGTH_LONG).show();

                        } else {
                            boolean[] isVehicleRegistered = {false};
                            firestore.collection("vehicles").whereEqualTo("license_no", vehicleLicense.getText().toString())
                                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                QuerySnapshot document = task.getResult();
                                                if (document.size() > 0) {
                                                    isVehicleRegistered[0] = true;
                                                } else {
                                                    isVehicleRegistered[0] = false;
                                                }
                                            } else {
                                                Log.d(getTag(), "Error getting documents: ", task.getException());
                                            }
                                        }
                                    });
                            if (isVehicleRegistered[0]) {
                                Toast.makeText(getContext(), "Vehicle is Registered", Toast.LENGTH_LONG).show();
                            } else {


                                String imageId = UUID.randomUUID().toString();
                                Vehicle vehicle = new Vehicle(categoryName, vehicleModel.getText().toString(), vehicleLicense.getText().toString(), imageId, user.getEmail(), status);
                                ProgressDialog progressDialog = new ProgressDialog(getActivity());
                                progressDialog.setMessage("Adding new Vehicle...");
                                progressDialog.setCancelable(false);
                                progressDialog.show();

                                firestore.collection("vehicles").add(vehicle).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        if(imagePath!=null){
                                            progressDialog.setMessage("Uploading Image");
                                           StorageReference reference = firebaseStorage.getReference("vehicle-images").child(imageId);
                                           reference.putFile(imagePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                               @Override
                                               public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                   vehicleItemAdapter.notifyDataSetChanged();
                                                   progressDialog.dismiss();
                                               }
                                           }).addOnFailureListener(new OnFailureListener() {
                                               @Override
                                               public void onFailure(@NonNull Exception e) {
                                                   progressDialog.dismiss();
                                                   Toast.makeText(getActivity().getApplicationContext(), e.getMessage(),Toast.LENGTH_LONG).show();

                                               }
                                           }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                               @Override
                                               public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                                 double progress = 100*snapshot.getBytesTransferred()/snapshot.getTotalByteCount();
                                                 progressDialog.setMessage( "Uploading"+(int)progress+"%");

                                               }
                                           });
                                        }

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getActivity().getApplicationContext(), e.getMessage(),Toast.LENGTH_LONG).show();

                                    }
                                });

//                                 Vehicle myvehicle = new Vehicle()


                            }

                        }


                    }
                });


            }

            ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(

                    new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {

                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                imagePath = result.getData().getData();
//                                Log.i(getTag(),"Image Path"+imagePath.getPath());
                                Picasso.get()
                                        .load(imagePath)
                                        .resize(200, 200)
                                        .centerCrop()
                                        .into(imageButton);
//                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
//                                    ImageDecoder.Source source = ImageDecoder.createSource(getActivity().getContentResolver(),imagePath);
//                                    try {
//                                        Bitmap bitmap = ImageDecoder.decodeBitmap(source);
//                                        imageButton.setImageBitmap(bitmap);
//                                    }catch (Exception e){
//                                        Log.e(getTag(),e.getMessage());
//                                    }
//                                }else{
//                                    try {
//                                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),imagePath);
//                                        imageButton.setImageBitmap(bitmap);
//                                    }catch (Exception e){
//                                        Log.e(getTag(),e.getMessage());
//                                    }
//
//                                }


                            }
                        }
                    });

        });


    }


    public static Vehicles getInstance() {
        if (vehicles == null) {
            vehicles = new Vehicles();
        }
        return vehicles;
    }
}