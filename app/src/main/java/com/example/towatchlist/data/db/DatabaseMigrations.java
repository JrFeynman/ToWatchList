package com.example.towatchlist.data.db;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class DatabaseMigrations {

    /** 1 → 2: users tablosu oluştur ve watchlistmovie’ye userId ekle */
    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override public void migrate(@NonNull SupportSQLiteDatabase db) {
            db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `users` (" +
                            "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "`username` TEXT, " +
                            "`password` TEXT" +
                            ")"
            );
            db.execSQL(
                    "ALTER TABLE `watchlistmovie` " +
                            "ADD COLUMN `userId` INTEGER NOT NULL DEFAULT 0"
            );
        }
    };

    /** 2 → 3: watchlistmovie’ye releaseDate ve voteAverage sütunlarını ekle */
    public static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override public void migrate(@NonNull SupportSQLiteDatabase db) {
            db.execSQL(
                    "ALTER TABLE `watchlistmovie` " +
                            "ADD COLUMN `releaseDate` TEXT NOT NULL DEFAULT ''"
            );
            db.execSQL(
                    "ALTER TABLE `watchlistmovie` " +
                            "ADD COLUMN `voteAverage` REAL NOT NULL DEFAULT 0"
            );
        }
    };

    /** 3 → 4: watchlistmovie’ye watched sütununu ekle */
    public static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override public void migrate(@NonNull SupportSQLiteDatabase db) {
            // INTEGER 0 = false, 1 = true
            db.execSQL(
                    "ALTER TABLE `watchlistmovie` " +
                            "ADD COLUMN `watched` INTEGER NOT NULL DEFAULT 0"
            );
        }
    };
}
