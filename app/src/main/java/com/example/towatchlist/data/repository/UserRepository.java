package com.example.towatchlist.data.repository;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import androidx.lifecycle.LiveData;
import com.example.towatchlist.data.db.AppDatabase;
import com.example.towatchlist.data.db.User;
import com.example.towatchlist.data.db.WatchlistMovie;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepository {
    private final Context ctx;
    private final AppDatabase db;

    // ↓ Arka plan thread & UI handler
    private final ExecutorService executor;
    private final Handler mainHandler;

    public UserRepository(Context context) {
        this.ctx = context.getApplicationContext();
        this.db  = AppDatabase.getInstance(ctx);

        // Executor + Handler kurulumu
        this.executor    = Executors.newSingleThreadExecutor();
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    /** Senkron register (aynı metotu koruduk) */
    public long register(String username, String password) {
        User u = new User(username, password);
        return db.userDao().insert(u);
    }

    /** Senkron login (aynı metotu koruduk) */
    public User login(String username, String password) {
        return db.userDao().login(username, password);
    }

    /** Kullanıcının watchlist’ini getir */
    public LiveData<List<WatchlistMovie>> getWatchlistForUser(int userId) {
        return db.watchlistDao().getForUser(userId);
    }

    /** Watchlist’ten sil */
    public void removeFromWatchlist(WatchlistMovie movie) {
        db.watchlistDao().removeMovie(movie);
    }

    // ----- Aşağısı yenilenen asenkron metotlar -----

    /**
     * Arka planda register et, sonucu UI thread’ine post et.
     * @param username   Yeni kullanıcı adı
     * @param password   Şifre
     * @param callback   Sonucu almak için AuthCallback
     */
    public void registerAsync(String username, String password, AuthCallback callback) {
        executor.execute(() -> {
            long id = db.userDao().insert(new User(username, password));
            User u = null;
            if (id > 0) {
                // Kayıt başarılıysa aynı anda login bilgisine de ulaşalım
                u = db.userDao().login(username, password);
            }
            User finalU = u;
            mainHandler.post(() -> callback.onComplete(finalU, true));
        });
    }

    /**
     * Arka planda login dene, sonucu UI thread’ine post et.
     * @param username   Kullanıcı adı
     * @param password   Şifre
     * @param callback   Sonucu almak için AuthCallback
     */
    public void loginAsync(String username, String password, AuthCallback callback) {
        executor.execute(() -> {
            User u = db.userDao().login(username, password);
            mainHandler.post(() -> callback.onComplete(u, false));
        });
    }
}
