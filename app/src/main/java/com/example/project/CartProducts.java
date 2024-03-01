package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Toast;

import com.example.project.databinding.ActivityCartProductsBinding;
import com.example.project.sampledata.MainActivity;
import com.example.project.sampledata.ViewProduct;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class CartProducts extends AppCompatActivity {

    ActivityCartProductsBinding bind;
    private DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    CardAdapter adapter;

    ArrayList<AddToCart> cardItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_products);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        bind = ActivityCartProductsBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        cardItemList= new ArrayList<>();
        Query query = FirebaseDatabase.getInstance().getReference("AddToCart").child(currentUser.getUid());


        FirebaseRecyclerOptions<AddToCart> options =
                new FirebaseRecyclerOptions.Builder<AddToCart>()
                        .setQuery(query, AddToCart.class)
                        .build();

        adapter = new CardAdapter(options);
        bind.recycle.setAdapter(adapter);
        bind.recycle.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                    float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addBackgroundColor(ContextCompat.getColor(recyclerView.getContext(), R.color.swip))
                        .addActionIcon(R.drawable.delete_icon)
                        .create()
                        .decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }


            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getBindingAdapterPosition();


                String itemId = cardItemList.get(position).getId();
                cardItemList.remove(position);
                adapter.notifyItemRemoved(position);
                mDatabase.child(itemId).removeValue();

            }


        };

        new ItemTouchHelper(simpleCallback).attachToRecyclerView(bind.recycle);


        bind.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), MainActivity.class);
                startActivity(i);
            }
        });

        if (currentUser != null) {
            mDatabase = FirebaseDatabase.getInstance().getReference("AddToCart").child(currentUser.getUid());
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Double total = 0.0;
                    cardItemList.clear();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        AddToCart addToCart = snapshot1.getValue(AddToCart.class);
                        if (addToCart != null) {
                            String productPriceString = addToCart.getProductPrice().replace("$", "");
                            Double price = addToCart.getQuantité() * Double.valueOf(productPriceString);
                            total += price;
                            cardItemList.add(addToCart);
                        }
                        adapter.notifyDataSetChanged();

                    }
                    bind.totalPrice.setText(total + "$");

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        ;

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}