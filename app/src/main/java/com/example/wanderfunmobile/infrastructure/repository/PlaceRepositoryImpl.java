package com.example.wanderfunmobile.infrastructure.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.wanderfunmobile.application.dto.ResponseDto;
import com.example.wanderfunmobile.application.dto.place.PlaceDto;
import com.example.wanderfunmobile.application.repository.PlaceRepository;
import com.example.wanderfunmobile.infrastructure.api.backend.PlaceApi;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceRepositoryImpl implements PlaceRepository {
    private final PlaceApi placeApi;

    @Inject
    public PlaceRepositoryImpl(PlaceApi placeApi) {
        this.placeApi = placeApi;
    }

    @Override
    public LiveData<ResponseDto<List<PlaceDto>>> getAllPlaces() {
        MutableLiveData<ResponseDto<List<PlaceDto>>> getAllPlacesResponseLiveData = new MutableLiveData<>();
        String errorType = "PlaceRepositoryImpl GetAllPlaces Error";
        try {
            Call<ResponseDto<List<PlaceDto>>> call = placeApi.getAllPlaces();
            call.enqueue(new Callback<ResponseDto<List<PlaceDto>>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<List<PlaceDto>>> call,
                                       @NonNull Response<ResponseDto<List<PlaceDto>>> response) {
                    getAllPlacesResponseLiveData.postValue(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<List<PlaceDto>>> call,
                                      @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during onFailure");
                }
            });

        } catch (Exception e) {
            Log.e(errorType, "Error during catch");
        }

        return getAllPlacesResponseLiveData;
    }

    @Override
    public LiveData<ResponseDto<PlaceDto>> getPlaceById(Long placeId) {
        MutableLiveData<ResponseDto<PlaceDto>> getPlaceByIdResponseLiveData = new MutableLiveData<>();
        String errorType = "PlaceRepositoryImpl GetPlaceById Error";

        try {
            Call<ResponseDto<PlaceDto>> call = placeApi.getPlaceById(placeId);
            call.enqueue(new Callback<ResponseDto<PlaceDto>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<PlaceDto>> call,
                                       @NonNull Response<ResponseDto<PlaceDto>> response) {
                    getPlaceByIdResponseLiveData.postValue(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<PlaceDto>> call,
                                      @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during onFailure");
                }
            });
        } catch (Exception e) {
            Log.e(errorType, "Error during catch");
        }

        return getPlaceByIdResponseLiveData;
    }
}
