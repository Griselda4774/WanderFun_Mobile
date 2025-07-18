package com.example.wanderfunmobile.domain.repository;

import androidx.lifecycle.LiveData;

import com.example.wanderfunmobile.domain.model.Result;
import com.example.wanderfunmobile.domain.model.favoriteplaces.FavoritePlace;
import com.example.wanderfunmobile.domain.model.places.Place;

import java.util.List;

public interface FavoritePlaceRepository {
    LiveData<Result<List<Place>>> findAllByUser(String bearerToken);
    LiveData<Result<Place>> createFavoritePlace(String bearerToken, Long placeId);
    LiveData<Result<Place>> deleteByUserAndPlaceId(String bearerToken, Long id);
}
