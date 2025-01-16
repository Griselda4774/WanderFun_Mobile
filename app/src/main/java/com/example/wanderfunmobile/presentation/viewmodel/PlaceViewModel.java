package com.example.wanderfunmobile.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wanderfunmobile.application.dto.ResponseDto;
import com.example.wanderfunmobile.application.dto.checkin.CheckInDto;
import com.example.wanderfunmobile.application.dto.favouriteplace.FavouritePlaceDto;
import com.example.wanderfunmobile.application.dto.feedback.FeedbackCreateDto;
import com.example.wanderfunmobile.application.dto.feedback.FeedbackDto;
import com.example.wanderfunmobile.application.dto.place.PlaceDto;
import com.example.wanderfunmobile.application.dto.place.PlaceMiniDto;
import com.example.wanderfunmobile.application.repository.PlaceRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class PlaceViewModel extends ViewModel {
    private final PlaceRepository placeRepository;


    @Inject
    public PlaceViewModel(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    private final MutableLiveData<ResponseDto<List<PlaceMiniDto>>> getAllPlacesResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<ResponseDto<List<PlaceMiniDto>>> searchPlacesByNameContainingResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<ResponseDto<PlaceDto>> getPlaceByIdResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<ResponseDto<FeedbackDto>> createFeedbackResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<ResponseDto<List<FavouritePlaceDto>>> getAllFavouritePlacesResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<ResponseDto<FavouritePlaceDto>> addFavouritePlaceResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<ResponseDto<FavouritePlaceDto>> deleteFavouritePlaceByIdsResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<ResponseDto<CheckInDto>> checkInPlaceResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isCheckingIn = new MutableLiveData<>();

    public LiveData<ResponseDto<List<PlaceMiniDto>>> getAllPlacesResponseLiveData() {
        return getAllPlacesResponseLiveData;
    }

    public LiveData<ResponseDto<List<PlaceMiniDto>>> searchPlacesByNameContainingResponseLiveData() {
        return searchPlacesByNameContainingResponseLiveData;
    }

    public LiveData<ResponseDto<PlaceDto>> getPlaceByIdResponseLiveData() {
        return getPlaceByIdResponseLiveData;
    }

    public LiveData<ResponseDto<FeedbackDto>> createFeedbackResponseLiveData() {
        return createFeedbackResponseLiveData;
    }

    public LiveData<ResponseDto<List<FavouritePlaceDto>>> getAllFavouritePlacesResponseLiveData() {
        return getAllFavouritePlacesResponseLiveData;
    }

    public LiveData<ResponseDto<FavouritePlaceDto>> addFavouritePlaceResponseLiveData() {
        return addFavouritePlaceResponseLiveData;
    }

    public LiveData<ResponseDto<FavouritePlaceDto>> deleteFavouritePlaceByIdsResponseLiveData() {
        return deleteFavouritePlaceByIdsResponseLiveData;
    }

    public LiveData<ResponseDto<CheckInDto>> checkInPlaceResponseLiveData() {
        return checkInPlaceResponseLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<Boolean> getIsCheckingIn() {
        return isCheckingIn;
    }

    public void getAllPlaces() {
        isLoading.setValue(true);
        placeRepository.getAllPlaces().observeForever(response -> {
            getAllPlacesResponseLiveData.setValue(response);
            isLoading.setValue(false);
        });
    }

    public void searchPlacesByNameContaining(String name) {
        isLoading.setValue(true);
        placeRepository.searchPlacesByNameContaining(name).observeForever(response -> {
            searchPlacesByNameContainingResponseLiveData.setValue(response);
            isLoading.setValue(false);
        });
    }

    public void getPlaceById(Long placeId) {
        isLoading.setValue(true);
        placeRepository.getPlaceById(placeId).observeForever(response -> {
            getPlaceByIdResponseLiveData.setValue(response);
            isLoading.setValue(false);
        });
    }

    public void createFeedback(String bearerToken, FeedbackCreateDto feedbackCreateDto, Long placeId) {
        isLoading.setValue(true);
        placeRepository.createFeedback(bearerToken, feedbackCreateDto, placeId).observeForever(response -> {
            createFeedbackResponseLiveData.setValue(response);
            isLoading.setValue(false);
        });
    }

    public void getAllFavouritePlaces(String bearerToken) {
        isLoading.setValue(true);
        placeRepository.getAllFavouritePlaces(bearerToken).observeForever(response -> {
            getAllFavouritePlacesResponseLiveData.setValue(response);
            isLoading.setValue(false);
        });
    }

    public void addFavouritePlace(String bearerToken, Long placeId) {
        isLoading.setValue(true);
        placeRepository.addFavouritePlace(bearerToken, placeId).observeForever(response -> {
            addFavouritePlaceResponseLiveData.setValue(response);
            isLoading.setValue(false);
        });
    }

    public void deleteFavouritePlaceByIds(String bearerToken, List<Long> placeIds) {
        isLoading.setValue(true);
        placeRepository.deleteFavouritePlaceByIds(bearerToken, placeIds).observeForever(response -> {
            deleteFavouritePlaceByIdsResponseLiveData.setValue(response);
            isLoading.setValue(false);
        });
    }

    public void checkInPlace(String bearerToken, Long placeId) {
        isCheckingIn.setValue(true);
        placeRepository.checkInPlace(bearerToken, placeId).observeForever(response -> {
            checkInPlaceResponseLiveData.setValue(response);
            isCheckingIn.setValue(false);
        });
    }
}
