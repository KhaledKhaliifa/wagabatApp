package com.example.wagabatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.wagabatapp.Adapters.CartAdapter;
import com.example.wagabatapp.Models.DishModel;
import com.example.wagabatapp.Models.RestaurantModel;
import com.example.wagabatapp.Room.User;
import com.example.wagabatapp.Room.UserDao;
import com.example.wagabatapp.Room.UserRoomDatabase;
import com.example.wagabatapp.databinding.ActivityCartPageBinding;
import com.google.firebase.auth.FirebaseAuth;
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
    String uid;

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
        UserRoomDatabase userDatabase = UserRoomDatabase.getDatabase(getApplicationContext());
        UserDao userDao = userDatabase.userDao();

        new Thread(new Runnable() {
            @Override
            public void run() {
                uid = FirebaseAuth.getInstance().getUid();
                Log.d("Gaberr", uid.toString());
                databaseReference = FirebaseDatabase.getInstance("https://wagbaapp-default-rtdb.europe-west1.firebasedatabase.app/")
                        .getReference("users/"+uid);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild("cart")){
                            databaseReference = FirebaseDatabase.getInstance("https://wagbaapp-default-rtdb.europe-west1.firebasedatabase.app/")
                                    .getReference("users/"+uid+"/cart");
                            databaseReference.addValueEventListener(new ValueEventListener() {

                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (list != null) {
                                        list.removeAll(list);
                                        Log.d("Gaberrr", "List not null");
                                        subtotal_cost = Float.valueOf(0);
                                        delivery_cost = Float.valueOf(0);
                                    }

                                    DishModel dish = new DishModel();
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        dish = dataSnapshot.getValue(DishModel.class);
                                        restaurantID = String.valueOf(dish.getReference().charAt(0));
                                        list.add(dish);
                                        subtotal_cost += Float.valueOf(dish.getPrice()) * Float.valueOf(dish.getItemCount());
                                    }

                                    restaurantReference = FirebaseDatabase.getInstance("https://wagbaapp-default-rtdb.europe-west1.firebasedatabase.app/")
                                            .getReference("restaurants/restaurant" + restaurantID);
                                    restaurantReference.addListenerForSingleValueEvent(new ValueEventListener() {

                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            RestaurantModel restaurant = snapshot.getValue(RestaurantModel.class);

                                            binding.deliveryTimeHidden.setText(restaurant.getDelivery_time());
                                            delivery_cost = Float.valueOf(restaurant.getDelivery_fee());
                                            if(subtotal_cost == 0.0){
                                                delivery_cost = Float.valueOf(0);
                                                binding.yourCartTextView.setText("Your cart is empty!");
                                            }
                                            binding.subtotalAmount.setText(subtotal_cost.toString());
                                            binding.deliveryFeeAmount.setText(delivery_cost.toString());
                                            total_cost = delivery_cost + subtotal_cost;
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
                        }
                        else{
                            binding.yourCartTextView.setText("Your cart is empty!");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }).start();
        binding.checkoutBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                TextView c = findViewById(R.id.subtotalAmount);
                subtotal_cost = Float.valueOf(c.getText().toString());
                TextView x = findViewById(R.id.totalAmountCart);
                total_cost = Float.valueOf(x.getText().toString());
                TextView y = findViewById(R.id.deliveryFeeAmount);
                delivery_cost = Float.valueOf(y.getText().toString());
                TextView z = findViewById(R.id.deliveryTimeHidden);
                String delivery_time = z.getText().toString();

                Intent intent = new Intent(CartPage.this, CheckoutPage.class);
                intent.putExtra("subtotal",subtotal_cost.toString());
                intent.putExtra("total",total_cost.toString());
                intent.putExtra("delivery",delivery_cost.toString());
                intent.putExtra("time",delivery_time);
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(adapter);
    }
}