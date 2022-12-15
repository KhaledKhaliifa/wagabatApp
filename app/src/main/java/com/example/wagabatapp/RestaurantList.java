package com.example.wagabatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RestaurantList extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<RestaurantModel> list;

    DatabaseReference databaseReference;
    RestaurantAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView = findViewById(R.id.restaurant_list_rv);
        list.add(new RestaurantModel("Papa John's","Italian","5 min","EGP 15.00","4.8"));

        adapter = new RestaurantAdapter(list);

        recyclerView.setAdapter(adapter);

    }
}