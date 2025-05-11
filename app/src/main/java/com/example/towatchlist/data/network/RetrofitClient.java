package com.example.towatchlist.data.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import com.example.towatchlist.util.Constants;

public class RetrofitClient {
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static Retrofit instance;

    public static Retrofit getInstance() {
        if (instance == null) {
            // OkHttpClient ile interceptor oluştur
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request original = chain.request();
                            Request reqWithAuth = original.newBuilder()
                                    .header("Authorization", "Bearer " + Constants.TMDB_ACCESS_TOKEN)
                                    .build();
                            return chain.proceed(reqWithAuth);
                        }
                    })
                    .build();

            instance = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)                              // buraya ekledik
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return instance;
    }
}