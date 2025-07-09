package com.example.wanderfunmobile.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wanderfunmobile.data.dto.goong.direction.GoongDirectionRequestDto;
import com.example.wanderfunmobile.data.dto.goong.direction.GoongDirectionRespondDto;
import com.example.wanderfunmobile.data.dto.goong.trip.GoongTripRequestDto;
import com.example.wanderfunmobile.data.dto.goong.trip.GoongTripRespondDto;
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
    private final MutableLiveData<Result<GoongDirectionRespondDto>> getGoongDirectionResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public MutableLiveData<Result<GoongTripRespondDto>> getGoongTripResponseLiveData() {
        return getGoongTripResponseLiveData;
    }

    public MutableLiveData<Result<GoongDirectionRespondDto>> getGoongDirectionResponseLiveData() {
        return getGoongDirectionResponseLiveData;
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

    public void getGoongDirection(String bearerToken, GoongDirectionRequestDto goongDirectionRequestDto, String apiKey) {
        isLoading.setValue(true);
        goongRepository.getGoongDirection(bearerToken, goongDirectionRequestDto, apiKey).observeForever(response -> {
            getGoongDirectionResponseLiveData.postValue(response);
            isLoading.setValue(false);
        });
    }
}
