package com.example.wanderfunmobile.data.api.backend;

import com.example.wanderfunmobile.data.dto.ResponseDto;
import com.example.wanderfunmobile.data.dto.leaderboard.LeaderboardPlaceDto;
import com.example.wanderfunmobile.data.dto.leaderboard.LeaderboardUserDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface LeaderboardApi {
    @GET("leaderboard/user")
    Call<ResponseDto<List<LeaderboardUserDto>>> getLeaderboardUser();

    @GET("leaderboard/place")
    Call<ResponseDto<List<LeaderboardPlaceDto>>> getLeaderboardPlace();
}
