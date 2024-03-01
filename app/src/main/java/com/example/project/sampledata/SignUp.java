package com.example.project.sampledata;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.project.R;
import com.example.project.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {
    ActivitySignUpBinding bind;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        bind = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();

        bind.iHaveAlreadyAnAcount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), Login.class);
                startActivity(i);
            }
        });
        bind.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String usename = bind.username.getText().toString();
                String email = bind.editTextTextEmailAddress.getText().toString().trim();
                String pass = bind.editTextTextPassword.getText().toString().trim();
                if (email.isEmpty()) {
                    Snackbar.make(bind.textView, "Email is required!", Toast.LENGTH_SHORT).show();
                }
                if (pass.isEmpty()) {
                    Snackbar.make(bind.textView, "Password is required!", Toast.LENGTH_SHORT).show();
                }

                if (!email.isEmpty() && !pass.isEmpty()) {
                    mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null) {
                                    saveUserToDatabase(user.getUid(), usename, email, pass, "default"); // You can pass the user image URL here
                                }

                                Intent i = new Intent(getBaseContext(), Login.class);
                                startActivity(i);
                                finish();
                                bind.editTextTextEmailAddress.setText("");
                                bind.editTextTextPassword.setText("");
                                Snackbar.make(bind.textView, "User registered successfuly", Snackbar.LENGTH_LONG).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Snackbar.make(bind.textView, "" + e.getMessage(), Snackbar.LENGTH_LONG).show();
                        }
                    });
                }

            }
        });


    }

    private void saveUserToDatabase(String userId, String username, String email, String password, String imageUser) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        User user = new User(userId, username, email, password, imageUser);
        userRef.setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getBaseContext(), "added", Toast.LENGTH_SHORT).show();
                        } else {
                            // Failed to save user data
                            Toast.makeText(getBaseContext(), "Failed to save user data.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}