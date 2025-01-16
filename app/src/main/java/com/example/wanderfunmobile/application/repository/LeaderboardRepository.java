package com.example.wanderfunmobile.application.repository;

import androidx.lifecycle.LiveData;

import com.example.wanderfunmobile.application.dto.ResponseDto;
import com.example.wanderfunmobile.application.dto.leaderboard.LeaderboardPlaceDto;
import com.example.wanderfunmobile.application.dto.leaderboard.LeaderboardUserDto;

import java.util.List;

public interface LeaderboardRepository {
    LiveData<ResponseDto<List<LeaderboardUserDto>>> getLeaderboardUser();

    LiveData<ResponseDto<List<LeaderboardPlaceDto>>> getLeaderboardPlace();
}
