package com.example.wanderfunmobile.domain.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.wanderfunmobile.data.dto.ResponseDto;
import com.example.wanderfunmobile.data.dto.leaderboard.LeaderboardPlaceDto;
import com.example.wanderfunmobile.data.dto.leaderboard.LeaderboardUserDto;
import com.example.wanderfunmobile.data.repository.LeaderboardRepository;
import com.example.wanderfunmobile.data.api.backend.LeaderboardApi;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaderboardRepositoryImpl implements LeaderboardRepository {
    private final LeaderboardApi leaderboardApi;

    @Inject
    public LeaderboardRepositoryImpl(LeaderboardApi leaderboardApi) {
        this.leaderboardApi = leaderboardApi;
    }

    @Override
    public LiveData<ResponseDto<List<LeaderboardUserDto>>> getLeaderboardUser() {
        MutableLiveData<ResponseDto<List<LeaderboardUserDto>>> getLeaderboardUserResponseLiveData = new MutableLiveData<>();
        String errorType = "PlaceRepositoryImpl GetLeaderboardUser Error";
        try {
            Call<ResponseDto<List<LeaderboardUserDto>>> call = leaderboardApi.getLeaderboardUser();
            call.enqueue(new Callback<ResponseDto<List<LeaderboardUserDto>>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<List<LeaderboardUserDto>>> call,
                                       @NonNull Response<ResponseDto<List<LeaderboardUserDto>>> response) {
                    getLeaderboardUserResponseLiveData.postValue(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<List<LeaderboardUserDto>>> call,
                                      @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during onFailure");
                }
            });
        } catch (Exception e) {
            Log.e(errorType, "Error during catch");
        }

        return getLeaderboardUserResponseLiveData;
    }

    @Override
    public LiveData<ResponseDto<List<LeaderboardPlaceDto>>> getLeaderboardPlace() {
        MutableLiveData<ResponseDto<List<LeaderboardPlaceDto>>> getLeaderboardPlaceResponseLiveData = new MutableLiveData<>();
        String errorType = "PlaceRepositoryImpl GetLeaderboardPlace Error";
        try {
            Call<ResponseDto<List<LeaderboardPlaceDto>>> call = leaderboardApi.getLeaderboardPlace();
            call.enqueue(new Callback<ResponseDto<List<LeaderboardPlaceDto>>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<List<LeaderboardPlaceDto>>> call,
                                       @NonNull Response<ResponseDto<List<LeaderboardPlaceDto>>> response) {
                    getLeaderboardPlaceResponseLiveData.postValue(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<List<LeaderboardPlaceDto>>> call,
                                      @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during onFailure");
                }
            });
        } catch (Exception e) {
            Log.e(errorType, "Error during catch");
        }

        return getLeaderboardPlaceResponseLiveData;
    }

}
