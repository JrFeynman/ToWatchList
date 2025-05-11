package com.example.towatchlist.data.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "watchlistmovie")
public class WatchlistMovie {
    @PrimaryKey
    private int id;
    private String title;
    private String posterPath;
    private String releaseDate;
    private double voteAverage;
    private int userId;
    private boolean watched;

    public WatchlistMovie(int id,
                          String title,
                          String posterPath,
                          String releaseDate,
                          double voteAverage,
                          int userId) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.userId = userId;
        this.watched=false;
    }
    public int getId()               { return id; }
    public String getTitle()         { return title; }
    public String getPosterPath()    { return posterPath; }
    public String getReleaseDate()   { return releaseDate; }
    public double getVoteAverage()   { return voteAverage; }
    public int getUserId()           { return userId; }
    public boolean isWatched()       { return watched; }
    public void setWatched(boolean watched) {
        this.watched = watched;
    }

}