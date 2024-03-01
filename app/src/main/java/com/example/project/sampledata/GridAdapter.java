package com.example.project.sampledata;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

// Adapter class for the grid view
public class GridAdapter extends RecyclerView.Adapter<GridAdapter.BrandViewHolder> {

     Context c;
    ArrayList<GridBrand> itemsGrid;
    String category;


    // Constructor to initialize the adapter with data and context
    public GridAdapter(Context c, ArrayList<GridBrand> itemsGrid) {
        this.c = c;
        this.itemsGrid = itemsGrid;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public BrandViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout and create a new ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid, parent, false);
        return new BrandViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull BrandViewHolder holder, int position) {
        // Bind data to the ViewHolder
        category = FirebaseDatabase.getInstance().getReference("All").getKey();
        holder.brandNameGrid.setText(itemsGrid.get(position).getNameProduct());
        holder.brandPrixGrid.setText(itemsGrid.get(position).getPrix() + "$");
        Glide.with(c).load(itemsGrid.get(position).getImage()).into(holder.imgItemGrid);


        // Set a click listener to handle item clicks
        holder.nextButton.setOnClickListener(v -> {
            sendDataToViewProduct(position);
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return itemsGrid.size();
    }

    // ViewHolder class to hold references to the views within each item view
    public class BrandViewHolder extends RecyclerView.ViewHolder {
        TextView brandNameGrid, brandPrixGrid;
        CardView itemCard;
        ImageView nextButton;
        ShapeableImageView imgItemGrid;
        // Constructor to initialize the views
        public BrandViewHolder(@NonNull View itemView) {
            super(itemView);
            nextButton = itemView.findViewById(R.id.nextButton);
            brandNameGrid = itemView.findViewById(R.id.Tv_item_name_grid);
            brandPrixGrid = itemView.findViewById(R.id.Tv_item_Prix_grid);
            imgItemGrid = itemView.findViewById(R.id.img_item_grid);
        }
    }

    // Method to send data to the ViewProduct activity
    public void sendDataToViewProduct(int position) {
        Intent i = new Intent(c, ViewProduct.class);
        i.putExtra("category", category);
        i.putExtra("rate" ,itemsGrid.get(position).getRate());
        i.putExtra("image", itemsGrid.get(position).getImage());
        i.putExtra("price", itemsGrid.get(position).getPrix() + "$");
        i.putExtra("desc", itemsGrid.get(position).getDescription());
        i.putExtra("name", itemsGrid.get(position).getNameProduct());
        c.startActivity(i);

    }



}
