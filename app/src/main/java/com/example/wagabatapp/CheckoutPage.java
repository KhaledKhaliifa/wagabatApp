package com.example.wagabatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Database;

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
    FirebaseAuth mAuth;
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
                else{
                    Long time = System.currentTimeMillis();
                    databaseReference = FirebaseDatabase.getInstance("https://wagbaapp-default-rtdb.europe-west1.firebasedatabase.app/")
                            .getReference("users/"+uid+"/cart");
                    orderHistoryReference = FirebaseDatabase.getInstance("https://wagbaapp-default-rtdb.europe-west1.firebasedatabase.app/")
                            .getReference("users/"+uid+"/orders/"+time);
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                                DishModel dish = dataSnapshot.getValue(DishModel.class);
                                dish_list.add(dish);
                            }
                            OrderModel order = new OrderModel();
                            order.setTotal_price(total_amount);
                            order.setDelivery_fee(delivery_fee);
                            order.setSubtotal_price(subtotal_price);
                            order.setDishList(dish_list);
                            order.setDate(time);
                            orderHistoryReference.setValue(order);
                            databaseReference.removeValue();
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