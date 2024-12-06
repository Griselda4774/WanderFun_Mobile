package com.example.wanderfunmobile.application.usecase.di;

import com.example.wanderfunmobile.application.service.AuthService;
import com.example.wanderfunmobile.application.usecase.AuthUsecase;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class UsecaseModule {
    @Provides
    @Singleton
    public AuthUsecase provideAuthUsecase(AuthService authService) {
        return new AuthUsecase(authService);
    }
}
