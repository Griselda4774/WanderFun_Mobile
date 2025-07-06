package com.example.wanderfunmobile.presentation.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wanderfunmobile.domain.model.Result;
import com.example.wanderfunmobile.domain.model.favoriteplaces.FavoritePlace;
import com.example.wanderfunmobile.domain.repository.FavoritePlaceRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class FavoritePlaceViewModel extends ViewModel {
    private final FavoritePlaceRepository favoritePlaceRepository;

    @Inject
    public FavoritePlaceViewModel(FavoritePlaceRepository favoritePlaceRepository) {
        this.favoritePlaceRepository = favoritePlaceRepository;
    }

    private final MutableLiveData<Result<List<FavoritePlace>>> findAllByUserLiveData = new MutableLiveData<>();
    private final MutableLiveData<Result<FavoritePlace>> createFavoritePlaceLiveData = new MutableLiveData<>();
    private final MutableLiveData<Result<FavoritePlace>> deleteByIdLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public MutableLiveData<Result<List<FavoritePlace>>> getFindAllByUserLiveData() {
        return findAllByUserLiveData;
    }

    public MutableLiveData<Result<FavoritePlace>> getCreateFavoritePlaceLiveData() {
        return createFavoritePlaceLiveData;
    }

    public MutableLiveData<Result<FavoritePlace>> getDeleteByIdLiveData() {
        return deleteByIdLiveData;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void findAllByUser(String bearerToken) {
        isLoading.setValue(true);
        favoritePlaceRepository.findAllByUser(bearerToken).observeForever(result -> {
            isLoading.setValue(false);
            findAllByUserLiveData.setValue(result);
        });
    }

    public void createFavoritePlace(String bearerToken, Long placeId) {
        isLoading.setValue(true);
        favoritePlaceRepository.createFavoritePlace(bearerToken, placeId).observeForever(result -> {
            isLoading.setValue(false);
            createFavoritePlaceLiveData.setValue(result);
        });
    }

    public void deleteById(String bearerToken, Long id) {
        isLoading.setValue(true);
        favoritePlaceRepository.deleteById(bearerToken, id).observeForever(result -> {
            isLoading.setValue(false);
            deleteByIdLiveData.setValue(result);
        });
    }
}
