package com.example.towatchlist.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.towatchlist.data.model.Movie;
import com.example.towatchlist.data.repository.MovieRepository;
import java.util.List;

public class SearchViewModel extends AndroidViewModel {
    private final MovieRepository repo;
    private LiveData<List<Movie>> results;

    public SearchViewModel(@NonNull Application app) {
        super(app);
        repo = new MovieRepository(app);
    }
    public void search(String q) { results = repo.searchMovies(q); }
    public LiveData<List<Movie>> getResults() { return results; }
}