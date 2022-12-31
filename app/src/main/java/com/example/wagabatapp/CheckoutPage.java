package com.example.wagabatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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

                }

            }
        });

    }
}