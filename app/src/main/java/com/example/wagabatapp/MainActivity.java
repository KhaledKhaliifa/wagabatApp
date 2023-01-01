package com.example.wagabatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth myAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myAuth = FirebaseAuth.getInstance();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(myAuth.getUid() != null){
                    Intent intent = new Intent(MainActivity.this, RestaurantList.class);
                    finish();
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    finish();
                    startActivity(intent);
                }

            }
        },2000);
    }
}