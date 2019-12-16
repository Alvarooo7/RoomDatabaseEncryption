package com.example.roomdbencryption.database;

import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(UserEntity user);
}
