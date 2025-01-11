package com.example.wanderfunmobile.infrastructure.api.backend.di;

import android.content.Context;

import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.infrastructure.api.backend.AlbumApi;
import com.example.wanderfunmobile.infrastructure.api.backend.AuthApi;
import com.example.wanderfunmobile.infrastructure.api.backend.CloudinaryApi;
import com.example.wanderfunmobile.infrastructure.api.backend.PlaceApi;
import com.example.wanderfunmobile.infrastructure.api.backend.TripApi;
import com.example.wanderfunmobile.infrastructure.api.backend.UserApi;

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
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
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
        String baseUrl = context.getString(R.string.base_url);
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
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
}
