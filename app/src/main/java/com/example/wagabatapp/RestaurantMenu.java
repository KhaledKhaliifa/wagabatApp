package com.example.wagabatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.wagabatapp.Adapters.DishAdapter;
import com.example.wagabatapp.Models.DishModel;
import com.example.wagabatapp.Room.User;
import com.example.wagabatapp.Room.UserDao;
import com.example.wagabatapp.Room.UserRoomDatabase;
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
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRestaurantMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        restaurantPosition = prefs.getString("restaurant","0");
        binding.shoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RestaurantMenu.this, CartPage.class);
                startActivity(intent);
            }
        });
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

    @Override
    public void onBackPressed() {
        Context context = this;
        UserRoomDatabase userDatabase = UserRoomDatabase.getDatabase(getApplicationContext());
        UserDao userDao = userDatabase.userDao();
        SharedPreferences sh = getSharedPreferences("UserDetails", MODE_PRIVATE);
        String email = sh.getString("email","");
        mAuth = FirebaseAuth.getInstance();

        new Thread(new Runnable() {
            @Override
            public void run() {
                databaseReference = FirebaseDatabase.getInstance("https://wagbaapp-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users/"+mAuth.getUid());
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild("cart")){
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);

                            builder.setCancelable(false)
                                    .setMessage("Are you sure? Going back clears your cart")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener(){

                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Toast.makeText(RestaurantMenu.this,"Cart Cleared", Toast.LENGTH_SHORT).show();
                                            clearCart(mAuth.getUid());
                                            RestaurantMenu.super.onBackPressed();
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener(){
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    }).show();
                        }
                        else{
                            RestaurantMenu.super.onBackPressed();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }).start();
    }
    public void clearCart(String uid){
        databaseReference = FirebaseDatabase.getInstance("https://wagbaapp-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("users/"+mAuth.getUid());
        databaseReference.child("cart").removeValue();
    }
}
