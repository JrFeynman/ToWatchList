package com.example.towatchlist.data.db;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * Uygulamanın Room veritabanı tanımı.
 * version = 3 yapılmalı, çünkü iki kademeli migration (1→2, 2→3) var.
 */
@Database(
        entities = { User.class, WatchlistMovie.class },
        version = 4,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DB_NAME = "app_db";
    private static AppDatabase instance;

    public abstract UserDao userDao();
    public abstract WatchlistDao watchlistDao();

    public static synchronized AppDatabase getInstance(Context ctx) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            ctx.getApplicationContext(),
                            AppDatabase.class,
                            DB_NAME
                    )
                    // 1→2 ve 2→3 migration'larını ekliyoruz
                    .addMigrations(
                            DatabaseMigrations.MIGRATION_1_2,
                            DatabaseMigrations.MIGRATION_2_3,
                            DatabaseMigrations.MIGRATION_3_4
                    )
                    // geliştirme sürecinde eğer veritabanınızı resetlemek istersen
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
