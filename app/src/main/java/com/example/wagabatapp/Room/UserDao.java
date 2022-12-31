package com.example.wagabatapp.Room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);

    @Query("DELETE FROM user_table")
    void deleteAll();

    @Query("SELECT * FROM user_table ORDER BY user ASC")
    LiveData<List<User>> getAllUsers();

    @Query("SELECT user FROM user_table WHERE user == :x")
    LiveData<User> getUser(String x);
}
