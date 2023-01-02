package com.example.wagabatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Database;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.wagabatapp.Models.DishModel;
import com.example.wagabatapp.Models.OrderModel;
import com.example.wagabatapp.Models.RestaurantModel;
import com.example.wagabatapp.databinding.ActivityCheckoutPageBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CheckoutPage extends AppCompatActivity {
    ActivityCheckoutPageBinding binding;
    DatabaseReference databaseReference;
    DatabaseReference orderHistoryReference;
    DatabaseReference restaurantReference;
    FirebaseAuth mAuth;
    String restaurant_name;
    String gateNum;
    static Integer counter;
    OrderModel order;
    List<DishModel> dish_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckoutPageBinding.inflate(getLayoutInflater());
        mAuth = FirebaseAuth.getInstance();
        dish_list = new ArrayList<>();
        String uid = mAuth.getUid();
        String subtotal_price = getIntent().getStringExtra("subtotal");
        String delivery_fee = getIntent().getStringExtra("delivery");
        String time_taken = getIntent().getStringExtra("time");
        String total_amount = getIntent().getStringExtra("total");

        setContentView(binding.getRoot());
        binding.withinTimeCheckout.setText("Within "+ time_taken);
        binding.subtotalAmount2.setText(subtotal_price);
        binding.deliveryFeeCheckout.setText(delivery_fee);
        binding.totalAmountCheckout.setText(total_amount);

        binding.placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.radioGroup.getCheckedRadioButtonId()==-1){
                    Toast.makeText(CheckoutPage.this, "You must select a payment method!", Toast.LENGTH_SHORT).show();
                }
                else if(binding.radioButton1.isChecked()){
                    Toast.makeText(CheckoutPage.this, "Payment by credit card is unavailable", Toast.LENGTH_SHORT).show();
                }
                else if(binding.radioGroup2.getCheckedRadioButtonId()==-1){
                    Toast.makeText(CheckoutPage.this, "You must select a gate!", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(binding.radioButtonGate3.isChecked()){
                        gateNum = "Gate 3";
                    }
                    else{
                        gateNum = "Gate 4";
                    }
                    Long time = System.currentTimeMillis();
                    Date d=new Date();
                    counter = 0;
                    databaseReference = FirebaseDatabase.getInstance("https://wagbaapp-default-rtdb.europe-west1.firebasedatabase.app/")
                            .getReference("users/"+uid+"/cart");
                    orderHistoryReference = FirebaseDatabase.getInstance("https://wagbaapp-default-rtdb.europe-west1.firebasedatabase.app/")
                            .getReference("users/"+uid+"/orders/");
                    orderHistoryReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            while (snapshot.hasChild(counter.toString())) {
                                counter++;
                            }
                            orderHistoryReference = FirebaseDatabase.getInstance("https://wagbaapp-default-rtdb.europe-west1.firebasedatabase.app/")
                                    .getReference("users/"+uid+"/orders/"+counter.toString());
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String restaurant_id = new String();
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        DishModel dish = dataSnapshot.getValue(DishModel.class);
                                        restaurant_id = String.valueOf(dish.getReference().charAt(0));
                                        dish_list.add(dish);
                                    }
                                    restaurantReference = FirebaseDatabase.getInstance("https://wagbaapp-default-rtdb.europe-west1.firebasedatabase.app/")
                                            .getReference("restaurants/restaurant" + restaurant_id);
                                    restaurantReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            order = new OrderModel();
                                            RestaurantModel restaurantModel = snapshot.getValue(RestaurantModel.class);
                                            restaurant_name = restaurantModel.getName();
                                            order.setHour_minute(String.valueOf(d.getHours())+":"+String.valueOf(d.getMinutes()));
                                            order.setTotal_price(total_amount);
                                            order.setDelivery_fee(delivery_fee);
                                            order.setSubtotal_price(subtotal_price);
                                            order.setDishList(dish_list);
                                            order.setOrder_reference(String.valueOf(counter));
                                            order.setDate(time);
                                            order.setStatus("Unconfirmed");
                                            order.setRestaurant_name(restaurant_name);
                                            order.setUserAuthId(mAuth.getUid());
                                            order.setGate(gateNum);

                                            orderHistoryReference.setValue(order);
                                            databaseReference.removeValue();
                                            finish();
                                            Intent intent = new Intent(CheckoutPage.this,RestaurantList.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                        }
                                    });
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }
        });
    }
}