package com.example.fooddelivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Signin extends AppCompatActivity {

    EditText emailfield, passwordfield;
    TextView forgotpassword_view;
    ProgressBar signin_progressbar;
    Button signin_btn;
    String email, pass;


    private FirebaseFirestore db;
    public CollectionReference collection_ref;
    private FirebaseAuth fbAuth;
    private FirebaseUser loged_user;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        //-------------------Initialising the views----------------------
        emailfield = findViewById(R.id.emailfield);
        passwordfield = findViewById(R.id.passwordfield);
        forgotpassword_view = findViewById(R.id.forgotpassword_view);
        signin_progressbar = findViewById(R.id.signin_bar);
        signin_btn= findViewById(R.id.signin_btn);




        db = FirebaseFirestore.getInstance();
        collection_ref = db.collection("users");
        fbAuth = FirebaseAuth.getInstance();

        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //----------------------Getting email and password from views---------------
                email = emailfield.getText().toString();
                pass = passwordfield.getText().toString();
                if(email.isEmpty())
                {
                    emailfield.setError("Email cannot be empty");
                    emailfield.requestFocus();
                }
                else if(pass.isEmpty())
                {
                    passwordfield.setError("Password cannot be empty.");
                    passwordfield.requestFocus();
                }
                else {
                    firebaseLogin();
                }
            }
        });
        forgotpassword_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Signin.this,ResetPassword.class));
            }
        });
    }


    private void firebaseLogin()
    {

       // signin_progressbar.setVisibility(View.VISIBLE);
        ProgressDialog progressDialog = new ProgressDialog(Signin.this);
        progressDialog.setMessage("Signing in...");
        progressDialog.show();

        fbAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful())
                {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user.isEmailVerified())
                    {
                        startActivity(new Intent(Signin.this, Home.class));

                    }
                    else
                    {
                        user.sendEmailVerification();
                        progressDialog.dismiss();
                        signin_progressbar.setVisibility(View.GONE);
                        Snackbar snackbar = Snackbar
                                .make(findViewById(android.R.id.content), "Email confirmation required, login to your email and confirm", Snackbar.LENGTH_LONG);
                        snackbar.show();

                    }
                }
                else
                {
                    progressDialog.dismiss();
                    signin_progressbar.setVisibility(View.GONE);
                    Toast.makeText(Signin.this,"Failed to login, kindly check your credentials", Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}