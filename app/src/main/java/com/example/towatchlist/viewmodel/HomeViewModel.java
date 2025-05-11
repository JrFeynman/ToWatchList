package com.example.towatchlist.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.towatchlist.data.model.Movie;
import com.example.towatchlist.data.repository.MovieRepository;
import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    private final MovieRepository repo;
    private LiveData<List<Movie>> movies;

    public HomeViewModel(@NonNull Application app) {
        super(app);
        repo = new MovieRepository(app);
    }

    public void fetchMovies(int genreId) {
        movies = repo.getMoviesByGenre(genreId);
    }
    public LiveData<List<Movie>> getMovies() { return movies; }
}