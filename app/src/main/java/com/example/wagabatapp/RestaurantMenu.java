package com.example.wagabatapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

public class RestaurantMenu extends AppCompatActivity {
    ActivityRestaurantMenuBinding binding;
    DatabaseReference databaseReference;
    String restaurantPosition;
    String restaurantName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRestaurantMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        restaurantPosition = getIntent().getStringExtra("position");
        applyRestaurantName();
        databaseReference = FirebaseDatabase.getInstance("https://wagbaapp-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("restaurants/restaurant"+restaurantPosition+"/");
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