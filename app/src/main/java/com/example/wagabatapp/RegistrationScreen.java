package com.example.wagabatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wagabatapp.Models.UserModel;
import com.example.wagabatapp.Room.User;
import com.example.wagabatapp.Room.UserDao;
import com.example.wagabatapp.Room.UserRoomDatabase;
import com.example.wagabatapp.databinding.ActivityRegistrationScreenBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationScreen extends AppCompatActivity {

    ActivityRegistrationScreenBinding binding;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();


        progressDialog = new ProgressDialog(this);
        binding.registerBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                createUser();
            }
        });
    }

    private void createUser(){
        String name = binding.registerFullName.getText().toString();
        String email = binding.registerEmailField.getText().toString();
        String password = binding.registerPasswordField.getText().toString();
        String confirm_password = binding.registerConfirmPassword.getText().toString();
        String address = binding.registerAddress.getText().toString();
        String number = binding.registerPhoneNumber.getText().toString();

        UserModel user= new UserModel();

        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(password) || TextUtils.isEmpty(confirm_password) ||
                TextUtils.isEmpty(address) || TextUtils.isEmpty(number)){
            Toast.makeText(RegistrationScreen.this,"Please make sure to fill out all boxes!", Toast.LENGTH_SHORT).show();
        }
        else if(!password.equals(confirm_password)){
            Toast.makeText(RegistrationScreen.this,"Passwords don't match", Toast.LENGTH_SHORT).show();
        }
        else{

            progressDialog.setMessage("Registering user...");
            progressDialog.show();
            user.setName(name);
            user.setEmail(email);
            user.setAddress(address);
            user.setPhone(number);

            firebaseAuth.createUserWithEmailAndPassword(email,password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            DatabaseReference databaseReference = FirebaseDatabase
                                    .getInstance("https://wagbaapp-default-rtdb.europe-west1.firebasedatabase.app/")
                                    .getReference("users/"+firebaseAuth.getUid());
                            databaseReference.setValue(user);
                            progressDialog.cancel();
                            Toast.makeText(RegistrationScreen.this,"User registered successfully!", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(RegistrationScreen.this,LoginActivity.class));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.cancel();
                            Toast toast = Toast.makeText(RegistrationScreen.this,e.getMessage(),Toast.LENGTH_SHORT);
                            TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                            if(v != null) v.setGravity(Gravity.CENTER);
                            toast.show();

                        }
                    });
        }

    }
}