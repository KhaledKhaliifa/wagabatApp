package com.example.wagabatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.wagabatapp.databinding.ActivityCheckoutPageBinding;

public class CheckoutPage extends AppCompatActivity {
    ActivityCheckoutPageBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckoutPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.withinTimeCheckout.setText("Within "+getIntent().getStringExtra("time"));
        binding.subtotalAmount2.setText(getIntent().getStringExtra("subtotal"));
        binding.deliveryFeeCheckout.setText(getIntent().getStringExtra("delivery"));
        binding.totalAmountCheckout.setText(getIntent().getStringExtra("total"));
    }
}