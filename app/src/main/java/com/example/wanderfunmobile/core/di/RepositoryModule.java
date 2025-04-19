package com.example.wanderfunmobile.core.di;

import com.example.wanderfunmobile.data.repository.AlbumRepository;
import com.example.wanderfunmobile.data.repository.AuthRepository;
import com.example.wanderfunmobile.data.repository.LeaderboardRepository;
import com.example.wanderfunmobile.data.repository.PlaceRepository;
import com.example.wanderfunmobile.data.repository.TripRepository;
import com.example.wanderfunmobile.data.repository.UserRepository;
import com.example.wanderfunmobile.data.api.backend.AlbumApi;
import com.example.wanderfunmobile.data.api.backend.AuthApi;
import com.example.wanderfunmobile.data.api.backend.LeaderboardApi;
import com.example.wanderfunmobile.data.api.backend.PlaceApi;
import com.example.wanderfunmobile.data.api.backend.TripApi;
import com.example.wanderfunmobile.data.api.backend.UserApi;
import com.example.wanderfunmobile.domain.repository.AlbumRepositoryImpl;
import com.example.wanderfunmobile.domain.repository.AuthRepositoryImpl;
import com.example.wanderfunmobile.domain.repository.LeaderboardRepositoryImpl;
import com.example.wanderfunmobile.domain.repository.PlaceRepositoryImpl;
import com.example.wanderfunmobile.domain.repository.TripRepositoryImpl;
import com.example.wanderfunmobile.domain.repository.UserRepositoryImpl;

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
