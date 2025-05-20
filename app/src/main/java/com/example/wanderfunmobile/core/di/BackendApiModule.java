package com.example.wanderfunmobile.core.di;

import android.content.Context;

import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.core.util.LocalDateDeserializer;
import com.example.wanderfunmobile.core.util.LocalDateSerializer;
import com.example.wanderfunmobile.core.util.typeadapter.LocalDateTimeAdapter;
import com.example.wanderfunmobile.data.api.backend.AddressApi;
import com.example.wanderfunmobile.data.api.backend.AlbumApi;
import com.example.wanderfunmobile.data.api.backend.AuthApi;
import com.example.wanderfunmobile.data.api.backend.CloudinaryApi;
import com.example.wanderfunmobile.data.api.backend.LeaderboardApi;
import com.example.wanderfunmobile.data.api.backend.PlaceApi;
import com.example.wanderfunmobile.data.api.backend.PostApi;
import com.example.wanderfunmobile.data.api.backend.TripApi;
import com.example.wanderfunmobile.data.api.backend.UserApi;
import com.example.wanderfunmobile.core.util.DateDeserializer;
import com.example.wanderfunmobile.core.util.LocalTimeDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    public AddressApi provideAddressApi(@ApplicationContext Context context) {
        String baseUrl = context.getString(R.string.base_url);
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build())
                .build()
                .create(AddressApi.class);
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
                .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
                .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
                .setDateFormat("yyyy-MM-dd")
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

    @Provides
    @Singleton
    public PostApi providePostApi(@ApplicationContext Context context) {
        String baseUrl = context.getString(R.string.base_url);
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(new OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build())
                .build()
                .create(PostApi.class);
    }
}
