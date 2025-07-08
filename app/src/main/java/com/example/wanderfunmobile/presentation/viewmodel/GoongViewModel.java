package com.example.wanderfunmobile.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wanderfunmobile.data.dto.goong.GoongTripRequestDto;
import com.example.wanderfunmobile.data.dto.goong.GoongTripRespondDto;
import com.example.wanderfunmobile.domain.model.Result;
import com.example.wanderfunmobile.domain.repository.GoongRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class GoongViewModel extends ViewModel {
    private final GoongRepository goongRepository;

    @Inject
    public GoongViewModel(GoongRepository goongRepository) {
        this.goongRepository = goongRepository;
    }

    private final MutableLiveData<Result<GoongTripRespondDto>> getGoongTripResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public MutableLiveData<Result<GoongTripRespondDto>> getGetGoongTripResponseLiveData() {
        return getGoongTripResponseLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void getGoongTrip(String bearerToken, GoongTripRequestDto goongTripRequestDto, String apiKey) {
        isLoading.setValue(true);
        goongRepository.getGoongTrip(bearerToken, goongTripRequestDto, apiKey).observeForever(response -> {
            getGoongTripResponseLiveData.postValue(response);
            isLoading.setValue(false);
        });
    }
}
