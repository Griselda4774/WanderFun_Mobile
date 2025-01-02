package com.example.wanderfunmobile.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wanderfunmobile.application.dto.ResponseDto;
import com.example.wanderfunmobile.application.dto.trip.TripCreateDto;
import com.example.wanderfunmobile.application.repository.TripRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class TripViewModel extends ViewModel {
    private final TripRepository tripRepository;

    @Inject
    public TripViewModel(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    private final MutableLiveData<ResponseDto<Object>> getAllTripsResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<ResponseDto<Object>> getTripByIdResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<ResponseDto<Object>> createTripResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<ResponseDto<Object>> updateTripByIdResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<ResponseDto<Object>> deleteAllTripsResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<ResponseDto<Object>> deleteTripByIdResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public LiveData<ResponseDto<Object>> getTripByIdResponseLiveData() {
        return getTripByIdResponseLiveData;
    }

    public LiveData<ResponseDto<Object>> getAllTripsResponseLiveData() {
        return getAllTripsResponseLiveData;
    }

    public LiveData<ResponseDto<Object>> createTripResponseLiveData() {
        return createTripResponseLiveData;
    }

    public LiveData<ResponseDto<Object>> updateTripByIdResponseLiveData() {
        return updateTripByIdResponseLiveData;
    }


    public LiveData<ResponseDto<Object>> deleteAllTripsResponseLiveData() {
        return deleteAllTripsResponseLiveData;
    }

    public LiveData<ResponseDto<Object>> deleteTripByIdResponseLiveData() {
        return deleteTripByIdResponseLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }


    public void getAllTrips(String bearerToken) {
        isLoading.setValue(true);
        tripRepository.getAllTrips(bearerToken).observeForever(response -> {
            getAllTripsResponseLiveData.setValue(response);
            isLoading.setValue(false);
        });
    }

    public void getTripById(String bearerToken, Long tripId) {
        isLoading.setValue(true);
        tripRepository.getTripById(bearerToken, tripId).observeForever(response -> {
            getTripByIdResponseLiveData.setValue(response);
            isLoading.setValue(false);
        });
    }

    public void createTrip(String bearerToken, TripCreateDto tripCreateDto) {
        isLoading.setValue(true);
        tripRepository.createTrip(bearerToken, tripCreateDto).observeForever(response -> {
            createTripResponseLiveData.setValue(response);
            isLoading.setValue(false);
        });
    }

    public void updateTripById(String bearerToken, Long tripId, TripCreateDto tripCreateDto) {
        isLoading.setValue(true);
        tripRepository.updateTripById(bearerToken, tripId, tripCreateDto).observeForever(response -> {
            updateTripByIdResponseLiveData.setValue(response);
            isLoading.setValue(false);
        });
    }

    public void deleteAllTrips(String bearerToken) {
        isLoading.setValue(true);
        tripRepository.deleteAllTrips(bearerToken).observeForever(response -> {
            deleteAllTripsResponseLiveData.setValue(response);
            isLoading.setValue(false);
        });
    }

    public void deleteTripById(String bearerToken, Long tripId) {
        isLoading.setValue(true);
        tripRepository.deleteTripById(bearerToken, tripId).observeForever(response -> {
            deleteTripByIdResponseLiveData.setValue(response);
            isLoading.setValue(false);
        });
    }
}
