package com.example.wanderfunmobile.infrastructure.service.di;

import com.example.wanderfunmobile.application.service.AuthService;
import com.example.wanderfunmobile.infrastructure.service.AuthServiceImpl;
import com.example.wanderfunmobile.network.backend.AuthApi;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class ServiceModule {
    @Provides
    @Singleton
    public AuthService provideAuthService(AuthApi authApi) {
        return new AuthServiceImpl(authApi);
    }
}
