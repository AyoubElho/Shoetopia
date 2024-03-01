package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project.sampledata.MainActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CardAdapter extends FirebaseRecyclerAdapter<AddToCart, CardAdapter.CardViewHolder> {

    private DatabaseReference ordersRef;
    private FirebaseUser firebaseUser;

    public CardAdapter(@NonNull FirebaseRecyclerOptions<AddToCart> options) {
        super(options);
    }


    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orders, parent, false);
        return new CardViewHolder(view);
    }


    @Override
    protected void onBindViewHolder(@NonNull CardViewHolder holder, int position, @NonNull AddToCart model) {
        Glide.with(holder.itemView.getContext()).load(model.getImgProduct()).into(holder.imgOrder);
        holder.ProductName.setText(model.getProductName());
        holder.ProductPrice.setText(model.getProductPrice());

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plus(model, holder);
            }
        });

        holder.moins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moins(model, holder);
            }
        });
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {
        ImageView imgOrder;
        TextView ProductName, counterTextView;
        TextView ProductPrice;
        ImageButton plus, moins;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            plus = itemView.findViewById(R.id.plus);
            counterTextView = itemView.findViewById(R.id.countertextView);
            moins = itemView.findViewById(R.id.moins);
            imgOrder = itemView.findViewById(R.id.imageViewProduct);
            ProductName = itemView.findViewById(R.id.textViewProductName);
            ProductPrice = itemView.findViewById(R.id.textViewProductPrice);
        }
    }


    public void plus(AddToCart model, CardViewHolder holder) {
        int count = model.getQuantité() + 1;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        ordersRef = FirebaseDatabase.getInstance().getReference("AddToCart").child(firebaseUser.getUid()).child(model.getId());
        HashMap<String, Object> objectHashMap = new HashMap<>();
        objectHashMap.put("Quantité", count);
        ordersRef.updateChildren(objectHashMap);
        holder.counterTextView.setText(String.valueOf(count));
    }

    public void moins(AddToCart model, CardViewHolder holder) {
        int count = model.getQuantité() - 1;
        if (count >= 1) {
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            ordersRef = FirebaseDatabase.getInstance().getReference("AddToCart").child(firebaseUser.getUid()).child(model.getId());
            HashMap<String, Object> objectHashMap = new HashMap<>();
            objectHashMap.put("Quantité", count);
            ordersRef.updateChildren(objectHashMap);
            holder.counterTextView.setText(String.valueOf(count));
        }
    }


}
