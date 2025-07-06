package com.example.wanderfunmobile.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.wanderfunmobile.data.api.backend.FavoritePlaceApi;
import com.example.wanderfunmobile.data.dto.ResponseDto;
import com.example.wanderfunmobile.data.dto.favouriteplace.FavoritePlaceDto;
import com.example.wanderfunmobile.data.mapper.ObjectMapper;
import com.example.wanderfunmobile.domain.model.Result;
import com.example.wanderfunmobile.domain.model.favoriteplaces.FavoritePlace;
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
    public LiveData<Result<List<FavoritePlace>>> findAllByUser(String bearerToken) {
        MutableLiveData<Result<List<FavoritePlace>>> findAllByUserResponseLiveData = new MutableLiveData<>();
        try {
            Call<ResponseDto<List<FavoritePlaceDto>>> call = favoritePlaceApi.findAllByUser(bearerToken);
            call.enqueue(new Callback<ResponseDto<List<FavoritePlaceDto>>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<List<FavoritePlaceDto>>> call,
                                       @NonNull Response<ResponseDto<List<FavoritePlaceDto>>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Result<List<FavoritePlace>> result = new Result<>();
                        result.setError(response.body().isError());
                        result.setMessage(response.body().getMessage());
                        if (response.body().getData() != null) {
                            result.setData(objectMapper.mapList(response.body().getData(), FavoritePlace.class));
                        }
                        findAllByUserResponseLiveData.postValue(result);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<List<FavoritePlaceDto>>> call,
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
    public LiveData<Result<FavoritePlace>> createFavoritePlace(String bearerToken, Long placeId) {
        MutableLiveData<Result<FavoritePlace>> createFavoritePlaceResponseLiveData = new MutableLiveData<>();
        try {
            Call<ResponseDto<FavoritePlaceDto>> call = favoritePlaceApi.createFavoritePlace(bearerToken, placeId);
            call.enqueue(new Callback<ResponseDto<FavoritePlaceDto>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<FavoritePlaceDto>> call,
                                       @NonNull Response<ResponseDto<FavoritePlaceDto>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Result<FavoritePlace> result = new Result<>();
                        result.setError(response.body().isError());
                        result.setMessage(response.body().getMessage());
                        if (response.body().getData() != null) {
                            result.setData(objectMapper.map(response.body().getData(), FavoritePlace.class));
                        }
                        createFavoritePlaceResponseLiveData.postValue(result);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<FavoritePlaceDto>> call,
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
    public LiveData<Result<FavoritePlace>> deleteById(String bearerToken, Long id) {
        MutableLiveData<Result<FavoritePlace>> deleteByIdResponseLiveData = new MutableLiveData<>();
        try {
            Call<ResponseDto<FavoritePlaceDto>> call = favoritePlaceApi.deleteById(bearerToken, id);
            call.enqueue(new Callback<ResponseDto<FavoritePlaceDto>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<FavoritePlaceDto>> call,
                                       @NonNull Response<ResponseDto<FavoritePlaceDto>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Result<FavoritePlace> result = new Result<>();
                        result.setError(response.body().isError());
                        result.setMessage(response.body().getMessage());
                        if (response.body().getData() != null) {
                            result.setData(objectMapper.map(response.body().getData(), FavoritePlace.class));
                        }
                        deleteByIdResponseLiveData.postValue(result);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<FavoritePlaceDto>> call,
                                      @NonNull Throwable throwable) {
                    Log.e("FavoritePlaceRepositoryImpl", "Error during onFailure: " + throwable.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e("FavoritePlaceRepositoryImpl", "Error during create comment", e);
        }
        return deleteByIdResponseLiveData;
    }
}
