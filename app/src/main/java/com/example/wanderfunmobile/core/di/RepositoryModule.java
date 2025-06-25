package com.example.wanderfunmobile.core.di;

import com.example.wanderfunmobile.data.api.backend.AddressApi;
import com.example.wanderfunmobile.data.api.backend.PostApi;
import com.example.wanderfunmobile.data.api.backend.places.FeedbackApi;
import com.example.wanderfunmobile.data.mapper.ObjectMapper;
import com.example.wanderfunmobile.data.repository.AddressRepositoryImpl;
import com.example.wanderfunmobile.data.repository.LeaderboardRepositoryImpl;
import com.example.wanderfunmobile.data.repository.PostRepositoryImpl;
import com.example.wanderfunmobile.data.repository.places.FeedbackRepositoryImpl;
import com.example.wanderfunmobile.domain.repository.AddressRepository;
import com.example.wanderfunmobile.domain.repository.AlbumRepository;
import com.example.wanderfunmobile.domain.repository.AuthRepository;
import com.example.wanderfunmobile.domain.repository.LeaderboardRepository;
import com.example.wanderfunmobile.domain.repository.places.FeedbackRepository;
import com.example.wanderfunmobile.domain.repository.places.PlaceRepository;
import com.example.wanderfunmobile.domain.repository.PostRepository;
import com.example.wanderfunmobile.domain.repository.TripRepository;
import com.example.wanderfunmobile.domain.repository.UserRepository;
import com.example.wanderfunmobile.data.api.backend.AlbumApi;
import com.example.wanderfunmobile.data.api.backend.AuthApi;
import com.example.wanderfunmobile.data.api.backend.LeaderboardApi;
import com.example.wanderfunmobile.data.api.backend.places.PlaceApi;
import com.example.wanderfunmobile.data.api.backend.TripApi;
import com.example.wanderfunmobile.data.api.backend.UserApi;
import com.example.wanderfunmobile.data.repository.AlbumRepositoryImpl;
import com.example.wanderfunmobile.data.repository.AuthRepositoryImpl;
import com.example.wanderfunmobile.data.repository.places.PlaceRepositoryImpl;
import com.example.wanderfunmobile.data.repository.TripRepositoryImpl;
import com.example.wanderfunmobile.data.repository.UserRepositoryImpl;

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
    public AddressRepository provideAddressRepository(AddressApi addressApi, ObjectMapper objectMapper) {
        return new AddressRepositoryImpl(addressApi, objectMapper);
    }

    @Provides
    @Singleton
    public PlaceRepository providePlaceRepository(PlaceApi placeApi, ObjectMapper objectMapper) {
        return new PlaceRepositoryImpl(placeApi, objectMapper);
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
    public UserRepository provideUserRepository(UserApi userApi, ObjectMapper objectMapper) {
        return new UserRepositoryImpl(userApi, objectMapper);
    }

    @Provides
    @Singleton
    public LeaderboardRepository provideLeaderboardRepository(LeaderboardApi leaderboardApi) {
        return new LeaderboardRepositoryImpl(leaderboardApi);
    }

    @Provides
    @Singleton
    public PostRepository providePostRepository(PostApi postApi, ObjectMapper objectMapper) {
        return new PostRepositoryImpl(postApi, objectMapper);
    }

    @Provides
    @Singleton
    public FeedbackRepository provideFeedbackRepository(FeedbackApi feedbackApi, ObjectMapper objectMapper) {
        return new FeedbackRepositoryImpl(feedbackApi, objectMapper);
    }
}
