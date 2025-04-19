package com.example.wanderfunmobile.data.repository;

import androidx.lifecycle.LiveData;

import com.example.wanderfunmobile.data.dto.ResponseDto;
import com.example.wanderfunmobile.data.dto.leaderboard.LeaderboardPlaceDto;
import com.example.wanderfunmobile.data.dto.leaderboard.LeaderboardUserDto;

import java.util.List;

public interface LeaderboardRepository {
    LiveData<ResponseDto<List<LeaderboardUserDto>>> getLeaderboardUser();

    LiveData<ResponseDto<List<LeaderboardPlaceDto>>> getLeaderboardPlace();
}
