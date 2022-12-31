package com.example.wagabatapp.Room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_table")
public class User {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "user")
    private String user;

    public User(@NonNull String user) {this.user = user;}

    public String getUser(){return this.user;}
}
