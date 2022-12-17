package com.example.wagabatapp;

import static android.content.ContentValues.TAG;

import static com.example.wagabatapp.RestaurantAdapter.context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

import com.example.wagabatapp.databinding.ActivityLoginBinding;
import com.example.wagabatapp.databinding.ActivityRestaurantMenuBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RestaurantMenu extends AppCompatActivity {
    ActivityRestaurantMenuBinding binding;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    ArrayList<DishModel> list;

    String restaurantPosition;
    String restaurantName;

    LinearLayoutManager layoutManager;
    DishAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRestaurantMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        restaurantPosition = prefs.getString("restaurant","0");

        applyRestaurantName();

        recyclerView = findViewById(R.id.dishes_rv);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        list = new ArrayList<>();
        adapter = new DishAdapter(list);

        databaseReference = FirebaseDatabase.getInstance("https://wagbaapp-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("restaurants/restaurant"+restaurantPosition+"/dishes/");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    DishModel dish = dataSnapshot.getValue(DishModel.class);
                    list.add(dish);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
        recyclerView.setAdapter(adapter);

    }

    public void applyRestaurantName(){
        databaseReference = FirebaseDatabase.getInstance("https://wagbaapp-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("restaurants/restaurant"+restaurantPosition+"/name");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                restaurantName = snapshot.getValue().toString();
                Log.d("Gaber", restaurantName);
                binding.restaurantNameInDishes.setText(restaurantName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                restaurantName = "Missing Restaurant Name";
                binding.restaurantNameInDishes.setText(restaurantName);
            }
        });


    }
}