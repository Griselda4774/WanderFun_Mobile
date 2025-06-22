package com.example.wanderfunmobile.presentation.viewmodel.places;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wanderfunmobile.domain.model.CheckIn;
import com.example.wanderfunmobile.domain.model.FavouritePlace;
import com.example.wanderfunmobile.domain.model.Result;
import com.example.wanderfunmobile.domain.model.places.Place;
import com.example.wanderfunmobile.domain.repository.PlaceRepository;

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

    private final MutableLiveData<Result<List<Place>>> findAllPlacesResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<Result<List<Place>>> findAllPlacesByProvinceNameResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<Result<List<Place>>> findAllPlacesByNameContainingResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<Result<Place>> findPlaceByIdResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<Result<List<FavouritePlace>>> findAllFavouritePlacesResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<Result<FavouritePlace>> addFavouritePlaceResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<Result<FavouritePlace>> deleteFavouritePlaceByIdsResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<Result<CheckIn>> checkInPlaceResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<Result<CheckIn>> findCheckInByPlaceIdResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isCheckingIn = new MutableLiveData<>();

    public LiveData<Result<List<Place>>> getFindAllPlacesResponseLiveData() {
        return findAllPlacesResponseLiveData;
    }

    public LiveData<Result<List<Place>>> getFindAllPlacesByProvinceNameResponseLiveData() {
        return findAllPlacesByProvinceNameResponseLiveData;
    }

    public LiveData<Result<List<Place>>> getFindAllPlacesByNameContainingResponseLiveData() {
        return findAllPlacesByNameContainingResponseLiveData;
    }

    public LiveData<Result<Place>> getFindPlaceByIdResponseLiveData() {
        return findPlaceByIdResponseLiveData;
    }

    public LiveData<Result<List<FavouritePlace>>> getFindAllFavouritePlacesResponseLiveData() {
        return findAllFavouritePlacesResponseLiveData;
    }

    public LiveData<Result<FavouritePlace>> getAddFavouritePlaceResponseLiveData() {
        return addFavouritePlaceResponseLiveData;
    }

    public LiveData<Result<FavouritePlace>> getDeleteFavouritePlaceByIdsResponseLiveData() {
        return deleteFavouritePlaceByIdsResponseLiveData;
    }

    public LiveData<Result<CheckIn>> getCheckInPlaceResponseLiveData() {
        return checkInPlaceResponseLiveData;
    }

    public LiveData<Result<CheckIn>> getFindCheckInByPlaceIdResponseLiveData() {
        return findCheckInByPlaceIdResponseLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<Boolean> getIsCheckingIn() {
        return isCheckingIn;
    }

    public void findAllPlaces() {
        isLoading.setValue(true);
        placeRepository.findAllPlaces().observeForever(response -> {
            findAllPlacesResponseLiveData.setValue(response);
            isLoading.setValue(false);
        });
    }

    public void findAllPlacesByProvinceName(String provinceName) {
        isLoading.setValue(true);
        placeRepository.findAllPlacesByProvinceName(provinceName).observeForever(response -> {
            findAllPlacesByProvinceNameResponseLiveData.setValue(response);
            isLoading.setValue(false);
        });
    }

    public void findAllPlacesByNameContaining(String name) {
        isLoading.setValue(true);
        placeRepository.findAllPlacesByNameContaining(name).observeForever(response -> {
            findAllPlacesByNameContainingResponseLiveData.setValue(response);
            isLoading.setValue(false);
        });
    }

    public void findPlaceById(Long placeId) {
        isLoading.setValue(true);
        placeRepository.findPlaceById(placeId).observeForever(response -> {
            findPlaceByIdResponseLiveData.setValue(response);
            isLoading.setValue(false);
        });
    }

//    public void findAllFavouritePlaces(String bearerToken) {
//        isLoading.setValue(true);
//        placeRepository.findAllFavouritePlaces(bearerToken).observeForever(response -> {
//            findAllFavouritePlacesResponseLiveData.setValue(response);
//            isLoading.setValue(false);
//        });
//    }
//
//    public void addFavouritePlace(String bearerToken, Long placeId) {
//        isLoading.setValue(true);
//        placeRepository.addFavouritePlace(bearerToken, placeId).observeForever(response -> {
//            addFavouritePlaceResponseLiveData.setValue(response);
//            isLoading.setValue(false);
//        });
//    }
//
//    public void deleteFavouritePlaceByIds(String bearerToken, List<Long> placeIds) {
//        isLoading.setValue(true);
//        placeRepository.deleteFavouritePlaceByIds(bearerToken, placeIds).observeForever(response -> {
//            deleteFavouritePlaceByIdsResponseLiveData.setValue(response);
//            isLoading.setValue(false);
//        });
//    }
//
//    public void findCheckInByPlaceId(String bearerToken, Long placeId) {
//        isLoading.setValue(true);
//        placeRepository.findCheckInByPlaceId(bearerToken, placeId).observeForever(response -> {
//            findCheckInByPlaceIdResponseLiveData.setValue(response);
//            isLoading.setValue(false);
//        });
//    }
//
//    public void checkInPlace(String bearerToken, Long placeId) {
//        isCheckingIn.setValue(true);
//        placeRepository.checkInPlace(bearerToken, placeId).observeForever(response -> {
//            checkInPlaceResponseLiveData.setValue(response);
//            isCheckingIn.setValue(false);
//        });
//    }
}
