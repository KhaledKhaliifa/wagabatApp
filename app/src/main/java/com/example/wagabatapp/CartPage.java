package com.example.wagabatapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.wagabatapp.databinding.ActivityCartPageBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CartPage extends AppCompatActivity {
    ActivityCartPageBinding binding;
    LinearLayoutManager layoutManager;
    RecyclerView recyclerView;
    ArrayList<DishModel> list;
    CartAdapter adapter;
    DatabaseReference databaseReference;
    DatabaseReference restaurantReference;
    Float total_cost;
    Float delivery_cost;
    Float subtotal_cost;
    String restaurantID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        recyclerView = findViewById(R.id.recyclerViewCart);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        list = new ArrayList<>();
        adapter = new CartAdapter(list);
        total_cost = Float.valueOf(0);
        delivery_cost = Float.valueOf(0);
        subtotal_cost = Float.valueOf(0);

        databaseReference = FirebaseDatabase.getInstance("https://wagbaapp-default-rtdb.europe-west1.firebasedatabase.app/").getReference("cart");


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    DishModel dish = dataSnapshot.getValue(DishModel.class);
                    restaurantID = String.valueOf(dish.getReference().charAt(0));
                    list.add(dish);
                    subtotal_cost += Float.valueOf(dish.getPrice()) * Float.valueOf(dish.getItemCount());
                }
                restaurantReference = FirebaseDatabase.getInstance("https://wagbaapp-default-rtdb.europe-west1.firebasedatabase.app/")
                        .getReference("restaurants/restaurant"+restaurantID+"/delivery_fee");
                restaurantReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        delivery_cost=  Float.valueOf(snapshot.getValue().toString());
                        binding.deliveryFeeAmount.setText(delivery_cost.toString());
                        binding.subtotalAmount.setText(subtotal_cost.toString());
                        total_cost = delivery_cost+subtotal_cost;
                        binding.totalAmountCart.setText(total_cost.toString());

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        recyclerView.setAdapter(adapter);
    }
}