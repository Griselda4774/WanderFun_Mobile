package com.example.wanderfunmobile.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wanderfunmobile.data.dto.ResponseDto;
import com.example.wanderfunmobile.data.dto.leaderboard.LeaderboardPlaceDto;
import com.example.wanderfunmobile.data.dto.leaderboard.LeaderboardUserDto;
import com.example.wanderfunmobile.domain.repository.LeaderboardRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class LeaderboardViewModel extends ViewModel {
    private final LeaderboardRepository leaderboardRepository;

    @Inject
    public LeaderboardViewModel(LeaderboardRepository leaderboardRepository) {
        this.leaderboardRepository = leaderboardRepository;
    }

    private final MutableLiveData<ResponseDto<List<LeaderboardUserDto>>> getLeaderboardUserResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<ResponseDto<List<LeaderboardPlaceDto>>> getLeaderboardPlaceResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public LiveData<ResponseDto<List<LeaderboardUserDto>>> getLeaderboardUserResponseLiveData() {
        return getLeaderboardUserResponseLiveData;
    }

    public LiveData<ResponseDto<List<LeaderboardPlaceDto>>> getLeaderboardPlaceResponseLiveData() {
        return getLeaderboardPlaceResponseLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void getLeaderboardUser(String bearerToken) {
        isLoading.setValue(true);
        leaderboardRepository.getLeaderboardUser(bearerToken).observeForever(response -> {
            getLeaderboardUserResponseLiveData.setValue(response);
            isLoading.setValue(false);
        });
    }

    public void getLeaderboardPlace(String bearerToken) {
        isLoading.setValue(true);
        leaderboardRepository.getLeaderboardPlace(bearerToken).observeForever(response -> {
            getLeaderboardPlaceResponseLiveData.setValue(response);
            isLoading.setValue(false);
        });
    }
}
