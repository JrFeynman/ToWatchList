package com.example.towatchlist.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Credits {
    @SerializedName("cast")
    private List<Cast> cast;
    @SerializedName("crew")
    private List<Crew> crew;

    public List<Cast> getCast() { return cast; }
    public List<Crew> getCrew() { return crew; }

}