package com.example.wanderfunmobile.infrastructure.api.backend.di;

import android.content.Context;

import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.infrastructure.api.backend.AlbumApi;
import com.example.wanderfunmobile.infrastructure.api.backend.AuthApi;
import com.example.wanderfunmobile.infrastructure.api.backend.CloudinaryApi;
import com.example.wanderfunmobile.infrastructure.api.backend.LeaderboardApi;
import com.example.wanderfunmobile.infrastructure.api.backend.PlaceApi;
import com.example.wanderfunmobile.infrastructure.api.backend.TripApi;
import com.example.wanderfunmobile.infrastructure.api.backend.UserApi;
import com.example.wanderfunmobile.infrastructure.util.DateDeserializer;
import com.example.wanderfunmobile.infrastructure.util.LocalTimeDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class BackendApiModule {
    @Provides
    @Singleton
    public AuthApi provideAuthApi(@ApplicationContext Context context) {
        String baseUrl = context.getString(R.string.base_url);
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build())
                .build()
                .create(AuthApi.class);
    }

    @Provides
    @Singleton
    public AlbumApi provideAlbumApi(@ApplicationContext Context context) {
        String baseUrl = context.getString(R.string.base_url);
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build())
                .build()
                .create(AlbumApi.class);
    }

    @Provides
    @Singleton
    public CloudinaryApi provideCloudinaryApi(@ApplicationContext Context context) {
        String baseUrl = context.getString(R.string.base_url);
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build())
                .build()
                .create(CloudinaryApi.class);
    }

    @Provides
    @Singleton
    public PlaceApi providePlaceApi(@ApplicationContext Context context) {
        String baseUrl = context.getString(R.string.base_url);
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalTime.class, new LocalTimeDeserializer())
                .create();
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(new OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build())
                .build()
                .create(PlaceApi.class);
    }

    @Provides
    @Singleton
    public TripApi provideTripApi(@ApplicationContext Context context) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                .create();
        String baseUrl = context.getString(R.string.base_url);
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(new OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build())
                .build()
                .create(TripApi.class);
    }

    @Provides
    @Singleton
    public UserApi provideUserApi(@ApplicationContext Context context) {
        String baseUrl = context.getString(R.string.base_url);
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build())
                .build()
                .create(UserApi.class);
    }

    @Provides
    @Singleton
    public LeaderboardApi provideLeaderboardApi(@ApplicationContext Context context) {
        String baseUrl = context.getString(R.string.base_url);
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build())
                .build()
                .create(LeaderboardApi.class);
    }
}
