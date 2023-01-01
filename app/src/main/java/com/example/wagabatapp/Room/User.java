package com.example.wagabatapp.Room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_table")
public class User {
    @PrimaryKey(autoGenerate = true)
    Integer id;
    @NonNull
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name ="email")
    private String email;
    @ColumnInfo(name = "phone")
    private String phone;
    @ColumnInfo(name = "address")
    private String address;
    @ColumnInfo(name = "uid")
    private String uid;

    public User(@NonNull String name, String email, String phone, String address, String uid) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.uid = uid;
    }

    public User() {

    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getId() {
        return id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
