package com.example.wagabatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.wagabatapp.databinding.ActivityRestaurantItemExpandedBinding;
import com.example.wagabatapp.databinding.ActivityRestaurantMenuBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RestaurantItemExpanded extends AppCompatActivity {
    ActivityRestaurantItemExpandedBinding binding;
    String restaurantPosition;
    String dishPosition;
    String dishName;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_restaurant_item_expanded);

        binding = ActivityRestaurantItemExpandedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        restaurantPosition = prefs.getString("restaurant","0");
        dishPosition = prefs.getString("dish","0");
        applyDishName();
        databaseReference = FirebaseDatabase.getInstance("https://wagbaapp-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("restaurants/restaurant"+restaurantPosition+"/dishes/dish"+dishPosition);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    //RestaurantModel restaurant = dataSnapshot.getValue(RestaurantModel.class);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void applyDishName(){
        databaseReference = FirebaseDatabase.getInstance("https://wagbaapp-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("restaurants/restaurant"+restaurantPosition+"/dishes/dish"+dishPosition+"/name");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dishName = snapshot.getValue().toString();
                Log.d("Gaber", dishName);
                binding.dishNameExtended.setText(dishName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dishName = "Missing Dish Name";
                binding.dishNameExtended.setText(dishName);
            }
        });


    }
}