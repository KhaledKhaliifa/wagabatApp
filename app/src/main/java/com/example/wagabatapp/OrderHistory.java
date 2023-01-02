package com.example.wagabatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.wagabatapp.Adapters.OrderAdapter;
import com.example.wagabatapp.Adapters.RestaurantAdapter;
import com.example.wagabatapp.Models.DishModel;
import com.example.wagabatapp.Models.OrderModel;
import com.example.wagabatapp.Models.RestaurantModel;
import com.example.wagabatapp.databinding.ActivityLoginBinding;
import com.example.wagabatapp.databinding.ActivityOrderHistoryBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class OrderHistory extends AppCompatActivity {
    ActivityOrderHistoryBinding binding;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    ArrayList<OrderModel> orderList;
    OrderAdapter orderAdapter;
    ArrayList<DishModel> dishList;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recyclerView = findViewById(R.id.recycler_view_history_activity);
        orderList = new ArrayList<>();
        orderAdapter = new OrderAdapter(orderList);
        mAuth = FirebaseAuth.getInstance();
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        databaseReference = FirebaseDatabase.getInstance("https://wagbaapp-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("users/"+mAuth.getUid()+"/orders/");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("0")){
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                                OrderModel order = dataSnapshot.getValue(OrderModel.class);
                                orderList.add(order);
                            }
                            orderAdapter.notifyDataSetChanged();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    recyclerView.setAdapter(orderAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
