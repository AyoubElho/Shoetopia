package com.example.project.sampledata;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.project.R;
import com.example.project.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        binding.Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = binding.editTextTextEmailAddress.getText().toString().trim();
                String pass = binding.editTextTextPassword.getText().toString().trim();
                if (user.isEmpty()) {
                    Toast.makeText(Login.this, "Email is required!", Toast.LENGTH_SHORT).show();
                }
                if (pass.isEmpty()) {
                    Toast.makeText(Login.this, "Password is required!", Toast.LENGTH_SHORT).show();
                }
                if (!user.isEmpty() && !pass.isEmpty()) {
                    mAuth.signInWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent is = new Intent(Login.this, MainActivity.class);
                                is.putExtra("email", user);
                                startActivity(is);
                                finish();
                                Snackbar.make(binding.Login, "Login succuesfuly", Snackbar.LENGTH_LONG).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Snackbar.make(binding.Login, "" + e.getMessage(), Snackbar.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        binding.acount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, MainActivity.class);
                startActivity(i);
            }
        });


    }
}