package com.example.towatchlist.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.towatchlist.data.db.WatchlistMovie;
import com.example.towatchlist.data.repository.MovieRepository;
import com.example.towatchlist.data.repository.UserRepository;
import java.util.List;

public class WatchlistViewModel extends AndroidViewModel {
    private final MovieRepository movieRepo;
    private final UserRepository userRepo;
    private LiveData<List<WatchlistMovie>> list;

    public WatchlistViewModel(@NonNull Application application) {
        super(application);
        // repo’ları uygulama context’iyle kendi içinde başlatıyoruz
        movieRepo = new MovieRepository(application);
        userRepo  = new UserRepository(application);
    }

    /** Giriş yapan kullanıcının ID'si ile listeyi yükle */
    public void fetchForUser(int userId) {
        list = userRepo.getWatchlistForUser(userId);
    }

    /** Fragment’ten observe etmek için expose et */
    public LiveData<List<WatchlistMovie>> getList() {
        return list;
    }

    /** Silme işlemini MovieRepository’ye devret */
    public void removeFromWatchlist(WatchlistMovie m) {
        movieRepo.removeFromWatchlist(m);
    }

    /** “Watched” durumunu güncelle: MovieRepository’ye delege et */
    public void markWatched(int movieId, int userId, boolean watched) {
        movieRepo.markWatched(movieId, userId, watched);
    }
}
