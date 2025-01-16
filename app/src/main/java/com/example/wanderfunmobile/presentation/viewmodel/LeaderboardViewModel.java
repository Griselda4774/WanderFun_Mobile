package com.example.wanderfunmobile.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wanderfunmobile.application.dto.ResponseDto;
import com.example.wanderfunmobile.application.dto.leaderboard.LeaderboardPlaceDto;
import com.example.wanderfunmobile.application.dto.leaderboard.LeaderboardUserDto;
import com.example.wanderfunmobile.application.repository.LeaderboardRepository;

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

    public void getLeaderboardUser() {
        isLoading.setValue(true);
        leaderboardRepository.getLeaderboardUser().observeForever(response -> {
            getLeaderboardUserResponseLiveData.setValue(response);
            isLoading.setValue(false);
        });
    }

    public void getLeaderboardPlace() {
        isLoading.setValue(true);
        leaderboardRepository.getLeaderboardPlace().observeForever(response -> {
            getLeaderboardPlaceResponseLiveData.setValue(response);
            isLoading.setValue(false);
        });
    }
}
