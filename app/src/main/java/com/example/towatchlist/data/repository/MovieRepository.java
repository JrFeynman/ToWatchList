package com.example.towatchlist.data.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.towatchlist.data.db.AppDatabase;
import com.example.towatchlist.data.db.WatchlistDao;
import com.example.towatchlist.data.db.WatchlistMovie;
import com.example.towatchlist.data.model.Movie;
import com.example.towatchlist.data.model.MovieDetail;
import com.example.towatchlist.data.model.MovieResponse;
import com.example.towatchlist.data.network.ApiInterface;
import com.example.towatchlist.data.network.RetrofitClient;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {
    private final ApiInterface api;
    private final WatchlistDao dao;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public MovieRepository(Context context) {
        api = RetrofitClient.getInstance().create(ApiInterface.class);
        dao = AppDatabase.getInstance(context).watchlistDao();
    }

    public LiveData<List<Movie>> getMoviesByGenre(int genreId) {
        MutableLiveData<List<Movie>> live = new MutableLiveData<>();
        api.getMoviesByGenre(ApiInterface.API_KEY, genreId)
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> res) {
                        if (res.isSuccessful() && res.body()!=null)
                            live.postValue(res.body().getResults());
                    }
                    @Override public void onFailure(Call<MovieResponse> c, Throwable t) { live.postValue(null); }
                });
        return live;
    }

    public LiveData<MovieDetail> getMovieDetail(int id) {
        MutableLiveData<MovieDetail> live = new MutableLiveData<>();
        api.getMovieDetail(id, ApiInterface.API_KEY, "credits")
                .enqueue(new Callback<MovieDetail>() {
                    @Override
                    public void onResponse(Call<MovieDetail> c, Response<MovieDetail> r) {
                        if (r.isSuccessful() && r.body()!=null) live.postValue(r.body());
                    }
                    @Override public void onFailure(Call<MovieDetail> c, Throwable t) { live.postValue(null); }
                });
        return live;
    }

    public LiveData<List<Movie>> searchMovies(String query) {
        MutableLiveData<List<Movie>> live = new MutableLiveData<>();
        api.searchMovies(ApiInterface.API_KEY, query)
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> c, Response<MovieResponse> r) {
                        if (r.isSuccessful() && r.body()!=null) live.postValue(r.body().getResults());
                    }
                    @Override public void onFailure(Call<MovieResponse> c, Throwable t) { live.postValue(null); }
                });
        return live;
    }

    public void addToWatchlist(WatchlistMovie m) {
        executor.execute(() -> dao.addMovie(m));
    }
    public void removeFromWatchlist(WatchlistMovie m) {
        executor.execute(() -> dao.removeMovie(m));
    }
    public LiveData<List<WatchlistMovie>> getWatchlist() {
        return dao.getAll();
    }
    public void markWatched(int movieId, int userId, boolean watched) {
        executor.execute(() -> {
            dao.updateWatched(movieId, userId, watched);
        });

    }
}