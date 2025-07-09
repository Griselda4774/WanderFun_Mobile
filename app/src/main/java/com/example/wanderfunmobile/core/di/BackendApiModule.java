package com.example.wanderfunmobile.core.di;

import android.content.Context;

import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.data.api.backend.AddressApi;
import com.example.wanderfunmobile.data.api.backend.AlbumApi;
import com.example.wanderfunmobile.data.api.backend.AuthApi;
import com.example.wanderfunmobile.data.api.backend.CheckInApi;
import com.example.wanderfunmobile.data.api.backend.CloudinaryApi;
import com.example.wanderfunmobile.data.api.backend.FavoritePlaceApi;
import com.example.wanderfunmobile.data.api.backend.GoongApi;
import com.example.wanderfunmobile.data.api.backend.LeaderboardApi;
import com.example.wanderfunmobile.data.api.backend.places.FeedbackApi;
import com.example.wanderfunmobile.data.api.backend.places.PlaceApi;
import com.example.wanderfunmobile.data.api.backend.PostApi;
import com.example.wanderfunmobile.data.api.backend.TripApi;
import com.example.wanderfunmobile.data.api.backend.UserApi;
import com.google.gson.Gson;

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
    public AlbumApi provideAlbumApi(@ApplicationContext Context context, Gson gson) {
        String baseUrl = context.getString(R.string.base_url);
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
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
    public PlaceApi providePlaceApi(@ApplicationContext Context context, Gson gson) {
        String baseUrl = context.getString(R.string.base_url);
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
    public FeedbackApi provideFeedbackApi(@ApplicationContext Context context, Gson gson) {
        String baseUrl = context.getString(R.string.base_url);
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(new OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build())
                .build()
                .create(FeedbackApi.class);
    }

    @Provides
    @Singleton
    public TripApi provideTripApi(@ApplicationContext Context context, Gson gson) {
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
    public UserApi provideUserApi(@ApplicationContext Context context, Gson gson) {
        String baseUrl = context.getString(R.string.base_url);
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
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
    public PostApi providePostApi(@ApplicationContext Context context, Gson gson) {
        String baseUrl = context.getString(R.string.base_url);
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

    @Provides
    @Singleton
    public CheckInApi provideCheckInApi(@ApplicationContext Context context, Gson gson) {
        String baseUrl = context.getString(R.string.base_url);
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(new OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build())
                .build()
                .create(CheckInApi.class);
    }

    @Provides
    @Singleton
    public GoongApi provideGoongApi(@ApplicationContext Context context) {
        String baseUrl = context.getString(R.string.goong_api_url);
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build())
                .build()
                .create(GoongApi.class);
    }

    @Provides
    @Singleton
    public FavoritePlaceApi provideFavoritePlaceApi(@ApplicationContext Context context, Gson gson) {
        String baseUrl = context.getString(R.string.base_url);
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(new OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build())
                .build()
                .create(FavoritePlaceApi.class);
    }
}
