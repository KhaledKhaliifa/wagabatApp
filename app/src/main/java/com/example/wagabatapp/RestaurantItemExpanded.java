package com.example.wagabatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wagabatapp.Models.DishModel;
import com.example.wagabatapp.Room.User;
import com.example.wagabatapp.Room.UserDao;
import com.example.wagabatapp.Room.UserRoomDatabase;
import com.example.wagabatapp.databinding.ActivityRestaurantItemExpandedBinding;
import com.google.firebase.auth.FirebaseAuth;
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
    static Context context;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getBaseContext();

        setContentView(R.layout.activity_restaurant_item_expanded);

        binding = ActivityRestaurantItemExpandedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        restaurantPosition = prefs.getString("restaurant","0");
        dishPosition = prefs.getString("dish","0");
        uid = FirebaseAuth.getInstance().getUid();

        UserRoomDatabase userDatabase = UserRoomDatabase.getDatabase(getApplicationContext());
        UserDao userDao = userDatabase.userDao();

        new Thread(new Runnable() {
            @Override
            public void run() {
                User user = userDao.getUser(uid);
            }
        }).start();

        applyDishName();
        binding.addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dishReference = FirebaseDatabase.getInstance("https://wagbaapp-default-rtdb.europe-west1.firebasedatabase.app/")
                        .getReference("restaurants/restaurant"+restaurantPosition+"/dishes/dish"+dishPosition);
                databaseReference = FirebaseDatabase.getInstance("https://wagbaapp-default-rtdb.europe-west1.firebasedatabase.app/")
                        .getReference("users/"+uid.toString()+"/cart/"+restaurantPosition+dishPosition);
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
                            Toast.makeText(RestaurantItemExpanded.this, "Added to cart!", Toast.LENGTH_LONG).show();

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
                Glide.with(context).load(dish.getImageLink()).into(binding.restaurantImage);

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