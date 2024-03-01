package com.example.project.sampledata;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.project.AddToCart;
import com.example.project.CartProducts;
import com.example.project.R;
import com.example.project.databinding.ActivityMainBinding;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.badge.ExperimentalBadgeUtils;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.view.View;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding bind;
    FirebaseAuth mAuth;
    DatabaseReference myRef;
    FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content view using data binding
        setContentView(R.layout.activity_main);
        bind = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        // Initialize the default fragment

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.productFrame, new AllProduct()).commit();


        // Toolbar menu item click listener
        bind.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Handle toolbar menu item clicks
                int id = item.getItemId();
                if (id == R.id.logout) {
                    // Sign out when logout is clicked
                    FirebaseAuth.getInstance().signOut();
                    recreate();

                } else if (item.getItemId() == R.id.order) {
                    // Navigate to CartProducts or SignUp based on user authentication status
                    Intent i;
                    if (currentUser != null) {
                        i = new Intent(getBaseContext(), CartProducts.class);
                    } else {
                        i = new Intent(getBaseContext(), SignUp.class);
                    }
                    startActivity(i);
                }
                return false;
            }
        });

        // Toolbar navigation icon click listener
        bind.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the navigation drawer when the navigation icon is clicked
                bind.drawer.open();
            }
        });

        // RadioGroup change listener for category selection
        bind.Category.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Change text color based on category selection and replace the fragment
                bind.All.setTextColor(Color.rgb(184, 184, 184));
                bind.running.setTextColor(Color.rgb(184, 184, 184));

                Fragment All = null;
                if (checkedId == R.id.All) {
                    bind.All.setTextColor(Color.WHITE);
                    All = new AllProduct();
                } else if (checkedId == R.id.running) {
                    bind.running.setTextColor(Color.WHITE);
                    All = new Runining();
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.productFrame, All).commit();
            }
        });

        // Firebase authentication and badge update

        if (currentUser != null) {
            myRef = FirebaseDatabase.getInstance().getReference("AddToCart").child(currentUser.getUid());
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // Update badge count based on the number of items in the cart
                    badgeOrder((int) snapshot.getChildrenCount());

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle database error if needed
                }
            });
        }


    }

    @OptIn(markerClass = ExperimentalBadgeUtils.class)
    public void badgeOrder(int countChildren) {
        int badgeValue;
        if (countChildren != 0) {
            badgeValue = countChildren;
        } else {
            badgeValue = 0;
        }
        BadgeDrawable badgeDrawable = BadgeDrawable.create(MainActivity.this);
        badgeDrawable.setBadgeGravity(BadgeDrawable.TOP_END);
        badgeDrawable.setNumber(badgeValue);
        BadgeUtils.attachBadgeDrawable(badgeDrawable, bind.toolbar.findViewById(R.id.order));

    }


}
