package com.example.towatchlist.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import com.example.towatchlist.data.model.Movie;
import com.example.towatchlist.data.repository.MovieRepository;
import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    private final MovieRepository repo;
    private final MutableLiveData<Integer> selectedGenre = new MutableLiveData<>();
    private final LiveData<List<Movie>> movies;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        // ➊ önce repo'yu oluştur
        repo = new MovieRepository(application);
        // ➋ sonra repo kullanarak switchMap ile movies LiveData'sını başlat
        movies = Transformations.switchMap(
                selectedGenre,
                genreId -> repo.getMoviesByGenre(genreId)
        );
    }

    /** Kullanıcı tür seçtiğinde çağrılır */
    public void setGenre(int genreId) {
        selectedGenre.setValue(genreId);
    }

    /** Fragment burayı observe eder */
    public LiveData<List<Movie>> getMovies() {
        return movies;
    }
}
