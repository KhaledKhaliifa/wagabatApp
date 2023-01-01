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

    @Query("SELECT * FROM user_table ORDER BY name ASC")
    LiveData<List<User>> getAllUsers();

    @Query("Select * From user_table where uid=(:uid)")
    User getUser(String uid);

}
