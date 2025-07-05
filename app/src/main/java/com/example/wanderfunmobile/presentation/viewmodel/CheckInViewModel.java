package com.example.wanderfunmobile.presentation.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wanderfunmobile.domain.model.Result;
import com.example.wanderfunmobile.domain.model.checkins.CheckIn;
import com.example.wanderfunmobile.domain.model.places.Place;
import com.example.wanderfunmobile.domain.repository.CheckInRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CheckInViewModel extends ViewModel {
    private final CheckInRepository checkInRepository;

    @Inject
    public CheckInViewModel(CheckInRepository checkInRepository) {
        this.checkInRepository = checkInRepository;
    }

    private final MutableLiveData<Result<List<CheckIn>>> findAllByUserLiveData = new MutableLiveData<>();
    private final MutableLiveData<Result<CheckIn>> createCheckInLiveData = new MutableLiveData<>();
    private final MutableLiveData<Result<List<Place>>> findAllEligiblePlacesLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isCheckingIn = new MutableLiveData<>();

    public MutableLiveData<Result<List<CheckIn>>> getFindAllByUserLiveData() {
        return findAllByUserLiveData;
    }

    public MutableLiveData<Result<CheckIn>> getCreateCheckInLiveData() {
        return createCheckInLiveData;
    }

    public MutableLiveData<Result<List<Place>>> getFindAllEligiblePlacesLiveData() {
        return findAllEligiblePlacesLiveData;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MutableLiveData<Boolean> getIsCheckingIn() {
        return isCheckingIn;
    }

    public void findAllByUser(String bearerToken) {
        isLoading.setValue(true);
        checkInRepository.findAllByUser(bearerToken).observeForever(result -> {
            isLoading.setValue(false);
            findAllByUserLiveData.setValue(result);
        });
    }

    public void createCheckIn(String bearerToken, Long placeId) {
        isCheckingIn.setValue(true);
        isLoading.setValue(true);
        checkInRepository.createCheckIn(bearerToken, placeId).observeForever(result -> {
            isCheckingIn.setValue(false);
            isLoading.setValue(false);
            createCheckInLiveData.setValue(result);
        });
    }

    public void findAllEligiblePlaces(String bearerToken, Double userLng, Double userLat) {
        isLoading.setValue(true);
        checkInRepository.findAllEligiblePlaces(bearerToken, userLng, userLat).observeForever(result -> {
            isLoading.setValue(false);
            findAllEligiblePlacesLiveData.setValue(result);
        });
    }
}
