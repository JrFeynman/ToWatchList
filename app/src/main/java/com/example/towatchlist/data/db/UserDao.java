package com.example.towatchlist.data.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.OnConflictStrategy;
import androidx.lifecycle.LiveData;
import java.util.List;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(User user); // dönen uzun id

    @Query("SELECT * FROM users WHERE username = :u AND password = :p LIMIT 1")
    User login(String u, String p);
}
