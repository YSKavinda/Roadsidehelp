package com.codevibe.roadsidehelp_client;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.codevibe.roadsidehelp_client.model.Driver;
import com.codevibe.roadsidehelp_client.services.UserService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RegisterUserActivity extends AppCompatActivity {

    EditText etUsername, etEmail, etMobile, etPassword, etCheck;

    private static final String TAG = MainActivity.class.getName();

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        loadUIThings();

        firebaseAuth = FirebaseAuth.getInstance();


        Button registerButton = findViewById(R.id.regUserButton);


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                etUsername = findViewById(R.id.textfieldusername);
                etEmail = findViewById(R.id.textfieldEmail);
                etMobile = findViewById(R.id.textfieldmobile);
                etPassword = findViewById(R.id.textFieldPw);
                etCheck = findViewById(R.id.textFieldPw2);

                if (fieldValidator()) {
                    if(isDriverRegistered(etEmail.getText().toString(),etMobile.getText().toString())){
                        Toast.makeText(RegisterUserActivity.this, "Driver Already Registered", Toast.LENGTH_LONG).show();
                        return;
                    }

                    Driver driver = new Driver();

                    driver.setUsername(etUsername.getText().toString());
                    driver.setEmail(etEmail.getText().toString());
                    driver.setMobile(etMobile.getText().toString());
                    driver.setPassword(etPassword.getText().toString());
                    driver.setActive(true);

                    UserService service = new UserService();

                    if (service.registerUsers(driver)) {
                        Toast.makeText(RegisterUserActivity.this, "User Registration Success", Toast.LENGTH_LONG).show();
                        authUserOnFireBase(etEmail.getText().toString(), etPassword.getText().toString());

                    } else {
                        Toast.makeText(RegisterUserActivity.this, "Registration Failed", Toast.LENGTH_LONG).show();
                    }

                }


            }
        });


    }

    public void isUserRegistered(String email,String password){

    }

    public boolean fieldValidator() {

        String mobileregex = "07[0,1,2,4,6,7,8][0-9]{7}$";
        String passwordregex = "^(?=.*[A-Za-z])(?=.*\\d).{9,}$";


        if (etUsername.length() == 0) {
            etUsername.setError("Field is Empty");
            return false;
        } else if (etUsername.getText().toString().isEmpty()) {
            etUsername.setError("Field is Empty");
            return false;
        } else if (!etMobile.getText().toString().matches(mobileregex)) {
            etMobile.setError("Field is Invalid");
            return false;
        } else if (!etEmail.getText().toString().matches(Patterns.EMAIL_ADDRESS.toString())) {
            etEmail.setError("Field is Invalid");
            return false;
        } else if (!etPassword.getText().toString().matches(passwordregex)) {
            etPassword.setError("Invalid Password");
            return false;
        } else if (!etCheck.getText().toString().matches(passwordregex)) {
            etCheck.setError("Invalid Password");
            return false;
        } else if (!etPassword.getText().toString().equals(etCheck.getText().toString())) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView myLabel = findViewById(R.id.displayLabel);
                    myLabel.setText("Passwords Doesn't Match");
                    myLabel.setVisibility(View.VISIBLE);
                }
            });
            return false;
        } else {
            return true;
        }
    }

    //load UI Materials
    public void loadUIThings() {
        ImageView imageView = findViewById(R.id.logoinReg);
        Picasso.get().load(R.mipmap.ic_launcher_foreground).into(imageView);

        Button toggle = findViewById(R.id.gotoSignin);

        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterUserActivity.this, Login.class);
                startActivity(intent);

            }
        });

    }


    //hadanna driver email,mobile already registered da balanna
    public boolean isDriverRegistered(String email, String mobile) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

       boolean[] changer = {false};
        try{
       Thread userSearcht = new Thread(new Runnable() {
           @Override
           public void run() {
               db.collection("drivers")
                       .whereEqualTo("email",etEmail.getText().toString())
                       .whereEqualTo("mobile",etMobile.getText().toString())
                       .get()
                       .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                           @Override
                           public void onComplete(@NonNull Task<QuerySnapshot> task) {

                               if (task.isSuccessful()) {
                                   QuerySnapshot document = task.getResult();

                                   if(document.size()!=0){
                                       changer[0] = true;
                                   }else{
                                       changer[0] = false;
                                   }



                               } else {
                                   Log.d(TAG, "Error getting documents: ", task.getException());
                               }
                           }
                       });
           }

       });
       userSearcht.start();

           userSearcht.join();
       }catch (Exception e){
           Log.i(TAG,"exception occured in Registration Thread"+e);
       }


        return changer[0];



    }

    public void authUserOnFireBase(String email, String pw) {
        firebaseAuth.createUserWithEmailAndPassword(email, pw)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.i(TAG, "createUsers with Email & Password : Success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            user.sendEmailVerification();
                            Toast.makeText(RegisterUserActivity.this, "Please Verify your Email", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(RegisterUserActivity.this,Login.class);
                            startActivity(intent);


                        } else {

                            Log.w(TAG, "createUsers with Email & Password : Fail");
                            Toast.makeText(RegisterUserActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }


}