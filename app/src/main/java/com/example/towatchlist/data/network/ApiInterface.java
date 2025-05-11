package com.example.towatchlist.data.network;

import com.example.towatchlist.data.model.MovieResponse;
import com.example.towatchlist.data.model.MovieDetail;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    String API_KEY = "YOUR_API_KEY";

    @GET("discover/movie")
    Call<MovieResponse> getMoviesByGenre(
            @Query("api_key") String apiKey,
            @Query("with_genres") int genreId
    );

    @GET("movie/{id}")
    Call<MovieDetail> getMovieDetail(
            @Path("id") int id,
            @Query("api_key") String apiKey,
            @Query("append_to_response") String appendToResponse  // -> "credits"
    );
    @GET("search/movie")
    Call<MovieResponse> searchMovies(
            @Query("api_key") String apiKey,
            @Query("query") String query
    );
}