package com.example.wanderfunmobile.domain.repository;

import androidx.lifecycle.LiveData;

import com.example.wanderfunmobile.data.dto.ResponseDto;
import com.example.wanderfunmobile.data.dto.leaderboard.LeaderboardPlaceDto;
import com.example.wanderfunmobile.data.dto.leaderboard.LeaderboardUserDto;

import java.util.List;

public interface LeaderboardRepository {
    LiveData<ResponseDto<List<LeaderboardUserDto>>> getLeaderboardUser(String bearerToken);

    LiveData<ResponseDto<List<LeaderboardPlaceDto>>> getLeaderboardPlace(String bearerToken);
}
