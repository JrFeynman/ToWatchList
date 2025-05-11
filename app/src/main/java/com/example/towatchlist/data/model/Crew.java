package com.example.towatchlist.data.model;

import com.google.gson.annotations.SerializedName;

public class Crew {
    @SerializedName("id")
    private int id;

    @SerializedName("job")
    private String job;

    @SerializedName("name")
    private String name;

    // getters
    public int getId() { return id; }
    public String getJob() { return job; }
    public String getName() { return name; }
}
