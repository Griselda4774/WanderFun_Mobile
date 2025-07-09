package com.example.wanderfunmobile.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.wanderfunmobile.data.api.backend.FavoritePlaceApi;
import com.example.wanderfunmobile.data.dto.ResponseDto;
import com.example.wanderfunmobile.data.dto.favouriteplace.FavoritePlaceDto;
import com.example.wanderfunmobile.data.dto.place.PlaceDto;
import com.example.wanderfunmobile.data.mapper.ObjectMapper;
import com.example.wanderfunmobile.domain.model.Result;
import com.example.wanderfunmobile.domain.model.favoriteplaces.FavoritePlace;
import com.example.wanderfunmobile.domain.model.places.Place;
import com.example.wanderfunmobile.domain.repository.FavoritePlaceRepository;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritePlaceRepositoryImpl implements FavoritePlaceRepository {
    private final FavoritePlaceApi favoritePlaceApi;
    private final ObjectMapper objectMapper;

    @Inject
    public FavoritePlaceRepositoryImpl(FavoritePlaceApi favoritePlaceApi, ObjectMapper objectMapper) {
        this.favoritePlaceApi = favoritePlaceApi;
        this.objectMapper = objectMapper;
    }

    @Override
    public LiveData<Result<List<Place>>> findAllByUser(String bearerToken) {
        MutableLiveData<Result<List<Place>>> findAllByUserResponseLiveData = new MutableLiveData<>();
        try {
            Call<ResponseDto<List<PlaceDto>>> call = favoritePlaceApi.findAllByUser(bearerToken);
            call.enqueue(new Callback<ResponseDto<List<PlaceDto>>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<List<PlaceDto>>> call,
                                       @NonNull Response<ResponseDto<List<PlaceDto>>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Result<List<Place>> result = new Result<>();
                        result.setError(response.body().isError());
                        result.setMessage(response.body().getMessage());
                        if (response.body().getData() != null) {
                            result.setData(objectMapper.mapList(response.body().getData(), Place.class));
                        }
                        findAllByUserResponseLiveData.postValue(result);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<List<PlaceDto>>> call,
                                      @NonNull Throwable throwable) {
                    Log.e("FavoritePlaceRepositoryImpl", "Error during onFailure: " + throwable.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e("FavoritePlaceRepositoryImpl", "Error during findAllPostsByCursor", e);
        }

        return findAllByUserResponseLiveData;
    }

    @Override
    public LiveData<Result<Place>> createFavoritePlace(String bearerToken, Long placeId) {
        MutableLiveData<Result<Place>> createFavoritePlaceResponseLiveData = new MutableLiveData<>();
        try {
            Call<ResponseDto<PlaceDto>> call = favoritePlaceApi.createFavoritePlace(bearerToken, placeId);
            call.enqueue(new Callback<ResponseDto<PlaceDto>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<PlaceDto>> call,
                                       @NonNull Response<ResponseDto<PlaceDto>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Result<Place> result = new Result<>();
                        result.setError(response.body().isError());
                        result.setMessage(response.body().getMessage());
                        if (response.body().getData() != null) {
                            result.setData(objectMapper.map(response.body().getData(), Place.class));
                        }
                        createFavoritePlaceResponseLiveData.postValue(result);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<PlaceDto>> call,
                                      @NonNull Throwable throwable) {
                    Log.e("FavoritePlaceRepositoryImpl", "Error during onFailure: " + throwable.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e("FavoritePlaceRepositoryImpl", "Error during create comment", e);
        }
        return createFavoritePlaceResponseLiveData;
    }

    @Override
    public LiveData<Result<Place>> deleteByUserAndPlaceId(String bearerToken, Long placeId) {
        MutableLiveData<Result<Place>> deleteByUserAndPlaceIdResponseLiveData = new MutableLiveData<>();
        try {
            Call<ResponseDto<PlaceDto>> call = favoritePlaceApi.deleteByUserAndPlaceId(bearerToken, placeId);
            call.enqueue(new Callback<ResponseDto<PlaceDto>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<PlaceDto>> call,
                                       @NonNull Response<ResponseDto<PlaceDto>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Result<Place> result = new Result<>();
                        result.setError(response.body().isError());
                        result.setMessage(response.body().getMessage());
                        if (response.body().getData() != null) {
                            result.setData(objectMapper.map(response.body().getData(), Place.class));
                        }
                        deleteByUserAndPlaceIdResponseLiveData.postValue(result);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<PlaceDto>> call,
                                      @NonNull Throwable throwable) {
                    Log.e("FavoritePlaceRepositoryImpl", "Error during onFailure: " + throwable.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e("FavoritePlaceRepositoryImpl", "Error during create comment", e);
        }
        return deleteByUserAndPlaceIdResponseLiveData;
    }
}
