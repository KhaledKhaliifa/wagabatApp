package com.example.wagabatapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.wagabatapp.databinding.ActivityRestaurantItemExpandedBinding;
import com.example.wagabatapp.databinding.ActivityRestaurantMenuBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class RestaurantItemExpanded extends AppCompatActivity {
    ActivityRestaurantItemExpandedBinding binding;
    String restaurantPosition;
    String dishPosition;
    String dishName;
    DatabaseReference databaseReference;
    DatabaseReference dishReference;

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
        binding.addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dishReference = FirebaseDatabase.getInstance("https://wagbaapp-default-rtdb.europe-west1.firebasedatabase.app/")
                        .getReference("restaurants/restaurant"+restaurantPosition+"/dishes/dish"+dishPosition);
                databaseReference = FirebaseDatabase.getInstance("https://wagbaapp-default-rtdb.europe-west1.firebasedatabase.app/")
                        .getReference("/cart/"+restaurantPosition+dishPosition);
                Float itemPrice = Float.valueOf(binding.itemPriceExtended.getText().toString());
                Integer number_of_items =  Integer.valueOf(binding.itemCountExtended.getText().toString());

                dishReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        DishModel dish = snapshot.getValue(DishModel.class);
                        String available = dish.getAvailable();

                        if(Objects.equals(available, "1")){
                            dish.itemCount=number_of_items.toString();
                            dish.reference = restaurantPosition+dishPosition;
                            databaseReference.setValue(dish);
                        }
                        else{
                            Toast.makeText(RestaurantItemExpanded.this, "This item isn't available", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        dishName = "Missing Dish Name";
                        binding.dishNameExtended.setText(dishName);
                    }
                });
            }
        });
        binding.imageView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer number_of_items =  Integer.valueOf(binding.itemCountExtended.getText().toString());
                number_of_items +=1;
                Float itemPrice = Float.valueOf(binding.itemPriceExtended.getText().toString());
                Float total_cost = number_of_items * itemPrice;
                binding.totalItemCost.setText(total_cost.toString());
                binding.itemCountExtended.setText(number_of_items.toString());

            }
        });
        binding.imageView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer number_of_items =  Integer.valueOf(binding.itemCountExtended.getText().toString());
                if(number_of_items!=1){
                    number_of_items-=1;
                    binding.itemCountExtended.setText(number_of_items.toString());
                    Float itemPrice = Float.valueOf(binding.itemPriceExtended.getText().toString());
                    Float total_cost = number_of_items * itemPrice;
                    binding.totalItemCost.setText(total_cost.toString());
                }

            }
        });
    }
    public void applyDishName(){
        databaseReference = FirebaseDatabase.getInstance("https://wagbaapp-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("restaurants/restaurant"+restaurantPosition+"/dishes/dish"+dishPosition);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DishModel dish = snapshot.getValue(DishModel.class);

                Log.d("Gaber", dish.getName());
                binding.dishNameExtended.setText(dish.getName());
                if(!Objects.equals(dish.getDescription(), "")){
                    binding.dishDescriptionExtended.setText(dish.getDescription());
                }
                else{
                    binding.dishDescriptionExtended.setText(R.string.missingDescriptionString);
                }
                binding.itemPriceExtended.setText(dish.getPrice());
                binding.totalItemCost.setText(dish.getPrice());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dishName = "Missing Dish Name";
                binding.dishNameExtended.setText(dishName);
            }
        });


    }
}