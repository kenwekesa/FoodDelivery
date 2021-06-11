package com.example.fooddelivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Signup extends AppCompatActivity {

    EditText passwordfield, namefield, password2field, phonefield,emailfield;
    Button submit_btn;
    String email, pass,phone,name;

    ProgressBar signup_progressbar;

    //---------------Firebase------//
    private FirebaseAuth fbAuth;
    public CollectionReference collection_ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        emailfield = findViewById(R.id.emailfield);
        phonefield = findViewById(R.id.phonefield);
        password2field = findViewById(R.id.password2field);
        passwordfield = findViewById(R.id.passwordfield);
        namefield = findViewById(R.id.namefield);

        signup_progressbar = findViewById(R.id.signup_bar);
        submit_btn = findViewById(R.id.submitbtn);

        //-----------------firebase---------------------------


        FirebaseApp.initializeApp(this);
        fbAuth = FirebaseAuth.getInstance();
        collection_ref = FirebaseFirestore.getInstance().collection("users");





        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email= emailfield.getText().toString();
                pass = passwordfield.getText().toString();
                phone = phonefield.getText().toString();
                name = namefield.getText().toString();

                if(!Patterns.EMAIL_ADDRESS.matcher(emailfield.getText().toString()).matches())
                {
                    emailfield.setError("Invalid email address");
                    emailfield.requestFocus();
                }
                else
                {
                    if(phonefield.getText().toString().isEmpty())
                    {
                        phonefield.setError("Phone field cannot be empty");
                        phonefield.requestFocus();
                    }
                    else
                    {
                        if(passwordfield.getText().toString().length()<6)
                        {
                            passwordfield.setError("Password too weak, should be at least 6 characters.");
                            passwordfield.requestFocus();
                        }
                        else {
                            if (!password2field.getText().toString().equals(passwordfield.getText().toString())) {
                                password2field.setError("Passwords doesn't match");
                                password2field.requestFocus();
                            } else {
                                if (namefield.getText().toString().isEmpty())
                                {
                                    namefield.setError("Name cannot be blank");
                                    namefield.requestFocus();
                                }
                                else
                                {
                                    //Success, cann now submit
                                    firebaseUser();
                                }
                            }
                        }
                        }
            }}
        });

    }



    private void firebaseUser()
    {
        signup_progressbar.setVisibility(View.VISIBLE);
        fbAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            final User user = new User(name,email,phone,pass);

                            FirebaseDatabase.getInstance().getReference("Users").child(
                                    FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if(task.isSuccessful())

                                            {
                                                FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
                                                signup_progressbar.setVisibility(View.GONE);

                                                AlertDialog.Builder builder_b = new AlertDialog.Builder(Signup.this);
                                                builder_b.setTitle("User registration");
                                                builder_b.setMessage("User successfully registerd.\n\nA confirmation email has been sent to you, confirm it to login.");
                                                AlertDialog alert2 = builder_b.create();
                                                alert2.show();
                                                alert2.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                                    @Override
                                                    public void onCancel(DialogInterface dialogInterface) {
                                                        finish();
                                                        //(new LoginActivity()).instance.finish();
                                                        startActivity(new Intent(getApplicationContext(),Signin.class));

                                                    }
                                                });
                                            }
                                            else
                                            {
                                                Toast.makeText(Signup.this,"Error occured, registration failed.",Toast.LENGTH_LONG).show();
                                                signup_progressbar.setVisibility(View.GONE);
                                            }
                                        }
                                    });

                            //FirebaseUser user = fbAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            signup_progressbar.setVisibility(View.GONE);
                            Toast.makeText(Signup.this, "Authentication failed, perhaps the user is already registered.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}