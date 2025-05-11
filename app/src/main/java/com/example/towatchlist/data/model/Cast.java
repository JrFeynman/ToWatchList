package com.example.towatchlist.data.model;

import com.google.gson.annotations.SerializedName;

public class Cast {
    @SerializedName("name")      private String name;
    @SerializedName("character") private String character;

    public String getName() { return name; }
    public String getCharacter() { return character; }
}