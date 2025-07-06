package com.example.wanderfunmobile.domain.repository;

import androidx.lifecycle.LiveData;

import com.example.wanderfunmobile.domain.model.Result;
import com.example.wanderfunmobile.domain.model.favoriteplaces.FavoritePlace;

import java.util.List;

public interface FavoritePlaceRepository {
    LiveData<Result<List<FavoritePlace>>> findAllByUser(String bearerToken);
    LiveData<Result<FavoritePlace>> createFavoritePlace(String bearerToken, Long placeId);
    LiveData<Result<FavoritePlace>> deleteById(String bearerToken, Long id);
}
