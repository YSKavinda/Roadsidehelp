package com.codevibe.roadsidehelp_client;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class Login extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    private static final String TAG = Login.class.getName();

    EditText emailEdit, pwEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()!=null){
            updateUI(firebaseAuth.getCurrentUser());
        }else{
            loadUIThings();
        }


    }

    public void loadUIThings() {
        emailEdit = findViewById(R.id.loginEmail);
        pwEdit = findViewById(R.id.loginPassword);
        ImageView imageView = findViewById(R.id.loginLogo);
        Picasso.get().load(R.mipmap.ic_launcher_foreground).into(imageView);

        Button toggle = findViewById(R.id.gotoSignUp);

        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, RegisterUserActivity.class);
                startActivity(intent);
            }
        });

        Button login = findViewById(R.id.loginButton);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String email = emailEdit.getText().toString();
                String pw = pwEdit.getText().toString();

                int validationId = isUserDataValidated(email, pw);

                if (validationId == 1) {
                    TextView labeltext = findViewById(R.id.loginLabel);
                    labeltext.setText("Enter your Email");
                } else if (validationId == 2) {
                    TextView labeltext = findViewById(R.id.loginLabel);
                    labeltext.setText("Enter your Password");
                } else {

                    firebaseAuth.signInWithEmailAndPassword(email, pw)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        updateUI(firebaseAuth.getCurrentUser());

                                    } else {
                                        Log.w(TAG, "createUsers with Email & Password : Fail");

                                        TextView labeltext = findViewById(R.id.loginLabel);
                                        labeltext.setText("Username or Password is incorrect");

                                        Toast.makeText(Login.this, "Login Failed", Toast.LENGTH_SHORT).show();

                                    }


                                }
                            });
                }


            }
        });
    }


    private int isUserDataValidated(String email, String pw) {
        if (email.isEmpty()) {
            return 1;
        } else if (pw.isEmpty()) {
            return 2;
        } else {
            return 3;
        }

    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            if (!user.isEmailVerified()) {
                Toast.makeText(Login.this, "Please Verify your Email", Toast.LENGTH_LONG).show();
                return;
            }

            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}