package com.example.towatchlist.data.model;

import com.google.gson.annotations.SerializedName;

public class MovieDetail {
    @SerializedName("id")            private int id;
    @SerializedName("title")         private String title;
    @SerializedName("overview")      private String overview;
    @SerializedName("poster_path")   private String posterPath;
    @SerializedName("release_date")  private String releaseDate;
    @SerializedName("vote_average")  private double voteAverage;
    @SerializedName("credits")       private Credits credits;

    // getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getOverview() { return overview; }
    public String getPosterPath() { return posterPath; }
    public String getReleaseDate() { return releaseDate; }
    public double getVoteAverage() { return voteAverage; }
    public Credits getCredits() { return credits; }
}
