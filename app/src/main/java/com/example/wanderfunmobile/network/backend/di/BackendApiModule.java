package com.example.wanderfunmobile.network.backend.di;

import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.network.backend.AuthApi;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class BackendApiModule {
    @Provides
    @Singleton
    public AuthApi provideAuthApi(Retrofit retrofit) {
        return new Retrofit.Builder()
                .baseUrl(String.valueOf(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build())
                .build()
                .create(AuthApi.class);
    }
}
