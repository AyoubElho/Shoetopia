package com.example.project.sampledata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.example.project.R;
import com.example.project.databinding.ActivityViewProductBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ViewProduct extends AppCompatActivity {
    // View binding
    ActivityViewProductBinding bind;

    // Firebase authentication
    FirebaseAuth mAuth;

    // Firebase database reference
    private DatabaseReference mDatabase;

    // Selected size value
    int value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the view using view binding
        bind = ActivityViewProductBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());
        // Initialize Firebase components
        mDatabase = FirebaseDatabase.getInstance().getReference("AddToCart");
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Set up toolbar navigation
        bind.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the main activity
                Intent i = new Intent(getBaseContext(), MainActivity.class);
                startActivity(i);
            }
        });

        // Retrieve product details from the intent
        Intent i = getIntent();
        Glide.with(getBaseContext()).load(i.getStringExtra("image")).into(bind.productImage);
        bind.priceProduct.setText(i.getStringExtra("price"));
        bind.descriptionProduct.setText(i.getStringExtra("desc"));
        bind.ratingBar.setRating(i.getIntExtra("rate", 0));
        bind.nameProduct.setText(i.getStringExtra("name"));
        ;
        bind.toolbar.setTitle(i.getStringExtra("category"));

        bind.sizes.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.size39) {
                    value = 39;
                } else if (checkedId == R.id.size40) {
                    value = 40;

                }
            }
        });


        // Handle "Add to Bag" button click
        bind.addToBag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if a user is logged in
                if (currentUser != null) {
                    String saveCurrentDate, saveCurrentTime;
                    Calendar calForDate = Calendar.getInstance();

                    // Format the current date
                    SimpleDateFormat currentDate = new SimpleDateFormat("dd, yyyy");
                    saveCurrentDate = currentDate.format(calForDate.getTime());

                    // Format the current time
                    SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
                    saveCurrentTime = currentTime.format(calForDate.getTime());

                    // Check if a size is selected
                    if (value != 0) {
                        // Create a HashMap to store cart item details
                        final HashMap<String, Object> cartMap = new HashMap<>();
                        cartMap.put("id", mDatabase.push().getKey());
                        cartMap.put("productName", i.getStringExtra("name"));
                        cartMap.put("productPrice", i.getStringExtra("price"));
                        cartMap.put("imgProduct", i.getStringExtra("image"));
                        cartMap.put("currentDate", saveCurrentDate);
                        cartMap.put("currentTime", saveCurrentTime);
                        cartMap.put("size", value);
                        cartMap.put("Quantité", 1);

                        // Add the item to the user's cart in the database
                        mDatabase.child(currentUser.getUid()).child(cartMap.get("id").toString()).setValue(cartMap);
                        Snackbar.make(bind.productImage, "Added to cart", Snackbar.LENGTH_SHORT).show();
                    } else {
                        // Display a Snackbar message if size is not selected
                        Snackbar.make(bind.productImage, "Select size", Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    // Redirect to SignUp activity if user is not logged in
                    Intent i = new Intent(getBaseContext(), SignUp.class);
                    startActivity(i);
                }
            }
        });
    }

}

