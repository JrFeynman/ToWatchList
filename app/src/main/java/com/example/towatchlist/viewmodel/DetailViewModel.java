package com.example.towatchlist.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.towatchlist.data.model.MovieDetail;
import com.example.towatchlist.data.db.WatchlistMovie;
import com.example.towatchlist.data.repository.MovieRepository;
import java.util.List;

public class DetailViewModel extends AndroidViewModel {
    private final MovieRepository repo;
    private LiveData<MovieDetail> detail;
    private LiveData<List<WatchlistMovie>> watchlist;

    public DetailViewModel(@NonNull Application app) {
        super(app);
        repo = new MovieRepository(app);
        watchlist = repo.getWatchlist();
    }
    public void fetchDetail(int id) { detail = repo.getMovieDetail(id); }
    public LiveData<MovieDetail> getDetail() { return detail; }

    public LiveData<List<WatchlistMovie>> getWatchlist() { return watchlist; }
    public void addToWatchlist(WatchlistMovie m) { repo.addToWatchlist(m); }
    public void removeFromWatchlist(WatchlistMovie m) { repo.removeFromWatchlist(m); }
}