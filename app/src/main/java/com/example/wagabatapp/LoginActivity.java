package com.example.wagabatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Database;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wagabatapp.Models.RestaurantModel;
import com.example.wagabatapp.Models.UserModel;
import com.example.wagabatapp.Room.User;
import com.example.wagabatapp.Room.UserDao;
import com.example.wagabatapp.Room.UserRoomDatabase;
import com.example.wagabatapp.databinding.ActivityLoginBinding;
import com.example.wagabatapp.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegistrationScreen.class));

            }
        });
        binding.forgotPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgetPassword.class));
            }
        });

    }
    private void loginUser(){
//        String email = binding.loginEmailField.getText().toString();
//        String password = binding.loginPasswordField.getText().toString();
        TextView emailField = findViewById(R.id.login_email_field);
        String email = emailField.getText().toString();

        TextView passwordField = findViewById(R.id.login_password_field);
        String password = passwordField.getText().toString();

        if(TextUtils.isEmpty(email)){
            binding.loginEmailField.setError("Email cannot be empty");
        }
        else if(TextUtils.isEmpty(password)){
            binding.loginPasswordField.setError("Password cannot be empty");
        }
        else{
            SharedPreferences sh = getSharedPreferences("UserDetails", MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sh.edit();
            myEdit.putString("email",email);
            myEdit.apply();

            UserRoomDatabase userDatabase = UserRoomDatabase.getDatabase(getApplicationContext());
            UserDao userDao = userDatabase.userDao();
            userDao.deleteAll();
            Log.d("Gaberrr", email);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mAuth.signInWithEmailAndPassword(email,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        fetchUser();
                                        finish();
                                        Toast.makeText(LoginActivity.this,"Login successful!",Toast.LENGTH_SHORT).show();
                                        //finish();
                                        startActivity(new Intent(LoginActivity.this, RestaurantList.class));
                                    }
                                    else{
                                        Toast.makeText(LoginActivity.this,"Wrong Email or Password",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
            }).start();

        }

    }
    public void fetchUser(){
        UserRoomDatabase userDatabase = UserRoomDatabase.getDatabase(getApplicationContext());
        UserDao userDao = userDatabase.userDao();
        DatabaseReference databaseReference = FirebaseDatabase
                .getInstance("https://wagbaapp-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("users/"+mAuth.getUid());
        Log.d("Auth uid", mAuth.getUid());
        User roomUser = new User();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel user = snapshot.getValue(UserModel.class);
                roomUser.setName(user.getName());
                roomUser.setAddress(user.getAddress());
                roomUser.setEmail(user.getEmail());
                roomUser.setPhone(user.getPhone());
                roomUser.setUid(mAuth.getUid());
                userDao.insert(roomUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}