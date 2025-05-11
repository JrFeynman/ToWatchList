package com.example.towatchlist.data.repository;

import com.example.towatchlist.data.db.User;

public interface AuthCallback {
    /**
     * @param user  Giriş başarılıysa User objesi, başarısızsa null
     * @param isRegister  true = register sonucu, false = login sonucu
     */
    void onComplete(User user, boolean isRegister);
}
