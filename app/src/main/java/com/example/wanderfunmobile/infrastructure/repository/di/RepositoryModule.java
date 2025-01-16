package com.example.wanderfunmobile.infrastructure.repository.di;

import com.example.wanderfunmobile.application.repository.AlbumRepository;
import com.example.wanderfunmobile.application.repository.AuthRepository;
import com.example.wanderfunmobile.application.repository.LeaderboardRepository;
import com.example.wanderfunmobile.application.repository.PlaceRepository;
import com.example.wanderfunmobile.application.repository.TripRepository;
import com.example.wanderfunmobile.application.repository.UserRepository;
import com.example.wanderfunmobile.infrastructure.api.backend.AlbumApi;
import com.example.wanderfunmobile.infrastructure.api.backend.AuthApi;
import com.example.wanderfunmobile.infrastructure.api.backend.LeaderboardApi;
import com.example.wanderfunmobile.infrastructure.api.backend.PlaceApi;
import com.example.wanderfunmobile.infrastructure.api.backend.TripApi;
import com.example.wanderfunmobile.infrastructure.api.backend.UserApi;
import com.example.wanderfunmobile.infrastructure.repository.AlbumRepositoryImpl;
import com.example.wanderfunmobile.infrastructure.repository.AuthRepositoryImpl;
import com.example.wanderfunmobile.infrastructure.repository.LeaderboardRepositoryImpl;
import com.example.wanderfunmobile.infrastructure.repository.PlaceRepositoryImpl;
import com.example.wanderfunmobile.infrastructure.repository.TripRepositoryImpl;
import com.example.wanderfunmobile.infrastructure.repository.UserRepositoryImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class RepositoryModule {
    @Provides
    @Singleton
    public AuthRepository provideAuthRepository(AuthApi authApi) {
        return new AuthRepositoryImpl(authApi);
    }

    @Provides
    @Singleton
    public PlaceRepository providePlaceRepository(PlaceApi placeApi) {
        return new PlaceRepositoryImpl(placeApi);
    }

    @Provides
    @Singleton
    public AlbumRepository provideAlbumRepository(AlbumApi albumApi) {
        return new AlbumRepositoryImpl(albumApi);
    }

    @Provides
    @Singleton
    public TripRepository provideTripRepository(TripApi tripApi) {
        return new TripRepositoryImpl(tripApi);
    }

    @Provides
    @Singleton
    public UserRepository provideUserRepository(UserApi userApi) {
        return new UserRepositoryImpl(userApi);
    }

    @Provides
    @Singleton
    public LeaderboardRepository provideLeaderboardRepository(LeaderboardApi leaderboardApi) {
        return new LeaderboardRepositoryImpl(leaderboardApi);
    }
}
