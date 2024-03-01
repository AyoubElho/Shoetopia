package com.example.project.sampledata;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.project.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllProduct extends Fragment {

    // Declare necessary variables
    ArrayList<GridBrand> itemsGrid;
    RecyclerView gridRc;
    ShimmerFrameLayout shimmerFrameLayout;
    DatabaseReference myRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View binding = inflater.inflate(R.layout.fragment_all_product, container, false);
        shimmerFrameLayout = binding.findViewById(R.id.shimmer);
        // Initialize RecyclerView and data list

        gridRc = binding.findViewById(R.id.gridRecycle);
        gridRc.setVisibility(View.GONE);
        itemsGrid = new ArrayList<>();

        // Link RecyclerView with Adapter
        GridAdapter adapterGr = new GridAdapter(getActivity(), itemsGrid);
        gridRc.setAdapter(adapterGr);
        gridRc.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        // Display category < all > products from Firebase Realtime Database
        myRef = FirebaseDatabase.getInstance().getReference("Products").child("All");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Clear the existing list
                itemsGrid.clear();

                // Iterate through the DataSnapshot to retrieve product data
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    GridBrand grid = snapshot1.getValue(GridBrand.class);
                    itemsGrid.add(grid);
                }

                // Notify the adapter that the data has changed
                adapterGr.notifyDataSetChanged();
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                gridRc.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors that occur during the data retrieval process
            }
        });

        // Disable nested scrolling for the RecyclerView
        gridRc.setNestedScrollingEnabled(false);

        // Return the inflated view
        return binding;
    }

}
