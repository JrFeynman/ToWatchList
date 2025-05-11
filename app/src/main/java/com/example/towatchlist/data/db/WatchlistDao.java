package com.example.towatchlist.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface WatchlistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addMovie(WatchlistMovie movie);

    @Delete
    void removeMovie(WatchlistMovie movie);

    @Query("SELECT * FROM watchlistmovie")
    LiveData<List<WatchlistMovie>> getAll();
    @Query("SELECT * FROM watchlistmovie WHERE userId = :uid")
    LiveData<List<WatchlistMovie>> getForUser(int uid);
    @Query("UPDATE watchlistmovie SET watched = :watched WHERE id = :movieId AND userId = :uid")
    void updateWatched(int movieId, int uid, boolean watched);

}