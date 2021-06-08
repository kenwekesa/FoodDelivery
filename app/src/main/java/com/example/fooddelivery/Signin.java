package com.example.fooddelivery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Signin extends AppCompatActivity {

    TextView forgotpassword_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        forgotpassword_view = findViewById(R.id.textView5);

        forgotpassword_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Signin.this,ResetPassword.class));
            }
        });
    }


    private void firebaseLogin()
    {
        login_progress.setVisibility(View.VISIBLE);

        fbAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful())
                {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user.isEmailVerified())
                    {
                        reference = FirebaseDatabase.getInstance().getReference("Users");
                        loged_user = FirebaseAuth.getInstance().getCurrentUser();

                        userID = loged_user.getUid();

                        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                User user_data = snapshot.getValue(User.class);
                                if(user_data!=null)
                                {

                                        Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_LONG).show();
                                        login_progress.setVisibility(View.GONE);
                                        startActivity(new Intent(getApplicationContext(), AdministratorActivity.class));




                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                    else
                    {
                        user.sendEmailVerification();
                        login_progress.setVisibility(View.GONE);
                        Snackbar snackbar = Snackbar
                                .make(findViewById(android.R.id.content), "Email confirmation required, login to your email and confirm", Snackbar.LENGTH_LONG);
                        snackbar.show();

                    }
                }
                else
                {    login_progress.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this,"Failed to login, kindly check your credentials", Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}