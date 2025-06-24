package com.example.wanderfunmobile.data.api.backend;

import com.example.wanderfunmobile.data.dto.ResponseDto;
import com.example.wanderfunmobile.data.dto.leaderboard.LeaderboardPlaceDto;
import com.example.wanderfunmobile.data.dto.leaderboard.LeaderboardUserDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface LeaderboardApi {
    @GET("statistics/user-ranking")
    Call<ResponseDto<List<LeaderboardUserDto>>> getLeaderboardUser(@Header("Authorization") String bearerToken);

    @GET("statistics/place-ranking")
    Call<ResponseDto<List<LeaderboardPlaceDto>>> getLeaderboardPlace(@Header("Authorization") String bearerToken);
}
